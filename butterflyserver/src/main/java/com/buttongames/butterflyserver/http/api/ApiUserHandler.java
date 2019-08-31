package com.buttongames.butterflyserver.http.api;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.util.MD5Utils;
import com.buttongames.butterflycore.util.StringUtils;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.TokenDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.time.temporal.ChronoUnit;

/**
 * API handler for any requests that come to the <code>user</code> module.
 * @author player-guest
 */
public class ApiUserHandler {

    private final Logger LOG = LogManager.getLogger(ApiUserHandler.class);

    /**
     * The DAO for managing users in the database.
     */
    final private ButterflyUserDao userDao;

    /**
     * The DAO for managing tokens in the database.
     */
    final private TokenDao tokenDao;

    public ApiUserHandler(ButterflyUserDao userDao, TokenDao tokenDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
    }

    /**
     * Handles an incoming request for the <code>user</code> module.
     * this is the request handle user login info
     * @param function The method of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    public Object handleRequest(final String function, final Request request, final Response response) {
        JSONObject reqBody = JSONUtil.getBody(request.body());

        if(function.equals("auth")){
            return handleAuthRequest(reqBody, request, response);
        }else if(function.equals("register")){
            return handleRegisterRequest(reqBody, request, response);
        }else if(function.equals("logout")){
            return handleLogoutRequest(reqBody,request,response);
        }
        return null;
    }

    /**
     * Handles user login and token generation
     * @param reqBody The JSON body of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleAuthRequest(final JSONObject reqBody, final Request request, final Response response){
        final String email = reqBody.getString("email");
        final String password = reqBody.getString("password");
        ButterflyUser user = userDao.getByEmail(email);
        if(user!=null){
            String passhash = MD5Utils.getHash(user.getSalt(),password);
            if(passhash.equals(user.getPasswordHash())){
                Token token = new Token(user, StringUtils.getRandomHexString(20), TimeUtils.getLocalDateTimeInUTC().plus(30, ChronoUnit.DAYS));
                tokenDao.create(token);
                response.status(200);
                // change cookies to secured after debug
                response.cookie("/","token",token.getToken(),2628000,false,true);
                String jsonString = new JSONObject()
                        .put("userId", user.getId())
                        .put("group",user.getUser_group())
                        .put("token", token.getToken())
                        .put("expire", token.getExpireTime()).toString();
                return jsonString;
            }
        }
        response.status(401);
        String jsonString = new JSONObject()
                .put("status", "failure")
                .put("data", new JSONObject().put("message", "Wrong E-Mail or Password")).toString();
        return jsonString;
    }

    /**
     * Handles user register
     * @param reqBody The JSON body of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleRegisterRequest(final JSONObject reqBody, final Request request, final Response response){
        final String email = reqBody.getString("email");
        final String password = reqBody.getString("password");
        ButterflyUser user = userDao.getByEmail(email);
        if(user!=null){
            response.status(401);
            return JSONUtil.errorMsg("This E-Mail had been registered");
        }else{
            //check password
            final String salt = MD5Utils.genSalt();
            ButterflyUser newUser = new ButterflyUser(email,salt,MD5Utils.getHash(salt, password),TimeUtils.getLocalDateTimeInUTC(),null,3000,"player");
            userDao.create(newUser);

            return JSONUtil.successMsg("Account created");
        }
    }

    /**
     * Handles user logout and token
     * @param reqBody The JSON body of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleLogoutRequest(final JSONObject reqBody, final Request request, final Response response){
        final String cookietoken = request.cookie("token");
        final String authtoken = request.headers("Authorization");
        String token = null;
        if(cookietoken!=null){
            token = cookietoken;
        }else if(authtoken!=null){
            token = authtoken;
        }

        if(token!=null){
            Token tokenObject = tokenDao.findByToken(token);
            tokenDao.delete(tokenObject);
            return JSONUtil.successMsg("Logouted");
        }

        response.status(403);
        return JSONUtil.errorMsg("Token Unavailable");

    }
}
