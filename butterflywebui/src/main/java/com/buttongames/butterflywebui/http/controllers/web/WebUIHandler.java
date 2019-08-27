package com.buttongames.butterflywebui.http.controllers.web;

import com.buttongames.butterflywebui.Main;
import com.google.common.io.ByteStreams;
import com.google.common.net.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;

public class WebUIHandler {
    private final Logger LOG = LogManager.getLogger(WebUIHandler.class);

    public Object sendResponse(final Request request, final Response response){
        final HttpServletResponse rawResponse = response.raw();
        response.type(MediaType.HTML_UTF_8.toString());
        String path = request.uri();
        byte[] bytes;

        try {
            if(path.contains("/public/static")){
                bytes = ByteStreams.toByteArray(Main.class.getResourceAsStream(path));
            }else{
                bytes = ByteStreams.toByteArray(Main.class.getResourceAsStream("/public/index/index.html"));
            }
            rawResponse.getOutputStream().write(bytes);
            rawResponse.getOutputStream().flush();
            rawResponse.getOutputStream().close();

            LOG.info("Response sent: " + path);
            return 200;
        }catch (Exception e){
            LOG.info("Static file not found: " + path);
            response.status(404);
            return 404;
        }
    }

}
