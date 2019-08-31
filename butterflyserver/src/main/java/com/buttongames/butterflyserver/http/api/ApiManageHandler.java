package com.buttongames.butterflyserver.http.api;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.util.LocalDateTimeAdapter;
import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxStageDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxMusic;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerProfile;
import com.buttongames.butterflymodel.model.gdmatixx.matixxStageRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import spark.Request;
import spark.Response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * API handler for any requests that come to the <code>manage</code> module.
 * @author player-guest
 */
public class ApiManageHandler {

    private final Logger LOG = LogManager.getLogger(ApiManageHandler.class);

    /**
     * The DAO for managing users in the database.
     */
    final private ButterflyUserDao userDao;

    /**
     * The DAO for managing matixx musics in the database.
     */
    final private MatixxMusicDao matixxMusicDao;

    /**
     * The DAO for managing matixx stages in the database.
     */
    final private MatixxStageDao matixxStageDao;

    /**
     * The DAO for managing matixx profiles in the database.
     */
    final private MatixxProfileDao matixxProfileDao;

    /**
     * The DAO for managing cards in the database.
     */
    final private CardDao cardDao;

    public ApiManageHandler(MatixxMusicDao matixxMusicDao, MatixxStageDao matixxStageDao, ButterflyUserDao userDao, MatixxProfileDao matixxProfileDao, CardDao cardDao) {
        this.matixxMusicDao = matixxMusicDao;
        this.matixxStageDao = matixxStageDao;
        this.userDao = userDao;
        this.matixxProfileDao = matixxProfileDao;
        this.cardDao = cardDao;
    }


    /**
     * Handles an incoming request for the <code>manager</code> module.
     * this is the request handle user login info
     * @param function The method of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    public Object handleRequest(final String function, final Request request, final Response response) {

        switch (function) {
            case "set_matixx_musiclist":
                return handleSetMusiclistRequest(request, response);
            case "fix_time":
                return handleFixTimeRequest(request, response);
            case "get_userlist":
                return handleUserListRequest(request, response);
            case "get_cardlist":
                return handleCardListRequest(request, response);
            case "get_matixx_playerprofilelist":
                return handleMatixxProfileListRequest(request, response);
            default:
                return null;
        }
    }

    private Object handleSetMusiclistRequest(final Request request, final Response response) {

        byte[] mdb_mtfile = request.bodyAsBytes();
        Element mdb = XmlUtils.byteArrayToXmlFile(mdb_mtfile);
        NodeList mdb_datas = XmlUtils.nodesAtPath(mdb, "/mdb_data");

        if(mdb_datas!=null) {
            LOG.info("Adding " + mdb_datas.getLength() + " records");

            for (int j = 0; j < mdb_datas.getLength(); j++) {

                Element item = (Element) mdb_datas.item(j);
                int music_id = XmlUtils.intAtChild(item, "music_id");

                boolean isNew = false;
                matixxMusic musicObj = matixxMusicDao.findByMusicId(music_id);
                if (musicObj == null) {
                    isNew = true;
                    musicObj = new matixxMusic();
                    musicObj.setMusicid(music_id);
                }

                    //omnimix
                    musicObj.setList_type(2);

                    String[] l1 = XmlUtils.strAtChild(item, "xg_diff_list").split(" ");

                    String guitar_diff = l1[0] + " " + l1[1] + " " + l1[2] + " " + l1[3] + " " + l1[4];
                    String bass_diff = l1[10] + " " + l1[11] + " " + l1[12] + " " + l1[13] + " " + l1[14];
                    String drum_diff = l1[5] + " " + l1[6] + " " + l1[7] + " " + l1[8] + " " + l1[9];

                    musicObj.setGuitar_diff(guitar_diff);
                    musicObj.setBass_diff(bass_diff);
                    musicObj.setDrum_diff(drum_diff);

                    musicObj.setIs_hot(false);

                    musicObj.setContain_stat(XmlUtils.strAtChild(item, "contain_stat"));

                    musicObj.setFirst_ver(XmlUtils.strAtChild(item, "first_ver"));

                    musicObj.setFirst_classic_ver(XmlUtils.strAtChild(item, "first_classic_ver"));

                    musicObj.setB_long(XmlUtils.boolAtChild(item, "b_long"));

                    musicObj.setB_eemail(XmlUtils.boolAtChild(item, "b_eemall"));

                    musicObj.setBpm(XmlUtils.intAtChild(item, "bpm"));

                    musicObj.setBpm2(XmlUtils.intAtChild(item, "bpm2"));

                    String title_name = XmlUtils.strAtChild(item, "title_name");
                    musicObj.setTitle_name(title_name);

                    musicObj.setTitle_ascii(XmlUtils.strAtChild(item, "title_ascii"));

                    musicObj.setArtist_title_ascii(XmlUtils.strAtChild(item, "artist_title_ascii"));

                    musicObj.setXg_secret(XmlUtils.strAtChild(item, "xg_secret"));

                    musicObj.setXg_b_session(XmlUtils.boolAtChild(item, "xg_b_session"));

                    musicObj.setSpeed(XmlUtils.intAtChild(item, "speed"));

                    musicObj.setChart_list(XmlUtils.strAtChild(item, "chart_list"));

                    musicObj.setOrigin(XmlUtils.intAtChild(item, "origin"));

                    musicObj.setMusic_type(XmlUtils.intAtChild(item, "music_type"));

                    musicObj.setGenre(XmlUtils.intAtChild(item, "genre"));

                    musicObj.setType_category(XmlUtils.intAtChild(item, "type_category"));

                    if (isNew) {
                        matixxMusicDao.create(musicObj);
                        LOG.info("Added " + title_name);
                    } else {
                        matixxMusicDao.update(musicObj);
                        LOG.info("Updated " + title_name);
                    }
            }

            return JSONUtil.successMsg("OK");
        } else {
            return JSONUtil.errorMsg("Wrong Parameter");
        }
    }

    private Object handleFixTimeRequest(final Request request, final Response response) {

        List<matixxStageRecord> stageRecordList = matixxStageDao.findAll();
        for (matixxStageRecord record : stageRecordList) {
            LocalDateTime oldDate = record.getDate();
            Instant instant1 = Instant.ofEpochMilli(oldDate.toEpochSecond(ZoneOffset.UTC));
            LocalDateTime newDate = instant1.atZone(ZoneId.systemDefault()).toLocalDateTime();
            record.setDate(newDate);
            matixxStageDao.update(record);
        }
        return JSONUtil.successMsg("OK");
    }

    private Object handleUserListRequest(final Request request, final Response response) {
        List<ButterflyUser> userList = userDao.findAll();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.toJson(userList);
    }

    private Object handleCardListRequest(final Request request, final Response response) {
        List<Card> cardList = cardDao.findAll();

        List<Map<String, Object>> respList = new LinkedList<>();
        for (Card card : cardList) {
            Map<String,Object> item = new HashMap<>();
            item.put("id", card.getId());
            if(card.getUser()!=null){
                item.put("user_id", card.getUser().getId());
            }else{
                item.put("user_id", -1);
            }
            item.put("type", card.getType());
            item.put("nfcId", card.getNfcId());
            item.put("displayId", card.getDisplayId());
            item.put("pin", card.getPin());
            item.put("last_playtime", card.getLastPlayTime());
            respList.add(item);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.toJson(respList);
    }

    private Object handleMatixxProfileListRequest(final Request request, final Response response) {
        List<matixxPlayerProfile> profileList = matixxProfileDao.findAll();

        List<Map<String, Object>> respList = new LinkedList<>();
        for (matixxPlayerProfile profile : profileList) {
            Map<String,Object> item = new HashMap<>();
            item.put("id", profile.getId());
            item.put("card", profile.getCard().getId());
            item.put("name", profile.getName());
            item.put("title", profile.getTitle());
            item.put("did", profile.getDid());
            item.put("gf_skill", XmlUtils.strAtPath(XmlUtils.stringToXmlFile(profile.getGf_record()),"/max_record/skill"));
            item.put("dm_skill", XmlUtils.strAtPath(XmlUtils.stringToXmlFile(profile.getDm_record()),"/max_record/skill"));
            item.put("last_playtime", profile.getCard().getLastPlayTime());
            respList.add(item);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.toJson(respList);
    }
}
