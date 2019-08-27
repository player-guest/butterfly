package com.buttongames.butterflyserver.http.handlers.matixxImpl;

import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
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

/**
 * Handler for any requests that come to the <code>matixx_shopinfo</code> module.
 * @author
 */
@Component
public class MatixxShopInfoRequestHandler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(MatixxShopInfoRequestHandler.class);

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

        if (requestMethod.equals("regist")) {
            return handleRegistRequest(requestBody, request, response);
        }

        throw new UnsupportedRequestException();
    }

    /**
     * Handles an incoming request for <code>matixx_shopinfo.regist</code>
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleRegistRequest(final Element requestBody, final Request request, final Response response) {
        final String locationid = XmlUtils.strAtPath(requestBody,"/matixx_shopinfo/shop/locationid");

        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("matixx_shopinfo").e("data")
                .u32("cabid",1).up()
                .str("locationid",locationid).up().up()
                .e("temperature")
                .bool("is_send",true).up().up()
                .e("tax").s32("tax_phase",1);
        // TODO: tax_phase read from db
        return this.sendResponse(request, response, respBuilder);
    }
}
