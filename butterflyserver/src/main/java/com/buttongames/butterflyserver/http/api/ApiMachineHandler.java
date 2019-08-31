package com.buttongames.butterflyserver.http.api;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.util.StringUtils;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.MachineDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Machine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * API handler for any requests that come to the <code>machine</code> module.
 * @author player-guest
 */
public class ApiMachineHandler {

    private final Logger LOG = LogManager.getLogger(ApiMachineHandler.class);

    /**
     * The DAO for managing machines in the database.
     */
    final private MachineDao machineDao;

    public ApiMachineHandler(MachineDao machineDao) {
        this.machineDao = machineDao;
    }

    /**
     * Handles an incoming request for the <code>machine</code> module.
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
        }else if(function.equals("generate")){
            return handleGenerateRequest(user, request, response);
        }else if(function.equals("delete")){
            return handleDeleteRequest(reqBody, user, request, response);
        }
        return null;
    }

    /**
     * Handles get user machines list request
     * @param user The ButterflyUser of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleGetRequest(final ButterflyUser user, final Request request, final Response response){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        List<Machine> machineList = machineDao.findByUser(user);
        return JSONUtil.createStr("SUCCESS", gson.toJson(machineList));

    }

    /**
     * Handles user generating new machine
     * @param user The ButterflyUser of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleGenerateRequest(final ButterflyUser user,  final Request request, final Response response){
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

    /**
     * Handles user generating delete machine
     * @param reqBody The JSON body of the incoming request.
     * @param user The ButterflyUser of incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
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
