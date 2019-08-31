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
 * Handler for any requests that come to the <code>matixx_gameinfo</code> module.
 * @author player-guest
 */
@Component
public class MatixxGameInfoRequestHandler extends BaseRequestHandler {

    private static final ImmutableSet<String> SUPPORTED_METHOD = ImmutableSet.of("get");

    private final Logger LOG = LogManager.getLogger(MatixxShopInfoRequestHandler.class);

    /**
     * Handles an incoming request for the <code>matixx_gameinfo</code> module.
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

    /**
     * Handles a request to load matixx_gameinfo, containing music and event info.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleGetRequest(Request request, Response response) {
        // TODO: Enable and Disable event
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/m32/matixx_gameinfo.resp.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }

    }
}
