package com.buttongames.butterflyserver.http.handlers.popn24Impl;

import com.buttongames.butterflyserver.Main;
import com.buttongames.butterflyserver.http.exception.InvalidRequestMethodException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import com.google.common.io.ByteStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

@Component
public class Info24Handler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(Info24Handler.class);

    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        switch(requestMethod) {
            case "common": return handlCommonRequest(requestBody,request,response);
            default: throw new InvalidRequestMethodException();
        }
    }

    private Object handlCommonRequest(final Element requestBody, final Request request, final Response response) {
        try {
            final byte[] respBody = ByteStreams.toByteArray(
                    Main.class.getResourceAsStream("/static_responses/m39/info24.common.resp.xml"));

            return this.sendBytesToClient(respBody,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }
}
