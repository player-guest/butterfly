package com.buttongames.butterflyserver.http.handlers.matixxImpl;

import com.buttongames.butterflycore.util.ObjectUtils;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxEventDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxStageDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxMusic;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerProfile;
import com.buttongames.butterflymodel.model.gdmatixx.matixxStageRecord;
import com.buttongames.butterflyserver.http.exception.InvalidRequestException;
import com.buttongames.butterflyserver.http.exception.UnsupportedRequestException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;

/**
 * Handler for any requests that come to the <code>matixx_gameend</code> module.
 * @author player-guest
 */
@Component
public class MatixxGameEndRequestHandler extends BaseRequestHandler {
    private final Logger LOG = LogManager.getLogger(MatixxShopInfoRequestHandler.class);

    /**
     * The DAO for managing cards in the database.
     */
    private CardDao cardDao;

    /**
     * The DAO for managing matixx profiles in the database.
     */
    private MatixxProfileDao matixxProfileDao;

    /**
     * The DAO for managing matixx stage result in the database.
     */
    private MatixxStageDao matixxStageDao;

    /**
     * The DAO for managing matixx event progress in the database.
     */
    private MatixxEventDao matixxEventDao;

    /**
     * The DAO for managing matixx music in the database.
     */
    private MatixxMusicDao matixxMusicDao;

    public MatixxGameEndRequestHandler(CardDao cardDao, MatixxProfileDao matixxProfileDao,MatixxStageDao matixxStageDao, MatixxEventDao matixxEventDao, MatixxMusicDao matixxMusicDao) {
        this.cardDao = cardDao;
        this.matixxProfileDao = matixxProfileDao;
        this.matixxStageDao = matixxStageDao;
        this.matixxEventDao = matixxEventDao;
        this.matixxMusicDao = matixxMusicDao;
    }

    /**
     * Handles an incoming request for the <code>matixx_gameend</code> module.
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
        }

        throw new UnsupportedRequestException();
    }

    /**
     * Handles a request to save the game progress to db.
     * @param requestBody The XML document of the incoming request
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleRegistRequest(final Element requestBody,Request request, Response response) {
        final String type = request.attribute("type");

        NodeList players = XmlUtils.nodesAtPath(requestBody,"/matixx_gameend/player");

        Element e = (Element) XmlUtils.nodeAtPath(requestBody, "/matixx_gameend/gamemode");
        String gamemode = "game_mode";
        if(e!=null){
            gamemode = e.getAttribute("mode");
        }

        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("matixx_gameend").e("gamemode").a("mode", gamemode).up();

        if(players!=null) {
            for(int i=0;i<players.getLength();i++){
                final Element player = (Element) players.item(i);
                if(player.getAttribute("card").equals("use")){
                    // not save guest now
                    final String refid = XmlUtils.strAtPath(player,"/refid");

                    Card card = cardDao.findByRefId(refid);

                    if (card == null) {
                        throw new InvalidRequestException();
                    }

                    card.setLastPlayTime(LocalDateTime.now());
                    cardDao.update(card);

                    matixxPlayerProfile matixxplayer;
                    matixxplayer = matixxProfileDao.findByCard(card);

                    //customdata
                    if (type.equals("a")){
                        final Node customdata = XmlUtils.nodeAtPath(player,"/customdata");
                        String customdataxml = XmlUtils.getStringFromNode(customdata);
                        matixxplayer.setCustomdata_gf(customdataxml);
                        player.removeChild(customdata);
                    }else if(type.equals("b")){
                        final Node customdata = XmlUtils.nodeAtPath(player,"/customdata");
                        String customdataxml = XmlUtils.getStringFromNode(customdata);
                        matixxplayer.setCustomdata_dm(customdataxml);
                        player.removeChild(customdata);
                    }


                    final Element pinode = (Element) XmlUtils.nodeAtPath(player,"/playinfo");

                    String playinfo = "";
                    playinfo = playinfo.concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"cabid"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"play"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"playtime"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"playterm"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"session_cnt"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"matching_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"extra_stage"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"extra_play"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"extra_clear"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"encore_play"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"encore_clear"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"pencore_play"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"pencore_clear"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"max_clear_diff"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"max_full_diff"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"max_exce_diff"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"clear_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"full_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"exce_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"no_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"e_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"d_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"c_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"b_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"a_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"s_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"ss_num"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"last_category"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"last_musicid"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"last_seq"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtChild(pinode,"disp_level"),"0")+",");

                    matixxplayer.setPlayinfo(playinfo);
                    player.removeChild(pinode);

                    //title_parts
                    final Node title_parts = XmlUtils.nodeAtPath(player,"/title_parts");
                    String title_partsxml = XmlUtils.getStringFromNode(title_parts);
                    player.removeChild(title_parts);

                    //secretmusic
                    final Node secretmusic = XmlUtils.nodeAtPath(player,"/secretmusic");
                    String secretmusicxml = XmlUtils.getStringFromNode(secretmusic);
                    matixxplayer.setSecretmusic(secretmusicxml);
                    player.removeChild(secretmusic);

                    //trbitem


                    //chara_list
                    final Node chara_list = XmlUtils.nodeAtPath(player,"/chara_list");
                    String chara_listxml = XmlUtils.getStringFromNode(chara_list);
                    matixxplayer.setChara_list(chara_listxml);
                    player.removeChild(chara_list);

                    //tutorial
                    String tutorial = "".concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player, "/tutorial/progress"), "0") + ",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player,"/tutorial/disp_state"),"0"));
                    matixxplayer.setTutorial(tutorial);
                    player.removeChild(XmlUtils.nodeAtPath(player,"/tutorial"));

                    //information (maybe just save the content)
                    final Node information = XmlUtils.nodeAtPath(player,"/information");
                    matixxplayer.setInformation(XmlUtils.getStringFromNode(information));
                    player.removeChild(information);

                    //reward
                    final Node reward = XmlUtils.nodeAtPath(player,"/reward");
                    matixxplayer.setReward(XmlUtils.getStringFromNode(reward));
                    player.removeChild(reward);

                    //skilldata
                    String skilldata = "".concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player, "/skilldata/skill"), "0") + ",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player,"/skilldata/all_skill"),"0"));
                    matixxplayer.setSkilldata(skilldata);
                    player.removeChild(XmlUtils.nodeAtPath(player,"/skilldata"));


                    //groove (maybe for encore gauge
                    String groove = "".concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player, "/groove/extra_gauge"), "0") + ",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player,"/groove/encore_gauge"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player,"/groove/encore_cnt"),"0")+",")
                            .concat(ObjectUtils.checkNull(XmlUtils.strAtPath(player,"/groove/encore_success"),"0"));
                    matixxplayer.setGroove(groove);
                    player.removeChild(XmlUtils.nodeAtPath(player,"/groove"));

                    //record (gf or dm?)
                    final Node record = XmlUtils.nodeAtPath(player,"/record");

                    Node max = XmlUtils.nodeAtPath((Element) record,"/max");
                    if(max!=null) {
                        max.getOwnerDocument().renameNode(max,max.getNamespaceURI(),"max_record");
                        Node diff = XmlUtils.nodeAtPath((Element) record,"/diff");
                        diff.getOwnerDocument().renameNode(diff,diff.getNamespaceURI(),"diff_record");
                        if(type.equals("a")){
                            record.getOwnerDocument().renameNode(record,record.getNamespaceURI(),"gf");
                            String recordxml = XmlUtils.getStringFromNode(record);
                            matixxplayer.setGf_record(recordxml);
                        }else if(type.equals("b")){
                            record.getOwnerDocument().renameNode(record,record.getNamespaceURI(),"dm");
                            String recordxml = XmlUtils.getStringFromNode(record);
                            matixxplayer.setDm_record(recordxml);
                        }
                        player.removeChild(record);
                    }else{
                        LOG.warn("/max not found");
                    }

                    //battledata

                    //sessiondata

                    //favoritemusic
                    final String music_list_1 = XmlUtils.strAtPath(player,"/favoritemusic/music_list_1");
                    final String music_list_2 = XmlUtils.strAtPath(player,"/favoritemusic/music_list_2");
                    final String music_list_3 = XmlUtils.strAtPath(player,"/favoritemusic/music_list_3");
                    matixxplayer.setFavoritemusic(music_list_1+","+music_list_2+","+music_list_3);

                    //monthly_skill


                    //event_skill


                    player.removeChild(XmlUtils.nodeAtPath(player,"/monstar_subjugation"));
                    player.removeChild(XmlUtils.nodeAtPath(player,"/bear_fes"));

                    //stage
                    NodeList stageList = XmlUtils.nodesAtPath(player,"/stage");
                    if(stageList!=null){
                        for(int j = 0;j<stageList.getLength();j++){
                            Element stage = (Element) stageList.item(j);
                            //Date time
                            LocalDateTime date = TimeUtils.timeFromEpoch(XmlUtils.longAtChild(stage, "date_ms"));
                            int musicid = XmlUtils.intAtChild(stage,"musicid");
                            matixxMusic music = matixxMusicDao.findByMusicId(musicid);
                            int seq = XmlUtils.intAtChild(stage,"seq");
                            int skill = XmlUtils.intAtChild(stage,"skill");
                            int new_skill = XmlUtils.intAtChild(stage,"new_skill");
                            boolean clear = XmlUtils.boolAtChild(stage,"clear");
                            boolean auto_clear = XmlUtils.boolAtChild(stage,"auto_clear");
                            boolean fullcombo = XmlUtils.boolAtChild(stage,"fullcombo");
                            boolean excellent = XmlUtils.boolAtChild(stage,"excellent");
                            int medal = XmlUtils.intAtChild(stage,"medal");
                            int perc = XmlUtils.intAtChild(stage,"perc");
                            int new_perc = XmlUtils.intAtChild(stage,"new_perc");
                            int rank = XmlUtils.intAtChild(stage,"rank");
                            int score = XmlUtils.intAtChild(stage,"score");
                            int combo = XmlUtils.intAtChild(stage,"combo");
                            int max_combo_perc = XmlUtils.intAtChild(stage,"max_combo_perc");
                            int flags = XmlUtils.intAtChild(stage,"flags");
                            int phrase_combo_perc = XmlUtils.intAtChild(stage,"phrase_combo_perc");
                            int perfect = XmlUtils.intAtChild(stage,"perfect");
                            int great = XmlUtils.intAtChild(stage,"great");
                            int good = XmlUtils.intAtChild(stage,"good");
                            int ok = XmlUtils.intAtChild(stage,"ok");
                            int miss = XmlUtils.intAtChild(stage,"miss");
                            int perfect_perc = XmlUtils.intAtChild(stage,"perfect_perc");
                            int great_perc = XmlUtils.intAtChild(stage,"great_perc");
                            int good_perc = XmlUtils.intAtChild(stage,"good_perc");
                            int ok_perc = XmlUtils.intAtChild(stage,"ok_perc");
                            int miss_perc = XmlUtils.intAtChild(stage,"miss_perc");

                            String meter = XmlUtils.strAtChild(stage,"meter");
                            String meter_prog = XmlUtils.strAtChild(stage,"meter_prog");
                            String before_meter = XmlUtils.strAtChild(stage,"before_meter");
                            String before_meter_prog = XmlUtils.strAtChild(stage,"before_meter_prog");

                            boolean is_new_meter = XmlUtils.boolAtChild(stage,"is_new_meter");
                            int phrase_data_num = XmlUtils.intAtChild(stage,"phrase_data_num");

                            String phrase_addr = XmlUtils.strAtChild(stage,"phrase_addr");
                            String phrase_type = XmlUtils.strAtChild(stage,"phrase_type");
                            String phrase_status = XmlUtils.strAtChild(stage,"phrase_status");
                            int phrase_end_addr = XmlUtils.intAtChild(stage,"phrase_end_addr");
                            matixxStageRecord matixxStageRecord = new matixxStageRecord(card,date,type,music,seq,skill,new_skill,clear,auto_clear,fullcombo,excellent,medal,perc,new_perc,rank,score,combo,max_combo_perc,flags,phrase_combo_perc,perfect,great,good,ok,miss,perfect_perc,great_perc,good_perc,ok_perc,miss_perc,meter,meter_prog,before_meter,before_meter_prog,is_new_meter,phrase_data_num,phrase_addr,phrase_type,phrase_status,phrase_end_addr);

                            matixxStageDao.create(matixxStageRecord);
                        }
                    }else {
                        LOG.error("Stage Record not saving due to /stage not found");
                    }


                    matixxProfileDao.update(matixxplayer);

                    respBuilder = respBuilder.e("player").a("no", String.valueOf(i + 1))
                            .e("skill")
                            .s32("rank", 1).up()
                            .s32("total_nr", 1).up().up()
                            .e("all_skill")
                            .s32("rank", 1).up()
                            .s32("total_nr", 1).up().up()
                            .e("kac2017").e("data")
                            .s32("term", 0).up()
                            .s32("total_score", 0).up()
                            .s32("score", 0).up()
                            .s32("music_type", 0).up()
                            .s32("play_count", 0).up().up().up();
                }

            }
            return this.sendResponse(request, response, respBuilder);
        }else throw new InvalidRequestException();
    }
}
