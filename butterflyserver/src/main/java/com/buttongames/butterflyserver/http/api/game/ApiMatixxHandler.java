package com.buttongames.butterflyserver.http.api.game;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.util.LocalDateTimeAdapter;
import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxPlayerboardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxStageDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxMusic;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerProfile;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerboard;
import com.buttongames.butterflymodel.model.gdmatixx.matixxStageRecord;
import com.google.common.collect.ImmutableSet;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ApiMatixxHandler {


    private final Logger LOG = LogManager.getLogger(ApiMatixxHandler.class);

    final ButterflyUserDao butterflyUserDao;

    final CardDao cardDao;

    final MatixxProfileDao matixxProfileDao;

    final MatixxStageDao matixxStageDao;

    final MatixxMusicDao matixxMusicDao;

    final MatixxPlayerboardDao matixxPlayerboardDao;


    public ApiMatixxHandler(ButterflyUserDao butterflyUserDao, CardDao cardDao, MatixxProfileDao matixxProfileDao, MatixxStageDao matixxStageDao, MatixxMusicDao matixxMusicDao, MatixxPlayerboardDao matixxPlayerboardDao) {
        this.butterflyUserDao = butterflyUserDao;
        this.cardDao = cardDao;
        this.matixxProfileDao = matixxProfileDao;
        this.matixxStageDao = matixxStageDao;
        this.matixxMusicDao = matixxMusicDao;
        this.matixxPlayerboardDao = matixxPlayerboardDao;
    }

    public Object handleRequest(final String function, final ButterflyUser user, final Request request, final Response response) {
        JSONObject reqBody = JSONUtil.getBody(request.body());


        /** Method after here need a matixx profile*/
        Card card ;
        List<Card> cardList = cardDao.findByUser(user);
        if(cardList==null){
            response.status(400);
            return JSONUtil.errorMsg("You have not bind card to this account");
        }else{
            card = cardList.get(0);
            if(card==null){
                response.status(400);
                return JSONUtil.errorMsg("You have not bind card to this account");
            }
        }


        matixxPlayerProfile profile =  matixxProfileDao.findByCard(card);
        if(profile==null){
            response.status(400);
            return JSONUtil.errorMsg("Can't find a profile by your first card. Multiple card not support now");
        }

        switch (function){
            case "get_profile":
                return handleGetProfileRequest(profile, request, response);
            case "set_profile": return handleUpdateProfileRequest(reqBody, profile, request, response);
            case "get_musiclist": return handleMusicListRequest(request,response);
            case "get_music_detail":
                return handleMusicDetailRequest(reqBody, card, request, response);
            case "get_play_record_list":
                return handlePlayRecordListRequest(card, request, response);
            case "get_play_record_detail": return handlePlayRecordRequest(reqBody,card,request,response);
            case "get_playerboard":
                return handleGetPlayerBoard(card, request, response);
            case "set_playerboard":
                return handleSetPlayerBoard(reqBody, card, request, response);
            case "get_skill": return handlePlayerSkill(card,request,response);
            default: return 404;
        }

    }

    private Object handleGetProfileRequest(final matixxPlayerProfile profile, final Request request, final Response response) {

        final String player_name = profile.getName();
        final String player_title = profile.getTitle();

        final String gf_skill = XmlUtils.strAtPath(XmlUtils.stringToXmlFile(profile.getGf_record()),"/max_record/skill");
        final String dm_skill = XmlUtils.strAtPath(XmlUtils.stringToXmlFile(profile.getDm_record()),"/max_record/skill");
        return new JSONObject().put("player_name", player_name)
                .put("player_title", player_title)
                .put("gf_skill", gf_skill)
                .put("dm_skill",dm_skill);
    }

    private Object handleUpdateProfileRequest(final JSONObject reqBody, final matixxPlayerProfile profile, final Request request, final Response response){
        int count=0;

        final String player_name = reqBody.getString("player_name");
        if(player_name!=null&&!player_name.equals("")){
            profile.setName(player_name);
        }else{count++;}
        final String player_title = reqBody.getString("player_title");
        if(player_title!=null&&!player_title.equals("")){
            profile.setTitle(player_title);
        }else{count++;}
        if(count==2){
            response.status(400);
            return JSONUtil.errorMsg("You input a null value");
        }else{
            matixxProfileDao.update(profile);
            return new JSONObject().put("player_name", profile.getName())
                    .put("player_title", profile.getTitle())
                    .put("player_skill", profile.getSkilldata().split(",")[0]).toString();
        }

    }

    private Object handleMusicListRequest(final Request request, final Response response) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(matixxMusicDao.findAll());
    }

    private Object handleMusicDetailRequest(final JSONObject reqBody, final Card card, final Request request, final Response response){
        final String recordId = reqBody.getString("id");

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        matixxMusic music =  matixxMusicDao.findByMusicId(Integer.parseInt(recordId));
        return gson.toJson(music);

    }

    private Object handlePlayRecordListRequest(final Card card, final Request request, final Response response) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        List<matixxStageRecord> list =  matixxStageDao.findByCard(card);
        return gson.toJson(list);
    }

    private Object handlePlayRecordRequest(final JSONObject reqBody, final Card card, final Request request, final Response response){
        final String recordId = reqBody.getString("id");

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        matixxStageRecord record =  matixxStageDao.findById(Long.parseLong(recordId));
        if(record==null){
            return JSONUtil.errorMsg("Record not found");
        }else if(record.getCard().getId()!=card.getId()){
            return JSONUtil.errorMsg("This record id not belong to you");
        }else {
            return gson.toJson(record);
        }
    }

    private Object handleGetPlayerBoard(final Card card, final Request request, final Response response) {
        List<matixxPlayerboard> list = matixxPlayerboardDao.findByCard(card);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        return gson.toJson(list);
    }

    private Object handleSetPlayerBoard(final JSONObject reqBody, final Card card, final Request request, final Response response) {
        JSONArray array = reqBody.getJSONArray("data");

        List<matixxPlayerboard> oldList = matixxPlayerboardDao.findByCard(card);
        oldList.forEach(matixxPlayerboardDao::delete);

        array.forEach(item -> {
            JSONObject json = (JSONObject) item;
            int stickerId = json.getInt("stickerId");
            float pos_x = json.getFloat("pos_x");
            float pos_y = json.getFloat("pos_y");
            float scale_x = json.getFloat("scale_x");
            float scale_y = json.getFloat("scale_y");
            float rotate = json.getFloat("rotate");

            matixxPlayerboard sticker = new matixxPlayerboard(card, stickerId, pos_x, pos_y, scale_x, scale_y, rotate);
            matixxPlayerboardDao.create(sticker);

        });
        return handleGetPlayerBoard(card, request, response);
    }

    private Object handlePlayerSkill(final Card card, final Request request, final Response response){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setExclusionStrategies(new SkillExclusionStrategy())
                .create();

        matixxPlayerProfile profile = matixxProfileDao.findByCard(card);
        final String gf_skill = XmlUtils.strAtPath(XmlUtils.stringToXmlFile(profile.getGf_record()),"/max_record/skill");
        final String dm_skill = XmlUtils.strAtPath(XmlUtils.stringToXmlFile(profile.getDm_record()),"/max_record/skill");

        List<matixxStageRecord> gfRecord = matixxStageDao.findByCard(card,"a");
        List<matixxStageRecord> dmRecord = matixxStageDao.findByCard(card,"b");


        List<Integer> hotList = matixxMusicDao.findByVersion(24);

        final List<matixxStageRecord> gfHotList = new ArrayList<>();
        final List<matixxStageRecord> gfOtherList = new ArrayList<>();
        sortScoresByTopSkill(gfRecord).forEach((integer, matixxStageRecord) -> {
            if(hotList.contains(integer)){
                gfHotList.add(matixxStageRecord);
            }else {
                gfOtherList.add(matixxStageRecord);
            }
        });
        getSortedArray(gfHotList);
        getSortedArray(gfOtherList);

        final List<matixxStageRecord> dmHotList = new ArrayList<>();
        final List<matixxStageRecord> dmOtherList = new ArrayList<>();
        sortScoresByTopSkill(dmRecord).forEach((integer, matixxStageRecord) -> {
            if(hotList.contains(integer)){
                dmHotList.add(matixxStageRecord);
            }else {
                dmOtherList.add(matixxStageRecord);
            }
        });

        HashMap<String, Object> result = new HashMap<>();
        result.put("gf_skill", gf_skill);
        result.put("dm_skill", dm_skill);
        result.put("gfHotList", gfHotList);
        result.put("gfOtherList", gfOtherList);
        result.put("dmHotList", dmHotList);
        result.put("dmOtherList", dmOtherList);

        return gson.toJson(result);
    }

    private HashMap<Integer, matixxStageRecord> sortScoresByTopSkill(final List<matixxStageRecord> records) {
        final HashMap<Integer, matixxStageRecord> topSkill = new HashMap<>();

        for(matixxStageRecord record : records){
            if(!topSkill.containsKey(record.getMusic().getMusicid())){
                topSkill.put(record.getMusic().getMusicid(), record);
            } else {
                if( topSkill.get(record.getMusic().getMusicid()).getSkill() < record.getSkill() ){
                    topSkill.put(record.getMusic().getMusicid(),record);
                }
            }
        }

        return topSkill;
    }

    private List<matixxStageRecord> getSortedArray(List<matixxStageRecord> list){
        list.sort(Comparator.comparing(matixxStageRecord::getSkill));
        return list;
    }

    private static class SkillExclusionStrategy implements ExclusionStrategy {



        public boolean shouldSkipField(FieldAttributes f) {
            ImmutableSet<String> i = ImmutableSet.of("id","type","music","musicid","guitar_diff","bass_diff","drum_diff","title_name","seq","skill","perc");
            String n = f.getName();
            return !i.contains(n);
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }


}
