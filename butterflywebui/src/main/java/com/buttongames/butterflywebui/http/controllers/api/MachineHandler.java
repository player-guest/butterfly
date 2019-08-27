package com.buttongames.butterflywebui.http.controllers.api;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.util.StringUtils;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.MachineDao;
import com.buttongames.butterflydao.hibernate.dao.impl.TokenDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.Machine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.List;

public class MachineHandler {

    private final Logger LOG = LogManager.getLogger(MachineHandler.class);

    final ButterflyUserDao userDao;

    final TokenDao tokenDao;

    final MachineDao machineDao;

    public MachineHandler(ButterflyUserDao userDao, TokenDao tokenDao, MachineDao machineDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.machineDao = machineDao;
    }

    public Object handleRequest(final String function, final ButterflyUser user, final Request request, final Response response) {
        JSONObject reqBody = JSONUtil.getBody(request.body());

        if(function.equals("get")){
            return handleGetRequest(reqBody, user, request, response);
        }else if(function.equals("generate")){
            return handleGenerateRequest(reqBody, user, request, response);
        }else if(function.equals("delete")){
            return handleDeleteRequest(reqBody, user, request, response);
        }
        return null;
    }

    private Object handleGetRequest(final JSONObject reqBody, final ButterflyUser user, final Request request, final Response response){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        List<Machine> machineList = machineDao.findByUser(user);
        return JSONUtil.createStr("SUCCESS", gson.toJson(machineList));

    }

    private Object handleGenerateRequest(final JSONObject reqBody, final ButterflyUser user,  final Request request, final Response response){
        List<Machine> machineList = machineDao.findByUser(user);
        if(machineList.size()>=3){
            response.status(403);
            return JSONUtil.errorMsg("You exceed the limit of 3");
        }else{
            String pcbId = "";
            do{
                pcbId = "0120100000"+ StringUtils.getRandomHexString(10);
            }while (machineDao.findByPcbId(pcbId)!=null);
            Machine machine = new Machine(user,pcbId, TimeUtils.getLocalDateTimeInUTC(),true,5700);
            machineDao.create(machine);
            return JSONUtil.successMsg("OK");
        }

    }

    private Object handleDeleteRequest(final JSONObject reqBody, final ButterflyUser user,  final Request request, final Response response){
        final long machineId = reqBody.getLong("id");

        Machine machine = machineDao.findById(machineId);
        if(machine!=null){
            if(machine.getUser().getId()==user.getId()){
                machineDao.delete(machine);
                return JSONUtil.successMsg("Deleted");
            }
        }

        response.status(400);
        return JSONUtil.errorMsg("Wrong Parameter");
    }
}
