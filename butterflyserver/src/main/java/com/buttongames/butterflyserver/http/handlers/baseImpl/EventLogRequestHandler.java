package com.buttongames.butterflyserver.http.handlers.baseImpl;

import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.GameplayEventLogDao;
import com.buttongames.butterflyserver.http.exception.UnsupportedRequestException;
import com.buttongames.butterflyserver.http.handlers.BaseRequestHandler;
import com.buttongames.butterflymodel.model.ddr16.GameplayEventLog;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflycore.xml.kbinxml.KXmlBuilder;
import com.buttongames.butterflycore.xml.XmlUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Handler for any requests that come to the <code>eventlog</code> module.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Component
public class EventLogRequestHandler extends BaseRequestHandler {

    private final Logger LOG = LogManager.getLogger(EventLogRequestHandler.class);

    /**
     * The DAO for creating gameplay event logs in the database.
     */
    private final GameplayEventLogDao gameplayEventLogDao;

    @Autowired
    public EventLogRequestHandler(final GameplayEventLogDao gameplayEventLogDao) {
        this.gameplayEventLogDao = gameplayEventLogDao;
    }

    /**
     * Handles an incoming request for the <code>eventlog</code> module.
     * @param requestBody The XML document of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    @Override
    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestMethod = request.attribute("method");

        if (requestMethod.equals("write")) {
            return handleWriteRequest(requestBody, request, response);
        }

        throw new UnsupportedRequestException();
    }

    /**
     * Handles an incoming request for <code>eventlog.write</code>
     * @param requestBody The XML document of the incoming request.
     * @param request The Spark request
     * @param response The Spark response
     * @return A response object for Spark
     */
    private Object handleWriteRequest(final Element requestBody, final Request request, final Response response) {
        // save the event to the database
        final String reqModel = request.attribute("model");
        final String reqPcbId = request.attribute("pcbid");
        final int retryCount = XmlUtils.intAtPath(requestBody, "/eventlog/retrycnt");
        final String eventId = XmlUtils.strAtPath(requestBody, "/eventlog/data/eventid");
        final int eventOrder = XmlUtils.intAtPath(requestBody, "/eventlog/data/eventorder");
        final LocalDateTime pcbTime = TimeUtils.timeFromEpoch(XmlUtils.longAtPath(requestBody, "/eventlog/data/pcbtime"));
        final long gameSession = XmlUtils.longAtPath(requestBody, "/eventlog/data/gamesession");
        final long numData1 = XmlUtils.longAtPath(requestBody, "/eventlog/data/numdata1");
        final long numData2 = XmlUtils.longAtPath(requestBody, "/eventlog/data/numdata2");
        final String locationId = XmlUtils.strAtPath(requestBody, "/eventlog/data/locationid");

        String stringData1 = XmlUtils.strAtPath(requestBody, "/eventlog/data/strdata1");
        String stringData2 = XmlUtils.strAtPath(requestBody, "/eventlog/data/strdata2");

        try {
            stringData1 = new String(Base64.getDecoder().decode(stringData1));
        } catch (IllegalArgumentException e) {

        }

        try {
            stringData2 = new String(Base64.getDecoder().decode(stringData2));
        } catch (IllegalArgumentException e) {

        }

        final GameplayEventLog event = new GameplayEventLog(reqPcbId, reqModel, retryCount, eventId,
                eventOrder, pcbTime, gameSession, stringData1, stringData2, numData1, numData2, locationId);
        this.gameplayEventLogDao.create(event);

        // send the response
        KXmlBuilder respBuilder = KXmlBuilder.create("response")
                .e("eventlog")
                    .s64("gamesession", 0).up()
                    .s32("logsendflg", 0).up()
                    .s32("logerrlevel", 0).up()
                    .s32("evtidnosendflg", 0);

        return this.sendResponse(request, response, respBuilder);
    }
}
