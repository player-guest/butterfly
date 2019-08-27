package com.buttongames.butterflycore.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {

    public static JSONObject getBody(final String body){
        if(body.length()==0){
            return new JSONObject();
        }else if(body.substring(0, 1).equals("{")) {
            return new JSONObject(body);
        }else if(body.substring(0, 1).equals("[")){
            return new JSONObject().put("data",new JSONArray(body) );
        }
        return new JSONObject();
    }

//    public static JSONObject create(final String status, final JSONObject json){
//        JSONObject jsonObject = new JSONObject().put("status", status)
//                .put("data", json);
//        return jsonObject;
//    }
    public static JSONObject create(final String status, final JSONObject json){
        JSONObject jsonObject = json;
        return jsonObject;
    }

    public static String createStr(final String status, final JSONObject json){
        String str = new JSONObject().put("status", status)
                .put("data", json).toString();
        return str;
    }

    public static String createStr(final String status, final JSONArray json){
        String str = new JSONObject().put("status", status)
                .put("data", json).toString();
        return str;
    }

//    public static String createStr(final String status, final String jsonStr){
//        String str = null;
//        if(jsonStr.substring(0, 1).equals("{")) {
//            str = new JSONObject().put("status", status)
//                    .put("data", new JSONObject(jsonStr)).toString();
//        }else if(jsonStr.substring(0, 1).equals("[")){
//            str = new JSONObject().put("status", status)
//                    .put("data", new JSONArray(jsonStr)).toString();
//        }
//        return str;
//    }

    public static String createStr(final String status, final String jsonStr){
        String str = null;
        if(jsonStr.substring(0, 1).equals("{")) {
            str = new JSONObject(jsonStr).toString();
        }else if(jsonStr.substring(0, 1).equals("[")){
            str = new JSONArray(jsonStr).toString();
        }
        return str;
    }


    public static String errorMsg(final String message){
        String str = new JSONObject().put("status", "ERROR")
                .put("message", message).toString();
        return str;
    }
    public static String successMsg(final String message){
        String str = new JSONObject().put("status", "SUCCESS")
                .put("message", message).toString();
        return str;
    }
}
