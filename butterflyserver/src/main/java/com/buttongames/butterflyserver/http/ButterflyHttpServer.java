package com.buttongames.butterflyserver.http;

import com.buttongames.butterflycore.compression.Lz77;
import com.buttongames.butterflycore.encryption.Rc4;
import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.util.ObjectUtils;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflycore.xml.kbinxml.PublicKt;
import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.MachineDao;
import com.buttongames.butterflydao.hibernate.dao.impl.TokenDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Machine;
import com.buttongames.butterflymodel.model.Token;
import com.buttongames.butterflyserver.http.api.ApiCardHandler;
import com.buttongames.butterflyserver.http.api.ApiMachineHandler;
import com.buttongames.butterflyserver.http.api.ApiManageHandler;
import com.buttongames.butterflyserver.http.api.ApiUserHandler;
import com.buttongames.butterflyserver.http.api.game.ApiMatixxHandler;
import com.buttongames.butterflyserver.http.exception.*;
import com.buttongames.butterflyserver.http.handlers.KfcHandler;
import com.buttongames.butterflyserver.http.handlers.M32Handler;
import com.buttongames.butterflyserver.http.handlers.M39Handler;
import com.buttongames.butterflyserver.http.handlers.MdxHandler;
import com.buttongames.butterflyserver.util.PropertyNames;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import spark.Request;
import spark.utils.StringUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.buttongames.butterflycore.util.Constants.*;
import static spark.Spark.*;

/**
 * The main HTTP server. This class is responsible for the top-level handling of incoming
 * requests, then delegates the responsibility to the appropriate handler.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Component
public class ButterflyHttpServer {

    private static final Logger LOG = LogManager.getLogger(ButterflyHttpServer.class);

    /**
     * Static set of all the models this server supports.
     */
    private static final ImmutableSet<String> SUPPORTED_MODELS;

    // Do a static setup of our supported models, modules, etc.
    static {
        SUPPORTED_MODELS = ImmutableSet.of("mdx","kfc","m32","m39");
    }

    /** The port the server listens on */
    @Value(PropertyNames.PORT)
    private String port;

    /** Handler for requests for the <code>mdx</code> model. */
    private final MdxHandler mdxHandler;

    /** DAO for interacting with <code>Machine</code> objects in the database. */
    private final MachineDao machineDao;

    /** DAO for interacting with <code>ButterflyUser</code> objects in the database. */
    private final ButterflyUserDao userDao;

    private final KfcHandler kfcHandler;

    private  final M32Handler m32Handler;

    private final M39Handler m39Handler;

    private final TokenDao tokenDao;

    private final ApiUserHandler apiUserHandler;

    private final ApiCardHandler apiCardHandler;

    private final ApiMachineHandler apiMachineHandler;

    private final ApiMatixxHandler apiMatixxHandler;

    private final ApiManageHandler apiManageHandler;

    /**
     * Constructor.
     */
    @Autowired
    public ButterflyHttpServer(MdxHandler mdxHandler,
                               MachineDao machineDao,
                               ButterflyUserDao userDao,
                               KfcHandler kfcHandler,
                               M32Handler m32Handler,
                               M39Handler m39Handler,
                               TokenDao tokenDao,
                               ApiUserHandler apiUserHandler,
                               ApiCardHandler apiCardHandler,
                               ApiMachineHandler apiMachineHandler,
                               ApiMatixxHandler apiMatixxHandler,
                               ApiManageHandler apiManageHandler) {
        this.mdxHandler = mdxHandler;
        this.machineDao = machineDao;
        this.userDao = userDao;
        this.kfcHandler = kfcHandler;
        this.m32Handler = m32Handler;
        this.m39Handler = m39Handler;
        this.tokenDao = tokenDao;
        this.apiUserHandler = apiUserHandler;
        this.apiCardHandler = apiCardHandler;
        this.apiMachineHandler = apiMachineHandler;
        this.apiMatixxHandler = apiMatixxHandler;
        this.apiManageHandler = apiManageHandler;
    }

    /**
     * Configures the routes on our server and begins listening.
     */
    public void startServer() {
        // configure the server properties
        int maxThreads = 20;
        int minThreads = 2;
        int timeOutMillis = 30000;

        final String hostUrl = System.getProperty("hostUrl");
        String Dport = System.getProperty("port");
        String port = ObjectUtils.checkNull(Dport, this.port);

        LOG.info("BaseUrl : "+hostUrl);

        // once routes are configured, the server automatically begins
        threadPool(maxThreads, minThreads, timeOutMillis);
        port(Integer.parseInt(port));
        this.configureRoutesAndExceptions();
    }

    /**
     * Stops the HTTP server.
     */
    public void stopServer() {
        stop();
    }

    /**
     * Configures the routes on the server, and the exception handlers.
     */
    private void configureRoutesAndExceptions() {

        path("/api",() -> {

            before("/*",(req,resp)-> {
                resp.type("application/json");
                LOG.info("API Call:"+req.requestMethod()+" "+req.uri());
                resp.header("Access-Control-Allow-Origin", "*");
                resp.header("Access-Control-Allow-Methods","GET, POST");
                resp.header("Access-Control-Allow-Headers","Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
                resp.header("Access-Control-Max-Age","86400");
                if(req.requestMethod().equals("OPTIONS")){
                    halt(200, "ok");
                }
            });

            path("/user",()->{
                post("/auth",(req,resp) -> apiUserHandler.handleRequest("auth", req, resp));
                post("/register",(req,resp) -> apiUserHandler.handleRequest("register", req, resp));
                post("/logout",(req,resp) -> apiUserHandler.handleRequest("logout", req, resp));
            });

            before("/admin/*",(req,resp)-> {
                if(!checkAuth(req)){
                    halt(401, JSONUtil.errorMsg("Unauthenticated"));
                }else if(!getUser(req).getUser_group().equals("admin")){
                    halt(401, JSONUtil.errorMsg("No Permission"));
                }
            });

            path("/admin", ()->{
                post("/matixx/musiclist",(req,resp)-> apiManageHandler.handleRequest("set_matixx_musiclist", req, resp));
                post("/fix_time",(req,resp)-> apiManageHandler.handleRequest("fix_time", req, resp));
                get("/userlist",(req,resp)-> apiManageHandler.handleRequest("get_userlist", req, resp));
                get("/cardlist",(req,resp)-> apiManageHandler.handleRequest("get_cardlist", req, resp));
                get("/matixx_playerprofilelist",(req,resp)-> apiManageHandler.handleRequest("get_matixx_playerprofilelist", req, resp));

            });

            before("/card/*",(req,resp)-> {
                if(!checkAuth(req)){
                    halt(401, JSONUtil.errorMsg("Unauthenticated"));
                }
            });

            path("/card",()->{
                get("/list",(req,resp) -> apiCardHandler.handleRequest("get", getUser(req), req,resp));
                post("/bind",(req,resp) -> apiCardHandler.handleRequest("bind", getUser(req), req,resp));
                post("/unbind",(req,resp) -> apiCardHandler.handleRequest("unbind", getUser(req), req,resp));
            });

            before("/machine/*",(req,resp)-> {
                if(!checkAuth(req)){
                    halt(401, JSONUtil.errorMsg("Unauthenticated"));
                }
            });

            path("/machine",()->{
                get("/list",(req,resp) -> apiMachineHandler.handleRequest("get", getUser(req), req,resp));
                post("/generate",(req,resp) -> apiMachineHandler.handleRequest("generate", getUser(req), req,resp));
                post("/delete",(req,resp) -> apiMachineHandler.handleRequest("delete", getUser(req), req,resp));
            });


            path("/game",()->{

                path("/matixx",()->{
                    get("/profile", (req,resp)-> apiMatixxHandler.handleRequest("get_profile", getUser(req),req,resp));
                    post("/profile", (req,resp)-> apiMatixxHandler.handleRequest("set_profile", getUser(req),req,resp));
                    get("/musiclist", (req,resp)-> apiMatixxHandler.handleRequest("get_musiclist", getUser(req),req,resp));
                    post("/music", (req, resp) -> apiMatixxHandler.handleRequest("get_music_detail", getUser(req), req, resp));
                    get("/recordlist", (req,resp)-> apiMatixxHandler.handleRequest("get_play_record_list", getUser(req),req,resp));
                    post("/record", (req,resp)-> apiMatixxHandler.handleRequest("get_play_record_detail", getUser(req),req,resp));
                    get("/playerboard", (req, resp) -> apiMatixxHandler.handleRequest("get_playerboard", getUser(req), req, resp));
                    post("/playerboard", (req, resp) -> apiMatixxHandler.handleRequest("set_playerboard", getUser(req), req, resp));
                    get("/skill", (req, resp) -> apiMatixxHandler.handleRequest("get_skill", getUser(req), req, resp));
                });

            });

            notFound((req, resp) -> {
                resp.type("application/json");
                return JSONUtil.errorMsg("404 Not found");
            });

            internalServerError((req, resp) -> {
                resp.type("application/json");
                return JSONUtil.errorMsg("500 Internal server error");
            });

        });

        // configure our root route; its handler will parse the request and go from there
        post("/ea", ((request, response) -> {
            // send the request to the right module handler
            final Element requestBody = validateAndUnpackRequest(request);
            final String requestModel = request.attribute("model");
            final String modelType = requestModel.split(":")[0].toLowerCase();

            switch (modelType) {
                case "mdx": return this.mdxHandler.handleRequest(requestBody, request, response);
                case "kfc": return this.kfcHandler.handleRequest(requestBody, request, response);
                case "m32": return this.m32Handler.handleRequest(requestBody, request, response);
                case "m39": return this.m39Handler.handleRequest(requestBody, request, response);
                default: throw new InvalidRequestModuleException();
            }
        }));



        // configure the exception handlers
        exception(InvalidRequestMethodException.class, ((exception, request, response) -> {
                    response.status(400);
                    response.body("Invalid request method.");
                    LOG.error(XmlUtils.getStringFromElement(validateAndUnpackRequest(request)));
                }));
        exception(InvalidRequestModelException.class, ((exception, request, response) -> {
                    response.status(400);
                    response.body("Invalid request model.");
                }));
        exception(InvalidRequestModuleException.class, ((exception, request, response) -> {
                    response.status(400);
                    response.body("Invalid request module.");
                    LOG.error(XmlUtils.getStringFromElement(validateAndUnpackRequest(request)));
                }));
        exception(MismatchedRequestUriException.class, (((exception, request, response) -> {
                    response.status(400);
                    response.body("Request URI does not match request body.");
                })));
        exception(InvalidPcbIdException.class, (((exception, request, response) -> {
                    response.status(403);
                    response.body("PCBID is not valid or nonexistent.");
                })));
        exception(UnsupportedRequestException.class, (((exception, request, response) -> {
                    response.status(400);
                    response.body("This request is probably valid, but currently unsupported.");
                    LOG.error(XmlUtils.getStringFromElement(validateAndUnpackRequest(request)));
                    LOG.info(String.format("RECEIVED AN UNSUPPORTED REQUEST: %s.%s",
                            request.attribute("module"), request.attribute("method")));
                })));
        exception(CardCipherException.class, (((exception, request, response) -> {
                    response.status(403);
                    response.body("Issue with converting the requested card ID, likely invalid ID.");
                })));
        exception(NullPointerException.class, (((exception, request, response) -> {
            exception.printStackTrace();
            LOG.error(XmlUtils.getStringFromElement(validateAndUnpackRequest(request)));
            response.status(400);
            response.body("Fail to read incoming request, please report a bug.");
        })));

    }

    /**
     * Validates incoming requests for basic sanity checks, and returns the request
     * as a plaintext XML document.
     * @param request The incoming request.
     * @return An <code>Element</code> representing the root of the request document
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private Element validateAndUnpackRequest(Request request) {
        final String requestUriModel = request.queryParams("model");
        final String requestUriModule = request.queryParams("module");
        final String requestUriMethod = request.queryParams("method");

        LOG.info("Request received: " + requestUriModel + " (" + requestUriModule + "." + requestUriMethod + ")");

        // 1) validate the model is supported
        if (!SUPPORTED_MODELS.contains(requestUriModel.split(":")[0].toLowerCase())) {
            LOG.warn("Invalid model requested: " + requestUriModel);
            throw new InvalidRequestModelException();
        }


        // 3) validate that the PCBID exists in the database
        final String encryptionKey = request.headers(CRYPT_KEY_HEADER);
        final String compressionScheme = request.headers(COMPRESSION_HEADER);
        byte[] reqBody = request.bodyAsBytes();

        // decrypt the request if it's encrypted
        if (!StringUtils.isBlank(encryptionKey)) {
            reqBody = Rc4.decrypt(reqBody, encryptionKey);
        }

        // decompress the request if it's compressed
        if (!StringUtils.isBlank(compressionScheme) &&
                compressionScheme.equals(LZ77_COMPRESSION)) {
            reqBody = Lz77.decompress(reqBody);
        }


        // convert the body to plaintext XML if it's binary XML
        Element rootNode = null;

        if (XmlUtils.isBinaryXML(reqBody)) {
            rootNode = XmlUtils.stringToXmlFile(PublicKt.kbinDecodeToString(reqBody));
        } else {
            rootNode = XmlUtils.byteArrayToXmlFile(reqBody);
        }

        // read the request body into an XML document
        if (rootNode == null ||
                !rootNode.getNodeName().equals("call")) {
            throw new InvalidRequestException();
        }

        final Element moduleNode = (Element) rootNode.getFirstChild();
        final String requestBodyModel = rootNode.getAttribute("model");
        final String requestBodyPcbId = rootNode.getAttribute("srcid");
        final String requestBodyModule = moduleNode.getNodeName();
        final String requestBodyMethod = moduleNode.getAttribute("method");

        // check if the PCB exists and is unbanned in the database
        Machine machine = this.machineDao.findByPcbId(requestBodyPcbId);

        if (machine == null) {
            // Not allow new pcbid by default
            LOG.warn("Invalid PCBID requested: " + requestBodyPcbId);
            throw new InvalidPcbIdException();
//            // create a machine and leave them enabled, ban them later if you want or change this
//            // to ban-by-default
//            final LocalDateTime now = LocalDateTime.now();
//            final ButterflyUser newUser = new ButterflyUser("0000", now, now, 10000);
//            userDao.create(newUser);
//
//            machine = new Machine(newUser, requestBodyPcbId, LocalDateTime.now(), true, 0);
//            machineDao.create(machine);
        } else if (!machine.isEnabled()) {
            throw new InvalidPcbIdException();
        }

        // 4) validate that the request URI matches the request body
        if (StringUtils.isBlank(requestBodyModel) ||
                StringUtils.isBlank(requestBodyModule) ||
                StringUtils.isBlank(requestBodyMethod) ||
                !requestBodyModel.equals(requestUriModel) ||
                !requestBodyModule.equals(requestUriModule) ||
                !requestBodyMethod.equals(requestUriMethod)) {
            throw new MismatchedRequestUriException();
        }

        // set the model, pcbid, module, and method as request "attributes" so they can be
        // used by the request handlers if needed
        request.attribute("model", requestBodyModel);
        request.attribute("pcbid", requestBodyPcbId);
        request.attribute("module", requestBodyModule);
        request.attribute("method", requestBodyMethod);

        // 5) return the node corresponding to the actual call
        return moduleNode;
    }

    /**
     * Check user authentication status
     * @param request The incoming request.
     * @return user authentication status
     */
    private boolean checkAuth(Request request){
        String tokenstr = null;
        // Cookies
        if(request.cookie("token")!=null){
            tokenstr = request.cookie("token");
        }else if(request.headers("Authorization")!=null){
            // Authorization header
            tokenstr = request.headers("Authorization");
        }

        if(tokenstr!=null&&!tokenstr.equals("")){
            Token token = tokenDao.findByToken(tokenstr);
            if(token!=null){
                LocalDateTime now = TimeUtils.getLocalDateTimeInUTC();
                return now.isBefore(token.getExpireTime());
            }

        }
        return false;
    }

    /**
     * Get ButtyerflyUser from token
     * @param request The incoming request.
     * @return ButterflyUser
     */
    private ButterflyUser getUser(Request request){
        String tokenstr = null;
        // Cookies
        if(request.cookie("token")!=null){
            tokenstr = request.cookie("token");
        }else if(request.headers("Authorization")!=null){
            // Authorization header
            tokenstr = request.headers("Authorization");
        }

        if(tokenstr!=null){
            return tokenDao.findByToken(tokenstr).getUser();
        }
        return null;
    }
}
