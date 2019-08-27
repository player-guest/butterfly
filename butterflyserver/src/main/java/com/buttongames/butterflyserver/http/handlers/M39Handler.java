package com.buttongames.butterflyserver.http.handlers;

import com.buttongames.butterflyserver.http.exception.InvalidRequestModelException;
import com.buttongames.butterflyserver.http.exception.InvalidRequestModuleException;
import com.buttongames.butterflyserver.http.handlers.popn24Impl.Popn24Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

@Component
public class M39Handler {
    private static final Logger LOG = LogManager.getLogger(M39Handler.class);

    private final Popn24Handler popn24Handler;

    @Autowired
    public M39Handler(Popn24Handler popn24Handler) {
        this.popn24Handler = popn24Handler;
    }

    public Object handleRequest(final Element requestBody, final Request request, final Response response) {

        final int version = new Integer(request.attribute("model").toString().split(":")[4].toLowerCase());

        if(version >= 2018101700){
            //v25 peace
            return this.popn24Handler.handleRequest(requestBody,request,response);
//            LOG.warn("Invalid version requested: " + version);
//            throw new InvalidRequestModelException();
        }else if(version <= 2018101500 && version >= 2016121400){
            return this.popn24Handler.handleRequest(requestBody,request,response);
        }else {
            throw new InvalidRequestModuleException();
        }
    }
}
