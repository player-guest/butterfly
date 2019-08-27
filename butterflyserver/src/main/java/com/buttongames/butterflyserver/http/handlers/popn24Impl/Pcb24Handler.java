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
public class Pcb24Handler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(Pcb24Handler.class);

    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        switch(requestMethod) {
            case "boot": return handleBootRequest(requestBody,request,response);
            case "error": return handleBootRequest(requestBody,request,response);
            default: throw new InvalidRequestMethodException();
        }
    }

    private Object handleBootRequest(final Element requestBody, final Request request, final Response response){

        KXmlBuilder respBuilder = KXmlBuilder.create("response").e("pcb24").attr("status","0");

        return this.sendResponse(request,response,respBuilder);
    }
}
