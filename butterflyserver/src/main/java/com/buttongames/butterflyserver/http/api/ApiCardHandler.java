package com.buttongames.butterflyserver.http.api;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.TokenDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.List;

public class ApiCardHandler {

    private final Logger LOG = LogManager.getLogger(ApiUserHandler.class);

    final ButterflyUserDao userDao;

    final TokenDao tokenDao;

    final CardDao cardDao;

    public ApiCardHandler(ButterflyUserDao userDao, TokenDao tokenDao, CardDao cardDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.cardDao = cardDao;
    }

    public Object handleRequest(final String function, final ButterflyUser user, final Request request, final Response response) {
        JSONObject reqBody = JSONUtil.getBody(request.body());

        if(function.equals("get")){
            return handleGetRequest(reqBody, user, request, response);
        }else if(function.equals("bind")){
            return handleBindRequest(reqBody, user, request, response);
        }else if(function.equals("unbind")){
            return handleUnbindRequest(reqBody, user, request, response);
        }
        return null;
    }

    private Object handleGetRequest(final JSONObject reqBody, final ButterflyUser user, final Request request, final Response response){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        List<Card> cardList = cardDao.findByUser(user);
        return JSONUtil.createStr("SUCCESS", gson.toJson(cardList));

    }

    private Object handleBindRequest(final JSONObject reqBody, final ButterflyUser user,  final Request request, final Response response){

        final String nfcId = reqBody.getString("nfcId");
        final String passcode = reqBody.getString("pin");

        if(nfcId!=null){
            Card card = cardDao.findByNfcId(nfcId);
            if(card!=null){
                if(card.getUser()==null){
                    if(card.getPin().equals(passcode)){
                        card.setUser(user);
                        cardDao.update(card);
                        response.status(200);
                        return JSONUtil.successMsg("OK");
                    }else{
                        response.status(403);
                        return JSONUtil.errorMsg("The Passcode of this card is wrong");
                    }
                }else{
                    response.status(403);
                    return JSONUtil.errorMsg("This card had been bind to another account");
                }
            }else{
                response.status(404);
                return JSONUtil.errorMsg("Card not found. You must play one time in game");
            }
        }

        response.status(400);
        return JSONUtil.errorMsg("Missing Parameter");

    }

    private Object handleUnbindRequest(final JSONObject reqBody, final ButterflyUser user,  final Request request, final Response response){
        final long cardId = reqBody.getLong("id");

        Card card = cardDao.findById(cardId);
        if(card!=null){
            if(card.getUser().getId()==user.getId()){
                card.setUser(null);
                cardDao.update(card);
                return JSONUtil.successMsg("Unbinded");
            }
        }

        response.status(400);
        return JSONUtil.errorMsg("Wrong Parameter");

    }


}
