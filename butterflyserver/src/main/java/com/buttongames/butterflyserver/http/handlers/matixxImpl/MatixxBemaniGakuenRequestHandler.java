package com.buttongames.butterflyserver.http.handlers.matixxImpl;

import com.buttongames.butterflyserver.Main;
import com.buttongames.butterflyserver.http.exception.UnsupportedRequestException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import com.google.common.collect.ImmutableSet;
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
public class MatixxBemaniGakuenRequestHandler extends BaseRequestHandler {

    private static final ImmutableSet<String> SUPPORTED_METHOD = ImmutableSet.of("get_music_info");


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

        if (requestMethod.equals("get_music_info")) {
            return handleGetMusicInfoRequest(request, response);
        }

        throw new UnsupportedRequestException();
    }

    private Object handleGetMusicInfoRequest(Request request, Response response) {
        // TODO: File not here
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/m32/bemani_gakuen.get_music_info.resp.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }

    }
}
