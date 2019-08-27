package com.buttongames.butterflyserver.http.handlers.popn24Impl;

import com.buttongames.butterflyserver.http.exception.InvalidRequestModuleException;
import com.buttongames.butterflyserver.http.handlers.baseImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

@Component
public class Popn24Handler {

    /** Handler for requests for the <code>services</code> module. */
    private final ServicesRequestHandler servicesRequestHandler;

    /** Handler for requests for the <code>pcbevent</code> module. */
    private final PcbEventRequestHandler pcbEventRequestHandler;

    /** Handler for requests for the <code>pcbtracker</code> module. */
    private final PcbTrackerRequestHandler pcbTrackerRequestHandler;

    /** Handler for requests for the <code>message</code> module. */
    private final MessageRequestHandler messageRequestHandler;

    /** Handler for requests for the <code>facility</code> module. */
    private final FacilityRequestHandler facilityRequestHandler;

    /** Handler for requests for the <code>package</code> module. */
    private final PackageRequestHandler packageRequestHandler;

    /** Handler for requests for the <code>eventlog</code> module. */
    private final EventLogRequestHandler eventLogRequestHandler;

    /** Handler for requests for the <code>tax</code> module. */
    private final TaxRequestHandler taxRequestHandler;

    /** Handler for requests for the <code>cardmng</code> module. */
    private final CardManageRequestHandler cardManageRequestHandler;

    /** Handler for requests for the <code>system</code> module. */
    private final SystemRequestHandler systemRequestHandler;

    /** Handler for requests for the <code>eacoin</code> module. */
    private final EacoinRequestHandler eacoinRequestHandler;

    private final Info24Handler info24Handler;

    private final Lobby24Handler lobby24Handler;

    private final Pcb24Handler pcb24Handler;

    private final Player24Handler player24Handler;

    @Autowired
    public Popn24Handler(ServicesRequestHandler servicesRequestHandler,
                         PcbEventRequestHandler pcbEventRequestHandler,
                         PcbTrackerRequestHandler pcbTrackerRequestHandler,
                         MessageRequestHandler messageRequestHandler,
                         FacilityRequestHandler facilityRequestHandler,
                         PackageRequestHandler packageRequestHandler,
                         EventLogRequestHandler eventLogRequestHandler,
                         TaxRequestHandler taxRequestHandler,
                         CardManageRequestHandler cardManageRequestHandler,
                         SystemRequestHandler systemRequestHandler,
                         EacoinRequestHandler eacoinRequestHandler,
                         Info24Handler info24Handler,
                         Lobby24Handler lobby24Handler,
                         Pcb24Handler pcb24Handler,
                         Player24Handler player24Handler) {
        this.servicesRequestHandler = servicesRequestHandler;
        this.pcbEventRequestHandler = pcbEventRequestHandler;
        this.pcbTrackerRequestHandler = pcbTrackerRequestHandler;
        this.messageRequestHandler = messageRequestHandler;
        this.facilityRequestHandler = facilityRequestHandler;
        this.packageRequestHandler = packageRequestHandler;
        this.eventLogRequestHandler = eventLogRequestHandler;
        this.taxRequestHandler = taxRequestHandler;
        this.cardManageRequestHandler = cardManageRequestHandler;
        this.systemRequestHandler = systemRequestHandler;
        this.eacoinRequestHandler = eacoinRequestHandler;
        this.info24Handler = info24Handler;
        this.lobby24Handler = lobby24Handler;
        this.pcb24Handler = pcb24Handler;
        this.player24Handler = player24Handler;
    }

    public Object handleRequest(final Element requestBody, final Request request, final Response response) {
        final String requestModule = request.attribute("module");

        final String type = request.attribute("model").toString().split(":")[2].toLowerCase();
        request.attribute("type",type);

        switch(requestModule) {
            case "services": return this.servicesRequestHandler.handleRequest(requestBody, request, response);
            case "pcbevent": return this.pcbEventRequestHandler.handleRequest(requestBody, request, response);
            case "pcbtracker": return this.pcbTrackerRequestHandler.handleRequest(requestBody, request, response);
            case "message": return this.messageRequestHandler.handleRequest(requestBody, request, response);
            case "facility": return this.facilityRequestHandler.handleRequest(requestBody, request, response);
            case "package": return this.packageRequestHandler.handleRequest(requestBody, request, response);
            case "eventlog": return this.eventLogRequestHandler.handleRequest(requestBody, request, response);
            case "tax": return this.taxRequestHandler.handleRequest(requestBody, request, response);
            case "cardmng": return this.cardManageRequestHandler.handleRequest(requestBody, request, response);
            case "system": return this.systemRequestHandler.handleRequest(requestBody, request, response);
            case "eacoin": return this.eacoinRequestHandler.handleRequest(requestBody, request, response);
            case "info24": return this.info24Handler.handleRequest(requestBody, request, response);
            case "lobby24": return this.lobby24Handler.handleRequest(requestBody, request, response);
            case "pcb24": return this.pcb24Handler.handleRequest(requestBody, request, response);
            case "player24": return this.player24Handler.handleRequest(requestBody, request, response);
            case "dlstatus": return 200;
            default: throw new InvalidRequestModuleException();
        }
    }
}
