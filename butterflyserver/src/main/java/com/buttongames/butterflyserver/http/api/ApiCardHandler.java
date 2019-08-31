package com.buttongames.butterflyserver.http.api;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
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

/**
 * API handler for any requests that come to the <code>card</code> module.
 * @author player-guest
 */
public class ApiCardHandler {

    private final Logger LOG = LogManager.getLogger(ApiUserHandler.class);

    /**
     * The DAO for managing cards profile in the database.
     */
    final private CardDao cardDao;

    public ApiCardHandler(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    /**
     * Handles an incoming request for the <code>card</code> module.
     * this is the request handle user login info
     * @param function The method of incoming request.
     * @param user The ButterflyUser of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    public Object handleRequest(final String function, final ButterflyUser user, final Request request, final Response response) {
        JSONObject reqBody = JSONUtil.getBody(request.body());

        if(function.equals("get")){
            return handleGetRequest(user, request, response);
        }else if(function.equals("bind")){
            return handleBindRequest(reqBody, user, request, response);
        }else if(function.equals("unbind")){
            return handleUnbindRequest(reqBody, user, request, response);
        }
        return null;
    }

    /**
     * Handles get user card list request
     * @param user The ButterflyUser of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleGetRequest(final ButterflyUser user, final Request request, final Response response){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        List<Card> cardList = cardDao.findByUser(user);
        return JSONUtil.createStr("SUCCESS", gson.toJson(cardList));

    }

    /**
     * Handles user card binding
     * @param reqBody The JSON body of the incoming request.
     * @param user The ButterflyUser of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
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

    /**
     * Handles user card unbind
     * @param reqBody The JSON body of the incoming request.
     * @param user The ButterflyUser of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
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
