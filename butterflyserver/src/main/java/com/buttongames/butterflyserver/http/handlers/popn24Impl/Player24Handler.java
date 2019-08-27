package com.buttongames.butterflyserver.http.handlers.popn24Impl;

import com.buttongames.butterflycore.util.ObjectUtils;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.popn24.*;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.popn24.*;
import com.buttongames.butterflyserver.Main;
import com.buttongames.butterflyserver.http.exception.InvalidRequestException;
import com.buttongames.butterflyserver.http.exception.InvalidRequestMethodException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class Player24Handler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(Player24Handler.class);

    private static final ImmutableSet<String> REFID_LIST;

    static {
        REFID_LIST = ImmutableSet.of("new","read","read_score","write_music","write");
    }

    private final CardDao cardDao;

    private final Popn24AccountDao popn24AccountDao;

    private final Popn24ProfileDao popn24ProfileDao;

    private final Popn24StageRecordDao popn24StageRecordDao;

    private final Popn24ItemDao popn24ItemDao;

    private final Popn24CharaParamDao popn24CharaParamDao;

    private final Popn24MissionDao popn24MissionDao;

    public Player24Handler(CardDao cardDao, Popn24AccountDao popn24AccountDao, Popn24ProfileDao popn24ProfileDao, Popn24StageRecordDao popn24StageRecordDao, Popn24ItemDao popn24ItemDao, Popn24CharaParamDao popn24CharaParamDao, Popn24MissionDao popn24MissionDao) {
        this.cardDao = cardDao;
        this.popn24AccountDao = popn24AccountDao;
        this.popn24ProfileDao = popn24ProfileDao;
        this.popn24StageRecordDao = popn24StageRecordDao;
        this.popn24ItemDao = popn24ItemDao;
        this.popn24CharaParamDao = popn24CharaParamDao;
        this.popn24MissionDao = popn24MissionDao;
    }

    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");
        Card card = null;
        if(REFID_LIST.contains(requestMethod)){
            Card temp_card = cardDao.findByRefId(ObjectUtils.checkNull(XmlUtils.strAtPath(requestBody, "/player24/ref_id"),"0"));
            if(temp_card!=null){
                card = temp_card;
            }else{
                throw new InvalidRequestException();
            }
        }

        switch(requestMethod) {
            case "new": return handleNewRequest(requestBody,card,request,response);
            case "start": return handleStartRequest(requestBody,request,response);
            case "read": return handleReadRequest(requestBody,card,request,response);
            case "read_score": return handleReadScoreRequest(requestBody,card,request,response);
            case "write_music": return handleWriteMusicRequest(requestBody,card,request,response);
            case "write": return handleWriteRequest(requestBody,card,request,response);//not binary xml
            case "logout": return handleLogoutRequest(requestBody,card,request,response);
            default: throw new InvalidRequestMethodException();
        }
    }

    private Object handleNewRequest(final Element requestBody, final Card card, final Request request, final Response response) {
        //create new profile
        final String name = XmlUtils.strAtPath(requestBody, "/player24/name");
        popn24Account account = getNewAccount();
        account.setName(name);
        account.setCard(card);
        popn24AccountDao.create(account);

        popn24Profile profile = getNewProfile();
        profile.setCard(card);
        popn24ProfileDao.create(profile);

        return this.handleReadRequest(requestBody,card,request,response);
    }

    private Object handleStartRequest(final Element requestBody, final Request request, final Response response) {
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/m39/player24.start.resp.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }

    private Object handleReadRequest(final Element requestBody, final Card card, final Request request, final Response response) {

        String path = "/player";

        popn24Account ac = popn24AccountDao.findByCard(card);
        if(ac==null){
            return this.handleNewRequest(requestBody,card,request,response);
        }
        popn24Profile pf= popn24ProfileDao.findByCard(card);
        if(pf==null){
            return this.handleNewRequest(requestBody,card,request,response);
        }

        KXmlBuilder respBuilder = buildReadBody(ac,pf,card);
        final Document document = respBuilder.getDocument();


        return this.sendResponse(request,response,respBuilder);

    }

    private Object handleReadScoreRequest(final Element requestBody, final Card card, final Request request, final Response response) {
        String path = "/player";

        popn24Account ac = popn24AccountDao.findByCard(card);
        popn24Profile pf= popn24ProfileDao.findByCard(card);

        KXmlBuilder respBuilder = buildReadBody(ac,pf,card);
        final Document document = respBuilder.getDocument();

        // Generate Music Score
        List<popn24StageRecord> stageList = popn24StageRecordDao.findByCard(card);
        if(stageList!=null){
            HashMap<Integer, HashMap<Integer, Object[]>> userTopScores = this.sortScoresByTopScore(stageList);
            for (Map.Entry<Integer, HashMap<Integer, Object[]>> entry : userTopScores.entrySet()) {
                for(int l = 0;l<4;l++) {
                    if (entry.getValue().containsKey(l)) {
                        Object[] record = entry.getValue().get(l);
                        popn24StageRecord topRecord = (popn24StageRecord) record[1];
                        Node music = KXmlBuilder.create("music").s16("music_num", entry.getKey()).up()
                                .u8("sheet_num", topRecord.getSheet_num()).up()
                                .s32("score", topRecord.getScore()).up()
                                .u8("clear_type", topRecord.getClear_type()).up()
                                .u8("clear_rank", topRecord.getClear_type()).up()
                                .s16("cnt",(Integer) record[0]).up().up().getElement();
                        XmlUtils.importNodeToPath(document,music,path);
                    }
                }
            }
        }


        return sendResponse(request,response,respBuilder);
    }

    private Object handleWriteMusicRequest(final Element requestBody, final Card card, final Request request, final Response response) {

        Element node = (Element) XmlUtils.nodeAtPath(requestBody,"/player24");
        try{
            popn24StageRecord record = new popn24StageRecord(card,
                    XmlUtils.intAtChild(node, "chara_num"),
                    XmlUtils.intAtChild(node, "mode"),
                    XmlUtils.intAtChild(node, "play_id"),
                    XmlUtils.intAtChild(node, "stage"),
                    XmlUtils.intAtChild(node, "music_num"),
                    XmlUtils.intAtChild(node, "sheet_num"),
                    XmlUtils.intAtChild(node, "clear_type"),
                    XmlUtils.intAtChild(node, "clear_rank"),
                    XmlUtils.intAtChild(node, "score"),
                    XmlUtils.intAtChild(node, "cool"),
                    XmlUtils.intAtChild(node, "great"),
                    XmlUtils.intAtChild(node, "good"),
                    XmlUtils.intAtChild(node, "bad"),
                    XmlUtils.intAtChild(node, "combo"),
                    XmlUtils.intAtChild(node, "highlight"),
                    XmlUtils.intAtChild(node, "gauge"),
                    XmlUtils.intAtChild(node, "gauge_type"),
                    XmlUtils.intAtChild(node, "is_netvs"),
                    XmlUtils.intAtChild(node, "is_win"),
                    XmlUtils.boolAtChild(node, "is_image_store"),
                    XmlUtils.intAtChild(node, "hispeed"),
                    XmlUtils.intAtChild(node, "popkun"),
                    XmlUtils.boolAtChild(node, "hidden"),
                    XmlUtils.intAtChild(node, "hidden_rate"),
                    XmlUtils.boolAtChild(node, "sudden"),
                    XmlUtils.intAtChild(node, "sudden_rate"),
                    XmlUtils.intAtChild(node, "randmir"),
                    XmlUtils.intAtChild(node, "ojama_0"),
                    XmlUtils.intAtChild(node, "ojama_1"),
                    XmlUtils.boolAtChild(node, "forever_0"),
                    XmlUtils.boolAtChild(node, "forever_1"),
                    XmlUtils.boolAtChild(node, "full_setting"),
                    XmlUtils.intAtChild(node, "guide_se"),
                    XmlUtils.intAtChild(node, "judge"),
                    XmlUtils.intAtChild(node, "slow"),
                    XmlUtils.intAtChild(node, "fast"),
                    XmlUtils.intAtChild(node, "netvs_ojama_type"),
                    XmlUtils.intAtChild(node, "netvs_type"),
                    XmlUtils.intAtChild(node, "netvs_rank"),
                    XmlUtils.intAtChild(node, "netvs_ojama_0"),
                    XmlUtils.intAtChild(node, "netvs_ojama_1"),
                    XmlUtils.intAtChild(node, "netvs_ojama_2"),
                    XmlUtils.boolAtChild(node, "is_staff"),
                    XmlUtils.intAtChild(node, "course_id"),
                    XmlUtils.strAtChild(node, "course_name"), TimeUtils.getLocalDateTimeInUTC() //maybe Null
            );

            popn24StageRecordDao.create(record);
        }catch (NullPointerException e){
            e.printStackTrace();
            LOG.warn("NullPointerException");
        }finally {
            KXmlBuilder respBuilder = KXmlBuilder.create("response").e("player24");
            return this.sendResponse(request,response,respBuilder);
        }

    }

    private Object handleWriteRequest(final Element requestBody, final Card card, final Request request, final Response response) {

        // AC
        popn24Account ac = popn24AccountDao.findByCard(card);
        if(ac==null){
            throw new InvalidRequestException();
        }
        try {
            Element acnode = (Element) XmlUtils.nodeAtPath(requestBody, "/player24/account");
            ac.setTutorial(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "tutorial"), ac.getTutorial()));
            ac.setRead_news(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "read_news"), ac.getRead_news()));
            ac.setArea_id(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "area_id"), ac.getArea_id()));
            ac.setUse_navi(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "use_navi"), ac.getUse_navi()));
            ac.setNice(ObjectUtils.checkNull(XmlUtils.strAtChild(acnode, "nice"), ac.getNice()));
            ac.setFavorite_chara(ObjectUtils.checkNull(XmlUtils.strAtChild(acnode, "favorite_chara"),ac.getFavorite_chara()));
            ac.setSpecial_area(ObjectUtils.checkNull(XmlUtils.strAtChild(acnode, "special_area"),ac.getSpecial_area()));
            ac.setChocolate_charalist(ObjectUtils.checkNull(XmlUtils.strAtChild(acnode, "chocolate_charalist"),ac.getChocolate_charalist()));
            ac.setChocolate_pass_cnt(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "chocolate_pass_cnt"),ac.getChocolate_pass_cnt()));
            ac.setChocolate_sp_chara(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "chocolate_sp_chara"),ac.getChocolate_sp_chara()));
            ac.setChocolate_hon_cnt(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "chocolate_hon_cnt"),ac.getChocolate_hon_cnt()));
            ac.setChocolate_giri_cnt(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "chocolate_giri_cnt"),ac.getChocolate_giri_cnt()));
            ac.setChocolate_kokyu_cnt(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "chocolate_kokyu_cnt"),ac.getChocolate_kokyu_cnt()));
            ac.setTeacher_setting(ObjectUtils.checkNull(XmlUtils.strAtChild(acnode, "teacher_setting"),ac.getTeacher_setting()));
            ac.setNavi_evolution_flg(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "navi_evolution_flg"),ac.getNavi_evolution_flg()));
            ac.setRanking_news_last_no(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "ranking_news_last_no"),ac.getRanking_news_last_no()));
            ac.setPower_point(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "power_point"),ac.getPower_point()));
            ac.setPlayer_point(ObjectUtils.checkNull(XmlUtils.intAtChild(acnode, "player_point"),ac.getPlayer_point()));
            ac.setPower_point_list(ObjectUtils.checkNull(XmlUtils.strAtChild(acnode, "power_point_list"),ac.getPower_point_list()));

            popn24AccountDao.update(ac);
        } catch (NullPointerException e){

            e.printStackTrace();
        }

        popn24Profile pf = popn24ProfileDao.findByCard(card);
        if(pf==null){
            throw new InvalidRequestException();
        }

        try {
            String[] info = pf.getInfo().split(",");
            pf.setInfo(
                    ObjectUtils.checkNull(XmlUtils.strAtPath(requestBody,"/player24/info/ep"),info[0]) + "," +
                            ObjectUtils.checkNull(XmlUtils.strAtPath(requestBody,"/player24/info/ap"),info[1])
            );

            Element configNode = (Element) XmlUtils.nodeAtPath(requestBody, "/player24/config");
            String[] config = pf.getConfig().split(",");
            pf.setConfig(
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"mode"),config[0])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"chara"),config[1])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"music"),config[2])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"sheet"),config[3])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"category"),config[4])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"sub_category"),config[5])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"chara_category"),config[6])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"story_id"),config[7])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"ms_banner_disp"),config[8])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"ms_down_info"),config[9])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"ms_side_info"),config[10])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"ms_raise_type"),config[11])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"ms_rnd_type"),config[12])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"banner_sort"),config[13])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"course_id"),config[14])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"course_folder"),config[15])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(configNode,"story_folder"),config[16])
            );

            // TODO:Option not saving
            Element optionNode = (Element) XmlUtils.nodeAtPath(requestBody, "/player24/option");
            String[] option = pf.getOption().split(",");
            pf.setOption(
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"mode"),option[0])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"hispeed"),option[1])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"popkun"),option[2])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"hidden"),option[3])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"hidden_rate"),option[4])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"sudden"),option[5])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"randmir"),option[6])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"gauge_type"),option[7])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"ojama_0"),option[8])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"ojama_1"),option[9])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"forever_0"),option[10])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"forever_1"),option[11])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"full_setting"),option[12])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"guide_se"),option[13])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"judge"),option[14])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"slow"),option[15])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(optionNode,"fast"),option[16])
            );

            Element customizeNode = (Element) XmlUtils.nodeAtPath(requestBody, "/player24/customize");
            String[] customize = pf.getCustomize().split(",");
            pf.setCustomize(
                    ObjectUtils.checkNull(XmlUtils.strAtChild(customizeNode,"effect_left"),customize[0])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(customizeNode,"effect_center"),customize[1])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(customizeNode,"effect_right"),customize[2])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(customizeNode,"hukidashi"),customize[3])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(customizeNode,"comment_1"),customize[4])+","+
                    ObjectUtils.checkNull(XmlUtils.strAtChild(customizeNode,"comment_2"),customize[5])
            );


            popn24ProfileDao.update(pf);
        }catch (NullPointerException e){

            e.printStackTrace();
        }

        // Save Item
        NodeList itemList = XmlUtils.nodesAtPath(requestBody,"/player24/item");
        if (itemList != null) {
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                int type = XmlUtils.intAtChild(item, "type");
                int itemId = XmlUtils.intAtChild(item, "id");
                int param = XmlUtils.intAtChild(item, "param");
                boolean is_new = XmlUtils.boolAtChild(item, "is_new");
                long get_time = XmlUtils.longAtChild(item, "get_time");
                popn24Item popn24ItemObj = popn24ItemDao.findByItemid(card, itemId);
                if (popn24ItemObj == null) {
                    popn24ItemObj = new popn24Item(card, type, itemId, param, is_new, get_time);
                    popn24ItemDao.create(popn24ItemObj);
                } else {
                    popn24ItemObj.setType(type);
                    popn24ItemObj.setParam(param);
                    popn24ItemObj.setIs_new(is_new);
                    popn24ItemObj.setGet_time(get_time);
                    popn24ItemDao.update(popn24ItemObj);
                }
            }
        }


        // Save CharaParam
        NodeList charaList = XmlUtils.nodesAtPath(requestBody,"/player24/chara_param");
        if (charaList != null) {
            for (int i = 0; i < charaList.getLength(); i++) {
                Element item = (Element) charaList.item(i);
                int chara_id = XmlUtils.intAtChild(item, "chara_id");
                int friendship = XmlUtils.intAtChild(item, "friendship");
                popn24CharaParam popn24CharaObj = popn24CharaParamDao.findByCharaId(card, chara_id);
                if (popn24CharaObj == null) {
                    popn24CharaObj = new popn24CharaParam(card, chara_id, friendship);
                    popn24CharaParamDao.create(popn24CharaObj);
                } else {
                    popn24CharaObj.setChara_id(chara_id);
                    popn24CharaObj.setFriendship(friendship);
                    popn24CharaParamDao.update(popn24CharaObj);
                }
            }
        }

        // Save Mission
        NodeList missionList = XmlUtils.nodesAtPath(requestBody, "/player24/mission");
        if (missionList != null) {
            for (int i = 0; i < missionList.getLength(); i++) {
                Element item = (Element) missionList.item(i);
                int mission_id = XmlUtils.intAtChild(item, "mission_id");
                int gauge_point = XmlUtils.intAtChild(item, "gauge_point");
                int mission_comp = XmlUtils.intAtChild(item, "mission_comp");
                popn24Mission popn24MissionObj = popn24MissionDao.findByMissionId(card, mission_id);
                if (popn24MissionObj == null) {
                    popn24MissionObj = new popn24Mission(card, mission_id, gauge_point, mission_comp);
                    popn24MissionDao.create(popn24MissionObj);
                } else {
                    popn24MissionObj.setMission_id(mission_id);
                    popn24MissionObj.setGauge_point(gauge_point);
                    popn24MissionObj.setMission_comp(mission_comp);
                    popn24MissionDao.update(popn24MissionObj);
                }
            }
        }


        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("player24");
        return this.sendResponse(request,response,respBuilder);
    }

    private Object handleLogoutRequest(final Element requestBody, final Card card, final Request request, final Response response) {
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("player24");
        return this.sendResponse(request,response,respBuilder);
    }


    private popn24Account getNewAccount(){
        popn24Account ac = new popn24Account();
        ac.setTutorial(-1);
        ac.setArea_id(51);
        ac.setLumina(0);
        ac.setMedal_set("0 0");
        ac.setRead_news(0);
        ac.setStaff(0);
        ac.setIs_conv(0);
        ac.setItem_type(0);
        ac.setItem_id(0);
        ac.setLicense_data("-1 -1 -1 -1 -1 -1 -1 -1 -1 -1");
        ac.setName("Player");
        // Gen ID
        final int did = new Random().nextInt(99999999);
        ac.setG_pm_id(String.valueOf(did));
        ac.setToday_play_cnt(0);
        ac.setToday_play_cnt(0);
        ac.setConsecutive_days(0);
        ac.setTotal_days(0);
        ac.setInterval_day(0);
        ac.setActive_fr_num(0);
        ac.setMy_best("-1 -1 -1 -1 -1 -1 -1 -1 -1 -1");
        ac.setLatest_music("-1 -1 -1 -1 -1");
        ac.setNice("-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1");
        ac.setFavorite_chara("-1 -1 -1 -1 -1 -1 -1 -1 -1 -1");
        ac.setSpecial_area("-1 -1 -1 -1 -1 -1 -1 -1");
        ac.setChocolate_charalist("-1 -1 -1 -1 -1");
        ac.setChocolate_sp_chara(0);
        ac.setChocolate_pass_cnt(0);
        ac.setChocolate_hon_cnt(0);
        ac.setChocolate_giri_cnt(0);
        ac.setChocolate_kokyu_cnt(0);
        ac.setTeacher_setting("-1 -1 -1 -1 -1 -1 -1 -1 -1 -1");
        ac.setWelcom_pack(false);
        ac.setMeteor_flg(false);
        ac.setUse_navi(0);
        ac.setRanking_node(0);
        ac.setChara_ranking_kind_id(0);
        ac.setNavi_evolution_flg(0);
        ac.setPower_point(0);
        ac.setPlayer_point(0);
        ac.setPower_point_list("0");
        return ac;
    }

    private popn24Profile getNewProfile(){
        popn24Profile pf = new popn24Profile();
        pf.setConfig("0,0,0,0,1,-1,2,0,1,0,0,0,0,0,0,0,0");
        pf.setOption("10,0,0,-1,0,-1,0,0,0,0,0,0,0,0,0,0,0");
        pf.setCustom_cate("0,0,0,0,0,0,0");
        pf.setNetvs("0 0 0 0 0 0,dialog#0,dialog#1,dialog#2,dialog#3,dialog#4,dialog#5,"+
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0,"+
                "0 0 0,0 0 0,0");
        pf.setCustomize("0,0,0,0,0,0");
        pf.setInfo("0,0");
        return pf;
    }

    private Node buildAccount(popn24Account ac){
        Node accountnode = KXmlBuilder.create("account")
                .s16("tutorial",ac.getTutorial()).up()
                .s16("area_id",ac.getArea_id()).up()
                .s16("lumina",ac.getLumina()).up()
                .s16("medal_set",String.valueOf(ac.getMedal_set().split(" ").length) ,ac.getMedal_set()).up()
                .s16("read_news",ac.getRead_news()).up()
                .s8("staff",ac.getStaff()).up()
                .s8("is_conv",ac.getIs_conv()).up()
                .s16("item_type",ac.getItem_type()).up()
                .s16("item_id",ac.getItem_id()).up()
                .s16("license_data", String.valueOf(ac.getLicense_data().split(" ").length) , ac.getLicense_data()).up()
                .str("name", ac.getName()).up()
                .str("g_pm_id", ac.getG_pm_id()).up()
                .s16("total_play_cnt", ac.getTotal_play_cnt()).up()
                .s16("today_play_cnt", ac.getToday_play_cnt()).up()
                .s16("consecutive_days", ac.getConsecutive_days()).up()
                .s16("total_days", ac.getTotal_days()).up()
                .s16("interval_day", ac.getInterval_day()).up()
                .u8("active_fr_num", ac.getActive_fr_num()).up()
                .s16("my_best", String.valueOf(ac.getMy_best().split(" ").length) , ac.getMy_best()).up()
                .s16("latest_music", String.valueOf(ac.getLatest_music().split(" ").length) , ac.getLatest_music()).up()
                .s16("nice", String.valueOf(ac.getNice().split(" ").length) , ac.getNice()).up()
                .s16("favorite_chara", String.valueOf(ac.getFavorite_chara().split(" ").length) , ac.getFavorite_chara()).up()
                .s16("special_area",  String.valueOf(ac.getSpecial_area().split(" ").length) , ac.getSpecial_area()).up()
                .s16("chocolate_charalist", String.valueOf(ac.getChocolate_charalist().split(" ").length) , ac.getChocolate_charalist()).up()
                .s32("chocolate_sp_chara", ac.getChocolate_sp_chara()).up()
                .s32("chocolate_pass_cnt", ac.getChocolate_pass_cnt()).up()
                .s32("chocolate_hon_cnt", ac.getChocolate_hon_cnt()).up()
                .s32("chocolate_giri_cnt", ac.getChocolate_giri_cnt()).up()
                .s32("chocolate_kokyu_cnt", ac.getChocolate_kokyu_cnt()).up()
                .s16("teacher_setting", String.valueOf(ac.getTeacher_setting().split(" ").length) ,ac.getTeacher_setting()).up()
                .bool("welcom_pack", ac.isWelcom_pack()).up()
                .bool("meteor_flg", ac.isMeteor_flg()).up()
                .s16("use_navi",  ac.getUse_navi()).up()
                .s32("ranking_node",  ac.getChara_ranking_kind_id()).up()
                .s32("chara_ranking_kind_id",  ac.getChara_ranking_kind_id()).up()
                .s8("navi_evolution_flg",  ac.getNavi_evolution_flg()).up()
                .s32("ranking_news_last_no",  ac.getRanking_news_last_no()).up()
                .s32("power_point",  ac.getPower_point()).up()
                .s32("player_point",  ac.getPlayer_point()).up()
                .s32("power_point_list", String.valueOf(ac.getPower_point_list().split(" ").length) ,  ac.getPower_point_list()).up().getElement();
        return accountnode;
    }

    private Node buildConfig(popn24Profile pf){
        String[] config = pf.getConfig().split(",");
        Node confignode = KXmlBuilder.create("config")
                .u8("mode",config[0]).up()
                .s16("chara",config[1]).up()
                .s16("music",config[2]).up()
                .u8("sheet",config[3]).up()
                .s8("category",config[4]).up()
                .s8("sub_category",config[5]).up()
                .s8("chara_category",config[6]).up()
                .s16("story_id",config[7]).up()
                .s8("ms_banner_disp",config[8]).up()
                .s8("ms_down_info",config[9]).up()
                .s8("ms_side_info",config[10]).up()
                .s8("ms_raise_type",config[11]).up()
                .s8("ms_rnd_type",config[12]).up()
                .s8("banner_sort",config[13]).up()
                .s16("course_id",config[14]).up()
                .s8("course_folder",config[15]).up()
                .s8("story_folder",config[16]).up().getElement();
        return confignode;
    }

    private Node buildOption(popn24Profile pf){
        String[] option = pf.getOption().split(",");
        Node optionnode = KXmlBuilder.create("option")
                .s16("hispeed",option[0]).up()
                .u8("popkun",option[1]).up()
                .bool("hidden",option[2]).up()
                .s16("hidden_rate",option[3]).up()
                .bool("sudden",option[4]).up()
                .s16("sudden_rate",option[5]).up()
                .s8("randmir",option[6]).up()
                .s8("gauge_type",option[7]).up()
                .u8("ojama_0",option[8]).up()
                .u8("ojama_1",option[9]).up()
                .bool("forever_0",option[10]).up()
                .bool("forever_1",option[11]).up()
                .bool("full_setting",option[12]).up()
                .s8("guide_se",option[13]).up()
                .u8("judge",option[14]).up()
                .s16("slow",option[15]).up()
                .s16("fast",option[16]).up().getElement();
        return optionnode;
    }

    private Node buildCustomCate(popn24Profile pf){
        String[] cc = pf.getCustom_cate().split(",");
        Node custom_catenode = KXmlBuilder.create("custom_cate")
                .s8("valid",cc[0]).up()
                .s8("lv_min",cc[1]).up()
                .s8("lv_max",cc[2]).up()
                .s8("medal_min",cc[3]).up()
                .s8("medal_max",cc[4]).up()
                .s8("friend_no",cc[5]).up()
                .s8("score_flg",cc[6]).up().getElement();

        return custom_catenode;
    }

    private Node buildInfo(popn24Profile pf){
        String[] info = pf.getInfo().split(",");
        Node infonode = KXmlBuilder.create("info")
                .u16("ep",info[0]).up()
                .u16("ap",info[1]).up().getElement();
        return infonode;
    }

    private Node buildCustomize(popn24Profile pf){
        String[] customize = pf.getCustomize().split(",");
        Node customizenode = KXmlBuilder.create("customize")
                .u16("effect_left",customize[0]).up()
                .u16("effect_center",customize[1]).up()
                .u16("effect_right",customize[2]).up()
                .u16("hukidashi",customize[3]).up()
                .u16("comment_1",customize[4]).up()
                .u16("comment_2",customize[5]).up().getElement();
        return customizenode;
    }

    private Node buildNetvs(popn24Profile pf){
        String[] netvs = pf.getNetvs().split(",");
        Node netvsnode = KXmlBuilder.create("netvs")
                .s16("record","6",netvs[0]).up()
                .str("dialog",netvs[1]).up()
                .str("dialog",netvs[2]).up()
                .str("dialog",netvs[3]).up()
                .str("dialog",netvs[4]).up()
                .str("dialog",netvs[5]).up()
                .str("dialog",netvs[6]).up()
                .s8("ojama_condition","74",netvs[7]).up()
                .s8("set_ojama","3",netvs[8]).up()
                .s8("set_recommend","3",netvs[9]).up()
                .u32("netvs_play_cnt",netvs[10]).up().getElement();
        return netvsnode;
    }

    private KXmlBuilder buildReadBody(popn24Account ac, popn24Profile pf,Card card){
        String path = "/player";
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("player")
                .s8("result", 0).up()
                .e("event").s16("enemy_medal", 0).up()
                .s16("hp", 0).up().up()
                .e("stamp").s16("stamp_id", 0).up()
                .s16("cnt", 0);

        final Document document = respBuilder.getDocument();

        XmlUtils.importNodeToPath(document,buildAccount(ac),path);
        XmlUtils.importNodeToPath(document,buildConfig(pf),path);
        XmlUtils.importNodeToPath(document,buildOption(pf),path);
        XmlUtils.importNodeToPath(document,buildCustomCate(pf),path);
        XmlUtils.importNodeToPath(document,buildInfo(pf),path);
        XmlUtils.importNodeToPath(document,buildCustomize(pf),path);
        XmlUtils.importNodeToPath(document,buildNetvs(pf),path);

        List<popn24Item> itemList = popn24ItemDao.findByCard(card);
        for(popn24Item item : itemList){
            Node itemBuilder = KXmlBuilder.create("item")
                    .u8("type", item.getType()).up()
                    .u16("id", item.getItemId()).up()
                    .u16("param", item.getParam()).up()
                    .bool("is_new", item.isIs_new()).up()
                    .u64("get_time", item.getGet_time()).up().up().getElement();
            XmlUtils.importNodeToPath(document,itemBuilder,path);
        }

        List<popn24CharaParam> charaParamsList = popn24CharaParamDao.findByCard(card);
        for(popn24CharaParam item : charaParamsList){
            Node charaBuilder = KXmlBuilder.create("chara_param")
                    .u16("chara_id", item.getChara_id()).up()
                    .u16("friendship", item.getFriendship()).up().up().getElement();
            XmlUtils.importNodeToPath(document,charaBuilder,path);
        }

        List<popn24Mission> missionList = popn24MissionDao.findByCard(card);
        for (popn24Mission item : missionList) {
            Node missionBuilder = KXmlBuilder.create("mission")
                    .u32("mission_id", item.getMission_id()).up()
                    .u32("gauge_point", item.getGauge_point()).up()
                    .u32("mission_comp", item.getMission_comp()).up().up().getElement();
            XmlUtils.importNodeToPath(document, missionBuilder, path);
        }

        String eaappli = "<eaappli>\n" +
                "<relation __type=\"s8\">1</relation>\n" +
                "</eaappli>";
        XmlUtils.importStringToPath(document,eaappli,path);

        String navi_data = "<navi_data>\n" +
                "<raisePoint __type=\"s32\" __count=\"5\">-1 -1 -1 -1 -1</raisePoint>\n" +
                "<navi_param>\n" +
                "<navi_id __type=\"u16\">0</navi_id>\n" +
                "<friendship __type=\"s32\">0</friendship>\n" +
                "</navi_param>\n" +
                "</navi_data>";
        XmlUtils.importStringToPath(document,navi_data,path);

        String area = "<area>\n" +
                "<area_id __type=\"u32\">0</area_id>\n" +
                "<chapter_index __type=\"u8\">0</chapter_index>\n" +
                "<gauge_point __type=\"u16\">0</gauge_point>\n" +
                "<is_cleared __type=\"bool\">0</is_cleared>\n" +
                "<diary __type=\"u32\">0</diary>\n" +
                "</area>";
        XmlUtils.importStringToPath(document,area,path);

        return respBuilder;
    }

    /**
     * Sorts scores into a hierarchy of top score by song/difficulty. Used for the various score requests.
     * @param records The records to sort
     * @return The sorted results
     */
    private HashMap<Integer, HashMap<Integer, Object[]>> sortScoresByTopScore(final List<popn24StageRecord> records) {
        // sort them by song and difficulty, then insert them into the response
        // key for top map is the song ID
        // key for the 2nd map is the difficulty
        // the array: a[0] = count, a[1] = top UserSongRecord
        final HashMap<Integer, HashMap<Integer, Object[]>> topScores = new HashMap<>();

        for (popn24StageRecord record : records) {
            if (!topScores.containsKey(record.getMusic_num())) {
                topScores.put(record.getMusic_num(), new HashMap<>());
                topScores.get(record.getMusic_num()).put(record.getSheet_num(), new Object[] { 1, record });
            } else {
                if (!topScores.get(record.getMusic_num()).containsKey(record.getSheet_num())) {
                    topScores.get(record.getMusic_num()).put(record.getSheet_num(), new Object[] { 1, record });
                } else {
                    Object[] currRecord = topScores.get(record.getMusic_num()).get(record.getSheet_num());
                    currRecord[0] = ((Integer) currRecord[0]) + 1;

                    if (((popn24StageRecord) currRecord[1]).getScore() < record.getScore()) {
                        currRecord[1] = record;
                    }

                    topScores.get(record.getMusic_num()).put(record.getSheet_num(), currRecord);
                }
            }
        }

        return topScores;
    }
}
