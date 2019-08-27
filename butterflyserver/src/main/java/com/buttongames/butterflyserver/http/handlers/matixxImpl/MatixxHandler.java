package com.buttongames.butterflyserver.http.handlers.matixxImpl;

import com.buttongames.butterflyserver.http.exception.InvalidRequestModuleException;
import com.buttongames.butterflyserver.http.handlers.baseImpl.*;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import spark.Request;
import spark.Response;

@Component
public class MatixxHandler {

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

    private final MatixxShopInfoRequestHandler matixxShopInfoRequestHandler;

    private final MatixxGameInfoRequestHandler matixxGameInfoRequestHandler;

    private final MatixxCardUtilRequestHandler matixxCardUtilRequestHandler;

    private final MatixxGameTopRequestHandler matixxGameTopRequestHandler;

    private final MatixxGameEndRequestHandler matixxGameEndRequestHandler;

    private final MatixxPlayableMusicRequestHandler matixxPlayableMusicRequestHandler;

    private final MatixxBemaniGakuenRequestHandler matixxBemaniGakuenRequestHandler;

    private static final ImmutableSet<String> MATIXX_SUPPORTED_MODULES = ImmutableSet.of("matixx_shopinfo",
            "matixx_gameinfo", "matixx_cardutil", "bemani_gakuen", "matixx_gameend", "matixx_playablemusic", "matixx_gametop");


    @Autowired
    public MatixxHandler(final ServicesRequestHandler servicesRequestHandler,
                      final PcbEventRequestHandler pcbEventRequestHandler,
                      final PcbTrackerRequestHandler pcbTrackerRequestHandler,
                      final MessageRequestHandler messageRequestHandler,
                      final FacilityRequestHandler facilityRequestHandler,
                      final PackageRequestHandler packageRequestHandler,
                      final EventLogRequestHandler eventLogRequestHandler,
                      final TaxRequestHandler taxRequestHandler,
                      final CardManageRequestHandler cardManageRequestHandler,
                      final SystemRequestHandler systemRequestHandler,
                      final EacoinRequestHandler eacoinRequestHandler,
                      final MatixxShopInfoRequestHandler matixxShopInfoRequestHandler,
                      final MatixxGameInfoRequestHandler matixxGameInfoRequestHandler,
                      final MatixxCardUtilRequestHandler matixxCardUtilRequestHandler,
                      final MatixxGameTopRequestHandler matixxGameTopRequestHandler,
                      final MatixxGameEndRequestHandler matixxGameEndRequestHandler,
                      final MatixxPlayableMusicRequestHandler matixxPlayableMusicRequestHandler,
                      final MatixxBemaniGakuenRequestHandler matixxBemaniGakuenRequestHandler) {
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
        this.matixxShopInfoRequestHandler = matixxShopInfoRequestHandler;
        this.matixxGameInfoRequestHandler = matixxGameInfoRequestHandler;
        this.matixxCardUtilRequestHandler = matixxCardUtilRequestHandler;
        this.matixxGameTopRequestHandler = matixxGameTopRequestHandler;
        this.matixxGameEndRequestHandler = matixxGameEndRequestHandler;
        this.matixxPlayableMusicRequestHandler = matixxPlayableMusicRequestHandler;
        this.matixxBemaniGakuenRequestHandler = matixxBemaniGakuenRequestHandler;
    }

    public Object handleRequest(final Element requestBody, final Request request, final Response response){
        final String requestModule = request.attribute("module");

        // get gfdm type
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
            case "matixx_shopinfo": return this.matixxShopInfoRequestHandler.handleRequest(requestBody, request, response);
            case "matixx_gameinfo": return this.matixxGameInfoRequestHandler.handleRequest(requestBody, request, response);
            case "matixx_cardutil": return this.matixxCardUtilRequestHandler.handleRequest(requestBody, request, response);
            case "matixx_gametop": return this.matixxGameTopRequestHandler.handleRequest(requestBody, request, response);
            case "matixx_gameend": return this.matixxGameEndRequestHandler.handleRequest(requestBody, request, response);
            case "matixx_playablemusic": return this.matixxPlayableMusicRequestHandler.handleRequest(requestBody, request, response);
            case "bemani_gakuen": return this.matixxBemaniGakuenRequestHandler.handleRequest(requestBody, request, response);
            default: throw new InvalidRequestModuleException();
        }
    }
}
