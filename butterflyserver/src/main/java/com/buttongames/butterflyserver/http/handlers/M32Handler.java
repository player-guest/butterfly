package com.buttongames.butterflyserver.http.handlers;

import com.buttongames.butterflyserver.http.exception.InvalidRequestModelException;
import com.buttongames.butterflyserver.http.exception.InvalidRequestModuleException;
import com.buttongames.butterflyserver.http.handlers.matixxImpl.*;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

@Component
public class M32Handler {

    private static final Logger LOG = LogManager.getLogger(M32Handler.class);

    private final MatixxHandler matixxHandler;

    private static final ImmutableSet<String> MATIXX_SUPPORTED_MODULES = ImmutableSet.of("matixx_shopinfo",
            "matixx_gameinfo", "matixx_cardutil", "bemani_gakuen", "matixx_gameend", "matixx_playablemusic", "matixx_gametop");

    @Autowired
    public M32Handler(final MatixxHandler matixxHandler) {
        this.matixxHandler = matixxHandler;
    }

        public Object handleRequest(final Element requestBody, final Request request, final Response response){
            final int version = new Integer(request.attribute("model").toString().split(":")[4].toLowerCase());

            if(version >= 2018091200){
                // EXTCHAIN
                LOG.warn("Invalid version requested: " + version);
                throw new InvalidRequestModelException();
            }else if(version <= 2018090400 && version >= 2017090600){
                return this.matixxHandler.handleRequest(requestBody,request,response);
            }else {
                throw new InvalidRequestModuleException();
            }
        }
}
