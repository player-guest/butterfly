package com.buttongames.butterflyserver.http.api.game;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxStageDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerProfile;
import com.buttongames.butterflymodel.model.gdmatixx.matixxStageRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.List;

public class ApiMatixxHandler {


    private final Logger LOG = LogManager.getLogger(ApiMatixxHandler.class);

    final ButterflyUserDao butterflyUserDao;

    final CardDao cardDao;

    final MatixxProfileDao matixxProfileDao;

    final MatixxStageDao matixxStageDao;

    final MatixxMusicDao matixxMusicDao;

    public ApiMatixxHandler(ButterflyUserDao butterflyUserDao, CardDao cardDao, MatixxProfileDao matixxProfileDao, MatixxStageDao matixxStageDao, MatixxMusicDao matixxMusicDao) {
        this.butterflyUserDao = butterflyUserDao;
        this.cardDao = cardDao;
        this.matixxProfileDao = matixxProfileDao;
        this.matixxStageDao = matixxStageDao;
        this.matixxMusicDao = matixxMusicDao;
    }

    public Object handleRequest(final String function, final ButterflyUser user, final Request request, final Response response) {
        JSONObject reqBody = JSONUtil.getBody(request.body());


        if (function.equals("musiclist")) {
            return handleMusicListRequest(reqBody, request, response);
        }

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

        if (function.equals("get_profile")) {
            return handleGetProfileRequest(reqBody, profile, request, response);
        } else if (function.equals("update_profile")) {
            return handleUpdateProfileRequest(reqBody, profile, request, response);
        } else if (function.equals("play_record_list")) {
            return handlePlayRecordListRequest(reqBody, card, request, response);
        }
        return null;

    }

    private Object handleGetProfileRequest(final JSONObject reqBody, final matixxPlayerProfile profile, final Request request, final Response response){

        final String player_name = profile.getName();
        final String player_title = profile.getTitle();
        final String[] player_skill = profile.getSkilldata().split(",");


        return JSONUtil.create("SUCCESS", new JSONObject().put("player_name", player_name)
                .put("player_title", player_title)
                .put("player_skill", player_skill[0])
        );
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

    private Object handleMusicListRequest(final JSONObject reqBody, final Request request, final Response response){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(matixxMusicDao.findAll());


    }

    private Object handlePlayRecordListRequest(final JSONObject reqBody, final Card card, final Request request, final Response response){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        List<matixxStageRecord> list =  matixxStageDao.findByCard(card);
        return gson.toJson(list);


    }



}
