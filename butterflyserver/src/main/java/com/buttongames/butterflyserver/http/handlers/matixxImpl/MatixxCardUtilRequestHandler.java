package com.buttongames.butterflyserver.http.handlers.matixxImpl;

import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerProfile;
import com.buttongames.butterflyserver.http.exception.InvalidRequestException;
import com.buttongames.butterflyserver.http.exception.UnsupportedRequestException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import spark.Request;
import spark.Response;

import java.util.Random;

/**
 * Handler for any requests that come to the <code>matixx_shopinfo</code> module.
 * @author
 */
@Component
public class MatixxCardUtilRequestHandler extends BaseRequestHandler {
    private final Logger LOG = LogManager.getLogger(MatixxShopInfoRequestHandler.class);

    /**
     * The DAO for managing users in the database.
     */
    private final ButterflyUserDao userDao;

    /**
     * The DAO for managing cards in the database.
     */
    private final CardDao cardDao;

    private final MatixxProfileDao matixxProfileDao;

    public MatixxCardUtilRequestHandler(ButterflyUserDao userDao, CardDao cardDao, MatixxProfileDao matixxProfileDao) {
        this.userDao = userDao;
        this.cardDao = cardDao;
        this.matixxProfileDao = matixxProfileDao;
    }

    /**
     * Handles an incoming request for the <code>matixx_shopinfo</code> module.
     * @param requestBody The XML document of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        if (requestMethod.equals("regist")) {
            return handleRegistRequest(requestBody,request, response);
        }else if (requestMethod.equals("check")){
            return handleCheckRequest(requestBody,request, response);
        }

        throw new UnsupportedRequestException();
    }

    private Object handleCheckRequest(final Element requestBody,Request request, Response response) {

        NodeList players = XmlUtils.nodesAtPath(requestBody,"/matixx_cardutil/player");
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("matixx_cardutil");

        for(int i=0;i<players.getLength();i++){
            Element player = (Element) players.item(i);
            String refid = XmlUtils.strAtPath(player,"/refid");
            Card card = cardDao.findByRefId(refid);

            if (card == null) {
                throw new InvalidRequestException();
            }

            matixxPlayerProfile matixxplayer;
                matixxplayer = matixxProfileDao.findByCard(card);

            if(matixxplayer!=null){
                String[] skilldata = matixxplayer.getSkilldata().split(",");
                respBuilder.e("player").attr("no", String.valueOf(i + 1)).attr("state", "2")
                        .str("name",matixxplayer.getName()).up()
                        .s32("did",matixxplayer.getDid()).up()
                        .s32("charaid",matixxplayer.getCharaid()).up()
                        .e("skilldata").s32("skill",new Integer(skilldata[0])).up()
                        .s32("all_skill",new Integer(skilldata[0])).up()
                        .s32("old_skill",new Integer(skilldata[0])).up()
                        .s32("old_all_skill",new Integer(skilldata[0])).up();

            }else{
                respBuilder.e("player").attr("no", String.valueOf(i + 1)).attr("state", "1").up();
            }

        }
        return this.sendResponse(request, response, respBuilder);

    }

    private Object handleRegistRequest(final Element requestBody,Request request, Response response) {

        NodeList players = XmlUtils.nodesAtPath(requestBody,"/matixx_cardutil/player");

        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("matixx_cardutil");

        for(int i=0;i<players.getLength();i++) {

            Element player = (Element) players.item(i);
            String refid = XmlUtils.strAtPath(player, "/refid");
            Card card = cardDao.findByRefId(refid);

            if (card == null) {
                throw new InvalidRequestException();
            }

            matixxPlayerProfile matixxplayer = matixxProfileDao.findByCard(card);

            if (matixxplayer != null){
                throw new InvalidRequestException();
            }

            //create new player profile
            int did = new Random().nextInt(99999999);
            while (matixxProfileDao.findByDid(did)!=null) {
                did = new Random().nextInt(99999999);
            }
            String didString = String.format("%08d", did);
            String playinfo = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
            String customdata = "<customdata>" +
                    "<playstyle __type=\"s32\" __count=\"50\">0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 20 0 0 0 0 0 0 0 0 0 0 0 20 0</playstyle>" +
                    "<custom __type=\"s32\" __count=\"50\">0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0</custom>" +
                    "</customdata>";
            String skilldata = "0,0";
            String secretmusic = "<secretmusic/>";
            String favoritemusic = "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1," +
                    "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1," +
                    "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1";
            String chara_list = "<chara_list><chara><charaid __type=\"s32\">1</charaid></chara></chara_list>";
            String title_parts = "<title_parts/>";
            String information = "<information><info __type=\"u32\" __count=\"50\">0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0</info></information>";
            String reward = "<reward><status __type=\"u32\" __count=\"50\">0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0</status></reward>";
            String groove = "0,0,0,0";
            String tutorial = "0,0";
            String gf_record = "<gf>" +
                    "<max_record>" +
                    "<skill __type=\"s32\">0</skill>" +
                    "<all_skill __type=\"s32\">0</all_skill>" +
                    "<clear_diff __type=\"s32\">0</clear_diff>" +
                    "<full_diff __type=\"s32\">0</full_diff>" +
                    "<exce_diff __type=\"s32\">0</exce_diff>" +
                    "<clear_music_num __type=\"s32\">0</clear_music_num>" +
                    "<full_music_num __type=\"s32\">0</full_music_num>" +
                    "<exce_music_num __type=\"s32\">0</exce_music_num>" +
                    "<clear_seq_num __type=\"s32\">0</clear_seq_num>" +
                    "</max_record>" +
                    "<diff_record>" +
                    "<diff_100_nr __type=\"s32\">0</diff_100_nr>" +
                    "<diff_150_nr __type=\"s32\">0</diff_150_nr>" +
                    "<diff_200_nr __type=\"s32\">0</diff_200_nr>" +
                    "<diff_250_nr __type=\"s32\">0</diff_250_nr>" +
                    "<diff_300_nr __type=\"s32\">0</diff_300_nr>" +
                    "<diff_350_nr __type=\"s32\">0</diff_350_nr>" +
                    "<diff_400_nr __type=\"s32\">0</diff_400_nr>" +
                    "<diff_450_nr __type=\"s32\">0</diff_450_nr>" +
                    "<diff_500_nr __type=\"s32\">0</diff_500_nr>" +
                    "<diff_550_nr __type=\"s32\">0</diff_550_nr>" +
                    "<diff_600_nr __type=\"s32\">0</diff_600_nr>" +
                    "<diff_650_nr __type=\"s32\">0</diff_650_nr>" +
                    "<diff_700_nr __type=\"s32\">0</diff_700_nr>" +
                    "<diff_750_nr __type=\"s32\">0</diff_750_nr>" +
                    "<diff_800_nr __type=\"s32\">0</diff_800_nr>" +
                    "<diff_850_nr __type=\"s32\">0</diff_850_nr>" +
                    "<diff_900_nr __type=\"s32\">0</diff_900_nr>" +
                    "<diff_950_nr __type=\"s32\">0</diff_950_nr>" +
                    "<diff_100_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_100_clear>" +
                    "<diff_150_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_150_clear>" +
                    "<diff_200_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_200_clear>" +
                    "<diff_250_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_250_clear>" +
                    "<diff_300_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_300_clear>" +
                    "<diff_350_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_350_clear>" +
                    "<diff_400_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_400_clear>" +
                    "<diff_450_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_450_clear>" +
                    "<diff_500_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_500_clear>" +
                    "<diff_550_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_550_clear>" +
                    "<diff_600_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_600_clear>" +
                    "<diff_650_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_650_clear>" +
                    "<diff_700_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_700_clear>" +
                    "<diff_750_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_750_clear>" +
                    "<diff_800_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_800_clear>" +
                    "<diff_850_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_850_clear>" +
                    "<diff_900_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_900_clear>" +
                    "<diff_950_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_950_clear>" +
                    "</diff_record>" +
                    "</gf>";
            String dm_record = "<dm>" +
                    "<max_record>" +
                    "<skill __type=\"s32\">0</skill>" +
                    "<all_skill __type=\"s32\">0</all_skill>" +
                    "<clear_diff __type=\"s32\">0</clear_diff>" +
                    "<full_diff __type=\"s32\">0</full_diff>" +
                    "<exce_diff __type=\"s32\">0</exce_diff>" +
                    "<clear_music_num __type=\"s32\">0</clear_music_num>" +
                    "<full_music_num __type=\"s32\">0</full_music_num>" +
                    "<exce_music_num __type=\"s32\">0</exce_music_num>" +
                    "<clear_seq_num __type=\"s32\">0</clear_seq_num>" +
                    "</max_record>" +
                    "<diff_record>" +
                    "<diff_100_nr __type=\"s32\">0</diff_100_nr>" +
                    "<diff_150_nr __type=\"s32\">0</diff_150_nr>" +
                    "<diff_200_nr __type=\"s32\">0</diff_200_nr>" +
                    "<diff_250_nr __type=\"s32\">0</diff_250_nr>" +
                    "<diff_300_nr __type=\"s32\">0</diff_300_nr>" +
                    "<diff_350_nr __type=\"s32\">0</diff_350_nr>" +
                    "<diff_400_nr __type=\"s32\">0</diff_400_nr>" +
                    "<diff_450_nr __type=\"s32\">0</diff_450_nr>" +
                    "<diff_500_nr __type=\"s32\">0</diff_500_nr>" +
                    "<diff_550_nr __type=\"s32\">0</diff_550_nr>" +
                    "<diff_600_nr __type=\"s32\">0</diff_600_nr>" +
                    "<diff_650_nr __type=\"s32\">0</diff_650_nr>" +
                    "<diff_700_nr __type=\"s32\">0</diff_700_nr>" +
                    "<diff_750_nr __type=\"s32\">0</diff_750_nr>" +
                    "<diff_800_nr __type=\"s32\">0</diff_800_nr>" +
                    "<diff_850_nr __type=\"s32\">0</diff_850_nr>" +
                    "<diff_900_nr __type=\"s32\">0</diff_900_nr>" +
                    "<diff_950_nr __type=\"s32\">0</diff_950_nr>" +
                    "<diff_100_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_100_clear>" +
                    "<diff_150_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_150_clear>" +
                    "<diff_200_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_200_clear>" +
                    "<diff_250_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_250_clear>" +
                    "<diff_300_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_300_clear>" +
                    "<diff_350_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_350_clear>" +
                    "<diff_400_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_400_clear>" +
                    "<diff_450_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_450_clear>" +
                    "<diff_500_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_500_clear>" +
                    "<diff_550_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_550_clear>" +
                    "<diff_600_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_600_clear>" +
                    "<diff_650_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_650_clear>" +
                    "<diff_700_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_700_clear>" +
                    "<diff_750_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_750_clear>" +
                    "<diff_800_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_800_clear>" +
                    "<diff_850_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_850_clear>" +
                    "<diff_900_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_900_clear>" +
                    "<diff_950_clear __type=\"s32\" __count=\"7\">0 0 0 0 0 0 0</diff_950_clear>" +
                    "</diff_record>" +
                    "</dm>";

            String battledata = "<battledata>" +
                    "<info>" +
                    "<orb __type=\"s32\">0</orb>" +
                    "<get_gb_point __type=\"s32\">0</get_gb_point>" +
                    "<send_gb_point __type=\"s32\">0</send_gb_point>" +
                    "</info>" +
                    "<greeting>" +
                    "<greeting_1 __type=\"str\">Thanks!</greeting_1>" +
                    "<greeting_2 __type=\"str\">Hello!</greeting_2>" +
                    "<greeting_3 __type=\"str\">Wait a moment.</greeting_3>" +
                    "<greeting_4 __type=\"str\">I'll try my best!</greeting_4>" +
                    "<greeting_5 __type=\"str\">I'll go with my favorite songs!</greeting_5>" +
                    "<greeting_6 __type=\"str\">I go with my favorite songs!</greeting_6>" +
                    "<greeting_7 __type=\"str\">I don't feel confident.</greeting_7>" +
                    "<greeting_8 __type=\"str\">Thank you!</greeting_8>" +
                    "<greeting_9 __type=\"str\">See you!</greeting_9>" +
                    "</greeting>" +
                    "<setting>" +
                    "<matching __type=\"s32\">0</matching>" +
                    "<info_level __type=\"s32\">0</info_level>" +
                    "</setting>" +
                    "<score>" +
                    "<battle_class __type=\"s32\">0</battle_class>" +
                    "<max_battle_class __type=\"s32\">0</max_battle_class>" +
                    "<battle_point __type=\"s32\">0</battle_point>" +
                    "<win __type=\"s32\">0</win>" +
                    "<lose __type=\"s32\">0</lose>" +
                    "<draw __type=\"s32\">0</draw>" +
                    "<consecutive_win __type=\"s32\">0</consecutive_win>" +
                    "<max_consecutive_win __type=\"s32\">0</max_consecutive_win>" +
                    "<glorious_win __type=\"s32\">0</glorious_win>" +
                    "<max_defeat_skill __type=\"s32\">0</max_defeat_skill>" +
                    "<latest_result __type=\"s32\">0</latest_result>" +
                    "</score>" +
                    "<history/>" +
                    "</battledata>";
            String monthly_skill = "<monthly_skill/>";
            matixxPlayerProfile newplayer = new matixxPlayerProfile(card,1,new Integer(didString),"Player","NEW PLAYER",0,playinfo,customdata,customdata,skilldata,
                    secretmusic,favoritemusic,chara_list,title_parts,information,reward,groove,tutorial,gf_record,dm_record,battledata,monthly_skill);

            matixxProfileDao.create(newplayer);

            respBuilder.e("player").attr("no", String.valueOf(i + 1)).s32("did", did).up()
                    .bool("is_succession",false).up().up();
        }

        return this.sendResponse(request,response,respBuilder);
    }
}
