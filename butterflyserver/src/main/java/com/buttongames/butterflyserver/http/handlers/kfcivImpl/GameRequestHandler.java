package com.buttongames.butterflyserver.http.handlers.kfcivImpl;

import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4ParamDao;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4ProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4SkillDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.sdvxiv.sdvx4UserParam;
import com.buttongames.butterflymodel.model.sdvxiv.sdvx4UserProfile;
import com.buttongames.butterflymodel.model.sdvxiv.sdvx4UserSkill;
import com.buttongames.butterflyserver.Main;
import com.buttongames.butterflyserver.http.exception.InvalidRequestException;
import com.buttongames.butterflyserver.http.exception.UnsupportedRequestException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spark.Request;
import spark.Response;

import java.util.Iterator;
import java.util.List;

@Component
@Qualifier("sdvx4")
public class GameRequestHandler extends BaseRequestHandler {

    private static final ImmutableSet<String> SUPPORTED_METHOD;

    static {
        SUPPORTED_METHOD = ImmutableSet.of("sv4_common","sv4_hiscore","sv4_shop","sv4_load",
                "sv4_load_m","sv4_load_r","sv4_frozen","sv4_play_s","sv4_play_e","sv4_lounge",
                "sv4_entry_s","sv4_entry_e","sv4_save","sv4_save_m","sv4_save_e","sv4_buy",
                "sv4_new"
                );
    }

    /**
     * The DAO for managing users in the database.
     */
    private final ButterflyUserDao userDao;

    /**
     * The DAO for managing cards in the database.
     */
    private final CardDao cardDao;

    /**
     * The DAO for managing profile in the database.
     */
    private final Sdvx4ProfileDao sdvx4ProfileDao;

    /**
     * The DAO for managing skills in the database.
     */
    private final Sdvx4SkillDao sdvx4SkillDao;

    /**
     * The DAO for managing params in the database.
     */
    private final Sdvx4ParamDao sdvx4ParamDao;

    /**
     * Static list of events for the server.
     */
    private static NodeList ITEMS;

    static {
        loadItem();
    }


    public GameRequestHandler(final ButterflyUserDao userDao, final CardDao cardDao, final Sdvx4ProfileDao sdvx4ProfileDao,final Sdvx4SkillDao sdvx4SkillDao,final Sdvx4ParamDao sdvx4ParamDao) {
        this.userDao = userDao;
        this.cardDao = cardDao;
        this.sdvx4ProfileDao = sdvx4ProfileDao;
        this.sdvx4SkillDao = sdvx4SkillDao;
        this.sdvx4ParamDao = sdvx4ParamDao;
    }

    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        if (requestMethod.equals("sv4_common")) {
            return handleCommonRequest(request, response);
        }else if (requestMethod.equals("sv4_hiscore")){
            return handleHiscoreRequest(request, response);
        }else if (requestMethod.equals("sv4_shop")){
            return handleShopRequest(request, response);
        }else if (requestMethod.equals("sv4_load")){
            return handleLoadRequest(requestBody,request, response);
        }else if (requestMethod.equals("sv4_load_m")){
            //load music data
            return handleLoadMRequest(request, response);
        }else if (requestMethod.equals("sv4_load_r")){
            return handleLoadRRequest(request, response);
        }else if (requestMethod.equals("sv4_frozen")){
            return handleFrozenRequest(request, response);
        }else if (requestMethod.equals("sv4_play_s")){
            return handlePlaySRequest(request, response);
        }else if (requestMethod.equals("sv4_play_e")){
            return handlePlayERequest(request, response);
        }else if (requestMethod.equals("sv4_lounge")){
            //wait for matching
            return handleLoungeRequest(request, response);
        }else if (requestMethod.equals("sv4_entry_s")){
            //send ip address
            return handleEntrySRequest(request, response);
        }else if (requestMethod.equals("sv4_entry_e")){
            //user send the entry id from sv4_entry_s response
            return handleEntryERequest(request, response);
        }else if (requestMethod.equals("sv4_save")){
            return handleSaveRequest(requestBody,request, response);
        }else if (requestMethod.equals("sv4_save_m")){
            //Save music score
            return handleSaveMRequest(request, response);
        }else if (requestMethod.equals("sv4_save_e")){
            return handleSaveERequest(request, response);
        }else if (requestMethod.equals("sv4_buy")){
            return handleBuyRequest(request, response);
        }else if (requestMethod.equals("sv4_new")){
            return handleNewRequest(request, response);
        }

        throw new UnsupportedRequestException();
    }


    /**
     * Handles an incoming request for the <code>sv4_common</code> method.
     * Event and skill course data
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleCommonRequest(final Request request, final Response response) {
        /* TODO: Handle DB */
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/kfc/sv4_common.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }


    /**
     * Handles an incoming request for the <code>sv4_hiscore</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleHiscoreRequest(final Request request, final Response response) {
        /* TODO: Read from db */
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/kfc/sv4_hiscore.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }

    /**
     * Handles an incoming request for the <code>sv4_shop</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleShopRequest(final Request request, final Response response) {
        /* TODO: Read from db */
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/kfc/sv4_shop.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }

    /**
     * Handles an incoming request for the <code>sv4_load</code> method.
     * Load user profiles
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleLoadRequest(final Element requestBody,final Request request, final Response response) {

        final String refid = XmlUtils.strAtPath(requestBody, "/game/refid");
        return handleUserProfileLoadRequest(refid,request,response);
    }

    /**
     * Handles an incoming request for the <code>sv4_load_m</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleLoadMRequest(final Request request, final Response response) {
        /* TODO: Handle DB */
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/kfc/sv4_load_m.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }
    /**
     * Handles an incoming request for the <code>sv4_load_r</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleLoadRRequest(final Request request, final Response response) {
        /* TODO: Handle DB */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game");
        return this.sendResponse(request, response, respBuilder);

    }

    /**
     * Handles an incoming request for the <code>sv4_frozen</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleFrozenRequest(final Request request, final Response response) {

        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game").e("result").u8("result", 0);
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_play_s</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handlePlaySRequest(final Request request, final Response response) {
        /* TODO: Handle Save */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game").e("result").u32("play_id", 0);
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_play_e</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handlePlayERequest(final Request request, final Response response) {

        /* TODO: Handle Save */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game");
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_lounge</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleLoungeRequest(final Request request, final Response response) {

        /* TODO: Handle Matching */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game").e("result").u32("interval", 5);
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_entry_s</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleEntrySRequest(final Request request, final Response response) {

        /* TODO: Handle Matching */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game").e("result").u32("entry_id", 337792553);
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_entry_e</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleEntryERequest(final Request request, final Response response) {

        /* TODO: Handle matching */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game");
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_save</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleSaveRequest(final Element requestBody,final Request request, final Response response) {

        final Element dataNode = (Element) XmlUtils.nodeAtPath(requestBody, "/game");
        final String refid = XmlUtils.strAtChild(dataNode, "refid");
        final Card card = this.cardDao.findByRefId(refid);

        // if there's no card for this refid, throw an error
//        if (card == null) {
//            throw new InvalidRequestException();
//        }


        sdvx4UserProfile profile = this.sdvx4ProfileDao.findByCard(card);
        // if a profile doesn't exist, throw an error
//        if (profile == null) {
//            throw new InvalidRequestException();
//        }


        // TODO: Prevent null data
        final int skill_level = XmlUtils.intAtChild(dataNode, "skill_level");
        final int skill_base_id = XmlUtils.intAtChild(dataNode, "skill_base_id");
        final int skill_name_id = XmlUtils.intAtChild(dataNode, "skill_name_id");
        final int earned_gamecoin_packet = XmlUtils.intAtChild(dataNode, "earned_gamecoin_packet");
        final int earned_gamecoin_block = XmlUtils.intAtChild(dataNode, "earned_gamecoin_block");
        final int earned_blaster_energy = XmlUtils.intAtChild(dataNode, "earned_blaster_energy");
        final int used_packet_booster = XmlUtils.intAtPath(dataNode, "/ea_shop/used_packet_booster");
        final int used_block_booster = XmlUtils.intAtPath(dataNode, "/ea_shop/used_block_booster");
        final int hispeed = XmlUtils.intAtChild(dataNode, "hispeed");
        final int lanespeed = XmlUtils.intAtChild(dataNode, "lanespeed");
        final int gauge_option = XmlUtils.intAtChild(dataNode, "gauge_option");
        final int ars_option = XmlUtils.intAtChild(dataNode, "ars_option");
        final int notes_option = XmlUtils.intAtChild(dataNode, "notes_option");
        final int early_late_disp = XmlUtils.intAtChild(dataNode, "early_late_disp");
        final int draw_adjust = XmlUtils.intAtChild(dataNode, "draw_adjust");
        final int eff_c_left = XmlUtils.intAtChild(dataNode, "eff_c_left");
        final int eff_c_right = XmlUtils.intAtChild(dataNode, "eff_c_right");
        final int music_id = XmlUtils.intAtChild(dataNode, "music_id");
        final int music_type = XmlUtils.intAtChild(dataNode, "music_type");
        final int sort_type = XmlUtils.intAtChild(dataNode, "sort_type");
        final int narrow_down = XmlUtils.intAtChild(dataNode, "narrow_down");
        final int headphone = XmlUtils.intAtChild(dataNode, "headphone");

        profile.setSkill_level(skill_level);
        profile.setSkill_base_id(skill_base_id);
        profile.setSkill_name_id(skill_name_id);
        profile.setGamecoin_packet(profile.getGamecoin_packet()+earned_gamecoin_packet);
        profile.setGamecoin_block(profile.getGamecoin_block()+earned_gamecoin_block);
        profile.setBlaster_energy(profile.getBlaster_energy()+earned_blaster_energy);
        profile.setHispeed(hispeed);
        profile.setLanespeed(lanespeed);
        profile.setGauge_option(gauge_option);
        profile.setArs_option(ars_option);
        profile.setNotes_option(notes_option);
        profile.setEarly_late_disp(early_late_disp);
        profile.setDraw_adjust(draw_adjust);
        profile.setEff_c_left(eff_c_left);
        profile.setEff_c_right(eff_c_right);
        profile.setLast_music_id(music_id);
        profile.setLast_music_type(music_type);
        profile.setSort_type(sort_type);
        profile.setNarrow_down(narrow_down);
        profile.setHeadphone(headphone);

        profile.setPlay_count(profile.getPlay_count()+1);

        //Update the profile
        this.sdvx4ProfileDao.update(profile);


        //Param
        List<sdvx4UserParam> userParam = this.sdvx4ParamDao.findByCard(card);

        final NodeList paramList = XmlUtils.nodesAtPath(dataNode, "/param/info");
        for(int i=0;i < paramList.getLength();i++){
            Element itemNode = (Element) paramList.item(i);
            int type = XmlUtils.intAtChild(itemNode,"type");
            int id = XmlUtils.intAtChild(itemNode,"id");
            String param = XmlUtils.strAtChild(itemNode,"param");
            int count = param.split(" ").length;

            Iterator it = userParam.iterator();
            boolean isNew = true;
            while(it.hasNext()){
                sdvx4UserParam temp = (sdvx4UserParam) it.next();
                if(temp.getId() == id){
                    temp.setType(type);
                    temp.setParam(param);
                    temp.setCount(count);
                    this.sdvx4ParamDao.update(temp);
                    isNew = false;
                }
            }
            if(isNew){
                sdvx4UserParam newParam = new sdvx4UserParam(card,type,id,param,count);
                this.sdvx4ParamDao.create(newParam);
            }
        }

        //Item


        final NodeList newItemList = XmlUtils.nodesAtPath(dataNode, "/param/item");
        for(int i=0;i < newItemList.getLength();i++){
            Element itemNode = (Element) newItemList.item(i);
            int id = XmlUtils.intAtChild(itemNode,"id");
            int type = XmlUtils.intAtChild(itemNode,"type");
            int param = XmlUtils.intAtChild(itemNode,"param");
        }

        //Skill



        this.sdvx4SkillDao.findByCard(card);

        /* TODO: Write to DB */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game");
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_save_m</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleSaveMRequest(final Request request, final Response response) {

        /* TODO: Write to DB */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game");
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_save_e</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleSaveERequest(final Request request, final Response response) {

        /* TODO: Write to DB */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game");
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_buy</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleBuyRequest(final Request request, final Response response) {

        /* TODO: Write actual code */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game").e("result").u32("gamecoin_packet", 26).up()
                .u32("gamecoin_block",13205).up()
                .u8("result",0);
        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Handles an incoming request for the <code>sv4_new</code> method.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleNewRequest(final Request request, final Response response) {

        /* TODO: Save User to DB */
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game");
        return this.sendResponse(request, response, respBuilder);
    }


    /**
     * Handles a request for the user scores.
     * @param refId The refId for the calling card
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleUserProfileLoadRequest(final String refId, final Request request, final Response response) {
        final boolean enableItem = true;

        final Card card = this.cardDao.findByRefId(refId);

        // if there's no card for this refid, throw an error
        if (card == null) {
            throw new InvalidRequestException();
        }

        sdvx4UserProfile profile = this.sdvx4ProfileDao.findByCard(card);

        if (profile == null){
            throw new InvalidRequestException();
        }

        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("game")
                .u8("result",0).up()
                .str("name",profile.getName()).up()
                .str("code",profile.getCode()).up()
                .str("sdvx_id",profile.getSdvxId()).up()
                .u16("appeal_id",profile.getAppeal_id()).up()
                .s16("skill_level",profile.getSkill_level()).up()
                .s16("skill_base_id",profile.getSkill_base_id()).up()
                .s16("skill_name_id",profile.getSkill_name_id()).up()
                .u32("gamecoin_packet",profile.getGamecoin_packet()).up()
                .u32("gamecoin_block",profile.getGamecoin_block()).up()
                .u32("blaster_energy",profile.getBlaster_energy()).up()
                .u32("blaster_count",profile.getBlaster_count()).up()
                .u32("play_count",profile.getPlay_count()).up()
                .u32("day_count",profile.getDay_count()).up()
                .u32("today_count",profile.getToday_count()).up()
                .u32("max_play_chain",profile.getMax_play_chain()).up()
                .u32("week_count",profile.getWeek_chain()).up()
                .u32("week_play_count",profile.getWeek_play_count()).up()
                .u32("week_chain",profile.getWeek_chain()).up()
                .u32("max_week_chain",profile.getMax_week_chain()).up()
                .u32("creator_id",profile.getCreator_id()).up()
                .s32("hispeed",profile.getHispeed()).up()
                .u32("lanespeed",profile.getLanespeed()).up()
                .u8("gauge_option",profile.getGauge_option()).up()
                .u8("ars_option",profile.getArs_option()).up()
                .u8("notes_option",profile.getNotes_option()).up()
                .u8("early_late_disp",profile.getEarly_late_disp()).up()
                .s32("draw_adjust",profile.getDraw_adjust()).up()
                .u8("eff_c_left",profile.getEff_c_left()).up()
                .u8("eff_c_right",profile.getEff_c_right()).up()
                .s32("last_music_id",profile.getLast_music_id()).up()
                .u8("last_music_type",profile.getLast_music_type()).up()
                .u8("sort_type",profile.getSort_type()).up()
                .u8("narrow_down",profile.getNarrow_down()).up()
                .u8("headphone",profile.getHeadphone()).up();


        //skill
        List<sdvx4UserSkill> skillList = this.sdvx4SkillDao.findByUser(card.getUser());
        respBuilder.e("skill");
        for(sdvx4UserSkill item:skillList){
            respBuilder.e("course")
                    .s16("ssnid",item.getSsnid()).up()
                    .s16("crsid",item.getCrsid()).up()
                    .s32("sc",item.getSc()).up()
                    .s16("ct",item.getCt()).up()
                    .s16("gr",item.getGr()).up()
                    .s16("ar",item.getAr()).up()
                    .s16("cnt",item.getCnt()).up().up();
        }
        respBuilder.up();


        //param
        // TODO: Read from param table
        List<sdvx4UserParam> paramList = this.sdvx4ParamDao.findByUser(card.getUser());
        respBuilder.e("param");
        for(sdvx4UserParam item:paramList){
            respBuilder.e("info")
                    .s32("type",item.getType()).up()
                    .s32("id",item.getParamId()).up()
                    .e("param").attr("__type","s32").attr("__count",String.valueOf(item.getCount())).data(item.getParam()).up().up();
        }
        respBuilder.up();

        if(enableItem){
            //Enable all item( Not all item now)
            // TODO: Read Item from database
            final Document document = respBuilder.getDocument();
            final Element elem = respBuilder.getElement();
            for (int i = 0; i < ITEMS.getLength(); i++) {
                Node tmp = document.importNode(ITEMS.item(i), true);
                elem.appendChild(tmp);
            }
            respBuilder.up();
        }else{
        }

        return this.sendResponse(request, response, respBuilder);
    }

    /**
     * Load the events data into memory.
     */
    private static void loadItem() {
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/kfc/item.xml"));
            final Element doc = XmlUtils.byteArrayToXmlFile(respBody);
            ITEMS = XmlUtils.nodesAtPath(doc, "/response/game/item");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
