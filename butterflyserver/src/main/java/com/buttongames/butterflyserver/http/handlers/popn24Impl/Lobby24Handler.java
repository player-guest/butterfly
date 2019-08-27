package com.buttongames.butterflyserver.http.handlers.popn24Impl;

import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflyserver.http.exception.InvalidRequestMethodException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

@Component
public class Lobby24Handler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(Lobby24Handler.class);

    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        switch(requestMethod) {
            case "getList": return handleGetListRequest(requestBody,request,response);
            default: throw new InvalidRequestMethodException();
        }
    }

    private Object handleGetListRequest(final Element requestBody, final Request request, final Response response){
        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("lobby24").e("list");
        return this.sendResponse(request,response,respBuilder);
    }
}
