package com.buttongames.butterflyserver.http.handlers.matixxImpl;

import com.buttongames.butterflycore.util.ObjectUtils;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxEventDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxPlayerboardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxStageDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerProfile;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerboard;
import com.buttongames.butterflymodel.model.gdmatixx.matixxStageRecord;
import com.buttongames.butterflyserver.http.exception.InvalidRequestException;
import com.buttongames.butterflyserver.http.exception.UnsupportedRequestException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for any requests that come to the <code>matixx_gametop</code> module.
 * @author player-guest
 */
@Component
public class MatixxGameTopRequestHandler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(MatixxShopInfoRequestHandler.class);

    private final CardDao cardDao;

    private final MatixxProfileDao matixxProfileDao;

    private final MatixxStageDao matixxStageDao;

    private final MatixxPlayerboardDao matixxPlayerboardDao;

    private final MatixxEventDao matixxEventDao;

    public MatixxGameTopRequestHandler(CardDao cardDao, MatixxProfileDao matixxProfileDao, MatixxStageDao matixxStageDao, MatixxPlayerboardDao matixxPlayerboardDao, MatixxEventDao matixxEventDao) {
        this.cardDao = cardDao;
        this.matixxProfileDao = matixxProfileDao;
        this.matixxStageDao = matixxStageDao;
        this.matixxPlayerboardDao = matixxPlayerboardDao;
        this.matixxEventDao = matixxEventDao;
    }

    /**
     * Handles an incoming request for the <code>matixx_gametop</code> module.
     * this is the request handle user login info
     * @param requestBody The XML document of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        switch (requestMethod) {
            case "get":
                return handleGetRequest(requestBody, request, response);
            default:
                throw new UnsupportedRequestException();
        }
    }

    private Object handleGetRequest(final Element requestBody,Request request, Response response) {

        final String type = request.attribute("type");
        NodeList players = XmlUtils.nodesAtPath(requestBody,"/matixx_gametop/player");
        KXmlBuilder respBuilder = KXmlBuilder.create("response");

        for(int i=0;i<players.getLength();i++){
            Element player = (Element) players.item(i);
            final String refid = XmlUtils.strAtChild(player,"refid");

            Card card = cardDao.findByRefId(refid);

            if (card == null) {
                throw new InvalidRequestException();
            }

            matixxPlayerProfile matixxplayer;
            matixxplayer = matixxProfileDao.findByCard(card);


            respBuilder.e("matixx_gametop").e("player")
                    .u64("now_date", TimeUtils.getTime()).up();


            final Document document = respBuilder.getDocument();
            String path = "/matixx_gametop/player";


            List<matixxPlayerboard> pblist = matixxPlayerboardDao.findByCard(card);
            boolean is_active = pblist != null;
            Node playerboardNode = KXmlBuilder.create("playerboard")
                    .s32("index", 1).up()
                    .bool("is_active", is_active).up().getElement();
            XmlUtils.importNodeToPath(document, playerboardNode, path);
            for (matixxPlayerboard sticker : pblist) {
                Node stickerNode = KXmlBuilder.create("sticker")
                        .s32("id", sticker.getStickerId()).up()
                        .flo("pos_x", sticker.getPos_x()).up()
                        .flo("pos_y", sticker.getPos_y()).up()
                        .flo("scale_x", sticker.getScale_x()).up()
                        .flo("scale_y", sticker.getScale_y()).up()
                        .flo("rotate", sticker.getRotate()).up().up().getElement();
                XmlUtils.importNodeToPath(document, stickerNode, "/matixx_gametop/player/playerboard");
            }


            Node player_info = KXmlBuilder.create("player_info").s8("player_type",matixxplayer.getPlayer_type()).up()
                    .s32("did",matixxplayer.getDid()).up()
                    .str("name",matixxplayer.getName()).up()
                    .str("title",matixxplayer.getTitle()).up()
                    .s32("charaid",matixxplayer.getCharaid()).up().getElement();
            XmlUtils.importNodeToPath(document,player_info,path);


            String[] playinfo = matixxplayer.getPlayinfo().split(",");
            Node playinfonode = KXmlBuilder.create("playinfo")
                    .s32("cabid", playinfo[0]).up()
                    .s32("play", playinfo[1]).up()
                    .s32("playtime", playinfo[2]).up()
                    .s32("playterm", playinfo[3]).up()
                    .s32("session_cnt", playinfo[4]).up()
                    .s32("matching_num", playinfo[5]).up()
                    .s32("extra_stage", playinfo[6]).up()
                    .s32("extra_play", playinfo[7]).up()
                    .s32("extra_clear", playinfo[8]).up()
                    .s32("encore_play", playinfo[9]).up()
                    .s32("encore_clear", playinfo[10]).up()
                    .s32("pencore_play", playinfo[11]).up()
                    .s32("pencore_clear", playinfo[12]).up()
                    .s32("max_clear_diff", playinfo[13]).up()
                    .s32("max_full_diff", playinfo[14]).up()
                    .s32("max_exce_diff", playinfo[15]).up()
                    .s32("clear_num", playinfo[16]).up()
                    .s32("full_num", playinfo[17]).up()
                    .s32("exce_num", playinfo[18]).up()
                    .s32("no_num", playinfo[19]).up()
                    .s32("e_num", playinfo[20]).up()
                    .s32("d_num", playinfo[21]).up()
                    .s32("c_num", playinfo[22]).up()
                    .s32("b_num", playinfo[23]).up()
                    .s32("a_num", playinfo[24]).up()
                    .s32("s_num", playinfo[25]).up()
                    .s32("ss_num", playinfo[26]).up()
                    .s32("last_category", playinfo[27]).up()
                    .s32("last_musicid", playinfo[28]).up()
                    .s32("last_seq", playinfo[29]).up()
                    .s32("disp_level", playinfo[30]).up().up().getElement();
            XmlUtils.importNodeToPath(document,playinfonode,path);

            if(type.equals("a")){
                XmlUtils.importStringToPath(document,matixxplayer.getCustomdata_gf(),path);
            }else if(type.equals("b")){
                XmlUtils.importStringToPath(document,matixxplayer.getCustomdata_dm(),path);
            }

            String[] skilldata  = matixxplayer.getSkilldata().split(",");
            Node skilldatanode = KXmlBuilder.create("skilldata").s32("skill",new Integer(skilldata[0])).up()
                    .s32("all_skill",new Integer(skilldata[0])).up()
                    .s32("old_skill",new Integer("0")).up()
                    .s32("old_all_skill",new Integer("0")).up().up().getElement();
            XmlUtils.importNodeToPath(document,skilldatanode,path);

            //This is for unlock song
            XmlUtils.importStringToPath(document, matixxplayer.getSecretmusic(), path);

            String[] favmuisc = matixxplayer.getFavoritemusic().split(",");
            Node favoritemusic = KXmlBuilder.create("favoritemusic")
                    .s32("list_1", "100", favmuisc[0]).up()
                    .s32("list_2", "100", favmuisc[1]).up()
                    .s32("list_3", "100", favmuisc[2]).up().up().getElement();
            XmlUtils.importNodeToPath(document,favoritemusic,path);


            if(matixxplayer.getChara_list()==null||matixxplayer.getChara_list().equals("")){
                XmlUtils.importStringToPath(document, "<chara_list><chara><charaid __type=\"s32\">1</charaid></chara></chara_list>", path);
            }else {
                XmlUtils.importStringToPath(document, matixxplayer.getChara_list(), path);
            }
            XmlUtils.importStringToPath(document,matixxplayer.getTitle_parts(),path);

            XmlUtils.importStringToPath(document,matixxplayer.getInformation(),path);

            XmlUtils.importStringToPath(document,matixxplayer.getReward(),path);

            String[] groove = ObjectUtils.checkNull(matixxplayer.getGroove(),"0,0,0,0").split(",");
            Node groovenode = KXmlBuilder.create("groove").s32("extra_gauge",new Integer(groove[0])).up()
                    .s32("encore_gauge",new Integer(groove[1])).up()
                    .s32("encore_cnt",new Integer(groove[2])).up()
                    .s32("encore_success",new Integer(groove[3])).up().up().getElement();
            XmlUtils.importNodeToPath(document,groovenode,path);

            XmlUtils.importStringToPath(document,"<rivaldata/>",path);

            String[] tutorial = ObjectUtils.checkNull(matixxplayer.getTutorial(),"0,0").split(",");
            Node tutorialnode = KXmlBuilder.create("tutorial").s32("progress",new Integer(tutorial[0])).up()
                    .u32("disp_state",new Integer(tutorial[1])).up().up().getElement();
            XmlUtils.importNodeToPath(document,tutorialnode,path);


            XmlUtils.createNodeToPath(document,"record",path);
            XmlUtils.importStringToPath(document,matixxplayer.getGf_record(),"/matixx_gametop/player/record");
            XmlUtils.importStringToPath(document,matixxplayer.getDm_record(),"/matixx_gametop/player/record");

            XmlUtils.importStringToPath(document,matixxplayer.getBattledata(),path);

            XmlUtils.importStringToPath(document,"<is_free_ok __type=\"bool\">0</is_free_ok>",path);

            Node ranking = KXmlBuilder.create("ranking").e("skill").s32("rank",1).up()
                    .s32("total_nr",16).up().up()
                    .e("all_skill").s32("rank",1).up()
                    .s32("total_nr",16).up().up().getElement();
            XmlUtils.importNodeToPath(document,ranking,path);

            String stageresult = "<stage_result><data><musicid __type=\"s32\">-1</musicid></data></stage_result>";
            XmlUtils.importStringToPath(document,stageresult,path);

            XmlUtils.importStringToPath(document,"<monthly_skill/>",path);

            XmlUtils.importStringToPath(document,"<light_mode_reward_item>\n" +
                    "<itemid __type=\"s32\">0</itemid>\n" +
                    "<rarity __type=\"s32\">0</rarity>\n" +
                    "</light_mode_reward_item>",path);

            XmlUtils.importStringToPath(document,"<standard_mode_reward_item>\n" +
                    "<itemid __type=\"s32\">0</itemid>\n" +
                    "<rarity __type=\"s32\">0</rarity>\n" +
                    "</standard_mode_reward_item>",path);

            XmlUtils.importStringToPath(document,"<delux_mode_reward_item>\n" +
                    "<itemid __type=\"s32\">0</itemid>\n" +
                    "<rarity __type=\"s32\">0</rarity>\n" +
                    "</delux_mode_reward_item>",path);

            XmlUtils.importStringToPath(document,"<event_skill>\n" +
                    "<skill __type=\"s32\">0</skill>\n" +
                    "<ranking>\n" +
                    "<rank __type=\"s32\">0</rank>\n" +
                    "<total_nr __type=\"s32\">0</total_nr>\n" +
                    "</ranking>\n" +
                    "</event_skill>\n",path);

            XmlUtils.importStringToPath(document,"<event_score>\n" +
                    "<eventlist>\n" +
                    "<event>\n" +
                    "<eventid __type=\"s32\">0</eventid>\n" +
                    "<skill __type=\"s32\">0</skill>\n" +
                    "<perc __type=\"s32\">0</perc>\n" +
                    "<is_target __type=\"bool\">0</is_target>\n" +
                    "</event>\n" +
                    "</eventlist>\n" +
                    "</event_score>",path);



            String monster = "<monstar_subjugation>\n" +
                    "<monstar_subjugation_1>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point_1 __type=\"s32\">0</point_1>\n" +
                    "<point_2 __type=\"s32\">0</point_2>\n" +
                    "<point_3 __type=\"s32\">0</point_3>\n" +
                    "</monstar_subjugation_1>\n" +
                    "<monstar_subjugation_2>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point_1 __type=\"s32\">0</point_1>\n" +
                    "<point_2 __type=\"s32\">0</point_2>\n" +
                    "<point_3 __type=\"s32\">0</point_3>\n" +
                    "</monstar_subjugation_2>\n" +
                    "<monstar_subjugation_3>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point_1 __type=\"s32\">0</point_1>\n" +
                    "<point_2 __type=\"s32\">0</point_2>\n" +
                    "<point_3 __type=\"s32\">0</point_3>\n" +
                    "</monstar_subjugation_3>\n" +
                    "<monstar_subjugation_4>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point_1 __type=\"s32\">0</point_1>\n" +
                    "<point_2 __type=\"s32\">0</point_2>\n" +
                    "<point_3 __type=\"s32\">0</point_3>\n" +
                    "</monstar_subjugation_4>\n" +
                    "</monstar_subjugation>\n";

            XmlUtils.importStringToPath(document,monster,path);

            for(int j = 1;j<=17;j++){
                Node tempnode;
                if(j==1) {
                    tempnode = KXmlBuilder.create("phrase_combo_challenge").s32("point", 0).root().getElement();
                }else{
                    tempnode = KXmlBuilder.create("phrase_combo_challenge_"+j).s32("point", 0).root().getElement();
                }
                XmlUtils.importNodeToPath(document,tempnode,path);
            }

            for(int k = 1;k<=3;k++){
                Node tempnode;
                tempnode = KXmlBuilder.create("kouyou_challenge_"+k).s32("point", 0).root().getElement();
                XmlUtils.importNodeToPath(document,tempnode,path);
            }

            XmlUtils.importStringToPath(document,"<sticker_campaign>\n" +
                    "<data>\n" +
                    "<term __type=\"u8\">0</term>\n" +
                    "<point __type=\"s32\">0</point>\n" +
                    "</data>\n" +
                    "</sticker_campaign>\n",path);

            XmlUtils.importStringToPath(document,"<kac2017><entry_status __type=\"s32\">0</entry_status><data><term __type=\"s32\">0</term>\n" +
                    "<total_score __type=\"s32\">0</total_score><score __type=\"s32\">0</score><music_type __type=\"s32\">0</music_type><play_count __type=\"s32\">0</play_count></data></kac2017>",path);

            XmlUtils.importStringToPath(document,"<bear_fes>\n" +
                    "<bear_fes_1>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point __type=\"s32\" __count=\"8\">0 0 0 0 0 0 0 0</point>\n" +
                    "</bear_fes_1>\n" +
                    "<bear_fes_2>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point __type=\"s32\" __count=\"8\">0 0 0 0 0 0 0 0</point>\n" +
                    "</bear_fes_2>\n" +
                    "<bear_fes_3>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point __type=\"s32\" __count=\"8\">0 0 0 0 0 0 0 0</point>\n" +
                    "</bear_fes_3>\n" +
                    "<bear_fes_4>\n" +
                    "<stage __type=\"s32\">0</stage>\n" +
                    "<point __type=\"s32\" __count=\"8\">0 0 0 0 0 0 0 0</point>\n" +
                    "</bear_fes_4>\n" +
                    "</bear_fes>\n",path);

            XmlUtils.importStringToPath(document,"<jubeat_omiyage_challenge/>",path);

            XmlUtils.importStringToPath(document,"<nostalgia_concert/>",path);

            XmlUtils.importStringToPath(document,"<bemani_summer_2018>\n" +
                    "<linkage_id __type=\"s32\">0</linkage_id>\n" +
                    "<is_entry __type=\"bool\">0</is_entry>\n" +
                    "<target_music_idx __type=\"s32\">0</target_music_idx>\n" +
                    "<point_1 __type=\"s32\">0</point_1>\n" +
                    "<point_2 __type=\"s32\">0</point_2>\n" +
                    "<point_3 __type=\"s32\">0</point_3>\n" +
                    "<point_4 __type=\"s32\">0</point_4>\n" +
                    "<point_5 __type=\"s32\">0</point_5>\n" +
                    "<point_6 __type=\"s32\">0</point_6>\n" +
                    "<point_7 __type=\"s32\">0</point_7>\n" +
                    "<reward_1 __type=\"bool\">0</reward_1>\n" +
                    "<reward_2 __type=\"bool\">0</reward_2>\n" +
                    "<reward_3 __type=\"bool\">0</reward_3>\n" +
                    "<reward_4 __type=\"bool\">0</reward_4>\n" +
                    "<reward_5 __type=\"bool\">0</reward_5>\n" +
                    "<reward_6 __type=\"bool\">0</reward_6>\n" +
                    "<reward_7 __type=\"bool\">0</reward_7>\n" +
                    "<unlock_status_1 __type=\"s32\">0</unlock_status_1>\n" +
                    "<unlock_status_2 __type=\"s32\">0</unlock_status_2>\n" +
                    "<unlock_status_3 __type=\"s32\">0</unlock_status_3>\n" +
                    "<unlock_status_4 __type=\"s32\">0</unlock_status_4>\n" +
                    "<unlock_status_5 __type=\"s32\">0</unlock_status_5>\n" +
                    "<unlock_status_6 __type=\"s32\">0</unlock_status_6>\n" +
                    "<unlock_status_7 __type=\"s32\">0</unlock_status_7>\n" +
                    "</bemani_summer_2018>\n",path);

            XmlUtils.importStringToPath(document,"<long_otobear_fes_1>\n" +
                    "<point __type=\"s32\">0</point>\n" +
                    "</long_otobear_fes_1>\n",path);

            XmlUtils.importStringToPath(document,"<rockwave><score_list><score><data_id __type=\"s32\">0</data_id><point __type=\"u64\">0</point>\n" +
                    "<mtime __type=\"u64\">0</mtime><play_cnt __type=\"s32\">0</play_cnt><is_clear __type=\"bool\">0</is_clear></score></score_list></rockwave>",path);



            //musiclist
            List<matixxStageRecord> stageList = matixxStageDao.findByCard(card,type);
            HashMap<Integer, HashMap<Integer, Object[]>> userTopScores = this.sortScoresByTopScore(stageList);

            KXmlBuilder musiclist = KXmlBuilder.create("musiclist").attr("nr",String.valueOf(userTopScores.size()));
            for (Map.Entry<Integer, HashMap<Integer, Object[]>> entry : userTopScores.entrySet()) {
                musiclist = musiclist.e("musicdata").attr("musicid",String.valueOf(entry.getKey()));

                String meterstr = "";
                String meter_progstr = "";
                String mdata1 = "-1 ";
                String mdata2 = "";
                for(int l = 1;l<9;l++){
                    if (entry.getValue().containsKey(l)) {
                        Object[] record = entry.getValue().get(l);
                        matixxStageRecord topRecord = (matixxStageRecord) record[1];
                        meterstr = meterstr.concat(ObjectUtils.checkNull(topRecord.getMeter(),"0")+" ");
                        meter_progstr = meter_progstr.concat(ObjectUtils.checkNull(topRecord.getMeter_prog(),"-1")+" ");
                        mdata1 = mdata1.concat(ObjectUtils.checkNull(topRecord.getPerc(),"-1")+" ");
                        mdata2 = mdata2.concat(ObjectUtils.checkNull(topRecord.getRank(),"-1")+" ");

                    }else{
                        meterstr = meterstr.concat("0 ");
                        meter_progstr = meter_progstr.concat("-1 ");
                        mdata1 = mdata1.concat("-1 ");
                        mdata2 = mdata2.concat("-1 ");
                    }
                }

                matixxStageRecord maxskill = getMusicMaxSkill(entry.getValue());

                musiclist = musiclist.s16("mdata", "20", mdata1+mdata2+" -1 -1 -1").up()
                        .u16("flag","5","0 0 0 64 0").up()
                        .s16("sdata","2",maxskill.getSeq()+" "+maxskill.getSkill()).up()
                        .u64("meter", "8",meterstr).up()
                        .s16("meter_prog","8",meter_progstr).up().up();

            }
            //flag 64 0 0 0 0= BASS ADV fullcombo
            //0 64 0 0 0 0= BASS ADV Exc
            //

            XmlUtils.importNodeToPath(document,musiclist.getElement(),path);

            XmlUtils.importStringToPath(document,"<finish __type=\"bool\">1</finish>",path);



        }
        return this.sendResponse(request, response, respBuilder);

        // TODO: Handle two player request
    }

    /**
     * Sorts scores into a hierarchy of top percent by song/difficulty. Used for the various score requests.
     * @param records The records to sort
     * @return The sorted results
     */
    private HashMap<Integer, HashMap<Integer, Object[]>> sortScoresByTopScore(final List<matixxStageRecord> records) {
        // sort them by song and difficulty, then insert them into the response
        // key for top map is the song ID
        // key for the 2nd map is the difficulty
        // the array: a[0] = count, a[1] = top UserSongRecord
        final HashMap<Integer, HashMap<Integer, Object[]>> topScores = new HashMap<>();

        for (matixxStageRecord record : records) {
            if (!topScores.containsKey(record.getMusic().getMusicid())) {
                topScores.put(record.getMusic().getMusicid(), new HashMap<>());
                topScores.get(record.getMusic().getMusicid()).put(record.getSeq(), new Object[] { 1, record });
            } else {
                if (!topScores.get(record.getMusic().getMusicid()).containsKey(record.getSeq())) {
                    topScores.get(record.getMusic().getMusicid()).put(record.getSeq(), new Object[] { 1, record });
                } else {
                    Object[] currRecord = topScores.get(record.getMusic().getMusicid()).get(record.getSeq());
                    currRecord[0] = ((Integer) currRecord[0]) + 1;

                    if (((matixxStageRecord) currRecord[1]).getPerc() < record.getPerc()) {
                        currRecord[1] = record;
                    }

                    topScores.get(record.getMusic().getMusicid()).put(record.getSeq(), currRecord);
                }
            }
        }

        return topScores;
    }

    private matixxStageRecord getMusicMaxSkill(HashMap<Integer, Object[]> records){
        matixxStageRecord currentrecord = new matixxStageRecord();
        currentrecord.setSkill(0);
        for(int i = 1;i<9;i++){
            if(records.containsKey(i)){
                matixxStageRecord record = (matixxStageRecord) records.get(i)[1];
                if((record!=null)&&(record.getSkill()>currentrecord.getSkill())){
                    currentrecord = record;
                }
            }


        }
        return currentrecord;
    }
}
