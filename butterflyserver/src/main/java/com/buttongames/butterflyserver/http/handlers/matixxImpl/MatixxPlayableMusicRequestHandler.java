package com.buttongames.butterflyserver.http.handlers.matixxImpl;

import com.buttongames.butterflycore.util.ObjectUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflymodel.model.gdmatixx.matixxMusic;
import com.buttongames.butterflyserver.Main;
import com.buttongames.butterflyserver.http.exception.UnsupportedRequestException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import com.google.common.io.ByteStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * Handler for any requests that come to the <code>matixx_shopinfo</code> module.
 * @author
 */
@Component
public class MatixxPlayableMusicRequestHandler extends BaseRequestHandler {
    private final Logger LOG = LogManager.getLogger(MatixxShopInfoRequestHandler.class);

    private MatixxMusicDao matixxMusicDao;


    public MatixxPlayableMusicRequestHandler(MatixxMusicDao matixxMusicDao) {
        this.matixxMusicDao = matixxMusicDao;
    }

    /**
     * Handles an incoming request for the <code>matixx_shopinfo</code> module.
     * @param requestBody The XML document of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        if (requestMethod.equals("get")) {
            return handleGetRequest(request, response);
        }

        throw new UnsupportedRequestException();
    }

    private Object handleGetRequest(Request request, Response response) {

        List<matixxMusic> list = matixxMusicDao.findAll();

        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("matixx_playablemusic")
                .e("hot").s32("major", -1).up()
                .s32("minor",-1).up().up();

        respBuilder = respBuilder.e("musicinfo").attr("nr",new String().valueOf(list.size()));

        for(int i = 0 ;i<list.size();i++){
            matixxMusic item = list.get(i);
            boolean isHot = false;
            if(ObjectUtils.checkNull(item.getFirst_ver().split(" ")[0],"0").equals("24")){
                isHot =true;
            }

            respBuilder = respBuilder.e("music")
                    .s32("id", item.getMusicid()).up()
                    .bool("cont_gf", true).up()
                    .bool("cont_dm",true).up()
                    .bool("is_secret", false).up()
                    .bool("is_hot",isHot).up()
                    .u16("diff", "15", item.getGuitar_diff()+" "+item.getBass_diff()+" "+item.getDrum_diff()).up().up();
        }

        return this.sendResponse(request,response,respBuilder);

    }
}
