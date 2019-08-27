package com.buttongames.butterflywebui.http;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.util.TimeUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.TokenDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Token;
import com.buttongames.butterflywebui.http.controllers.api.CardHandler;
import com.buttongames.butterflywebui.http.controllers.api.MachineHandler;
import com.buttongames.butterflywebui.http.controllers.api.UserHandler;
import com.buttongames.butterflywebui.http.controllers.api.admin.game.MatixxManageHandler;
import com.buttongames.butterflywebui.http.controllers.api.game.MatixxHandler;
import com.buttongames.butterflywebui.util.PropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spark.Request;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * The main HTTP server. This class is responsible for the top-level handling of incoming
 * requests, then delegates the responsibility to the appropriate controller.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Component
public class WebUiHttpServer {

    private static final Logger LOG = LogManager.getLogger(WebUiHttpServer.class);


    private final TokenDao tokenDao;

    private final UserHandler userHandler;

    private final CardHandler cardHandler;

    private final MachineHandler machineHandler;

    private final MatixxHandler matixxHandler;

    private final MatixxManageHandler matixxManageHandler;


    /** The port the server listens on */
    @Value(PropertyNames.PORT)
    private String port;

    @Autowired
    public WebUiHttpServer(TokenDao tokenDao, UserHandler userHandler, CardHandler cardHandler, MachineHandler machineHandler, MatixxHandler matixxHandler, MatixxManageHandler matixxManageHandler) {
        this.tokenDao = tokenDao;
        this.userHandler = userHandler;
        this.cardHandler = cardHandler;
        this.machineHandler = machineHandler;
        this.matixxHandler = matixxHandler;
        this.matixxManageHandler = matixxManageHandler;
    }


    /**
     * Configures the routes on our server and begins listening.
     */
    public void startServer() {
        // configure the server properties
        int maxThreads = 20;
        int minThreads = 2;
        int timeOutMillis = 30000;

        // once routes are configured, the server automatically begins
        threadPool(maxThreads, minThreads, timeOutMillis);
        port(Integer.parseInt(this.port));
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

        staticFiles.location("/public");

        before("/register/",(req,resp)->{
            if(checkAuth(req)) resp.redirect("/");
        });
        before("/portal/",(req,resp)->{
            if(!checkAuth(req)) resp.redirect("/");
        });
        before("/profile/",(req,resp)->{
            if(!checkAuth(req)) resp.redirect("/");
        });
        before("/card/",(req,resp)->{
            if(!checkAuth(req)) resp.redirect("/");
        });
        before("/machine/",(req,resp)->{
            if(!checkAuth(req)) resp.redirect("/");
        });

        get("/", (req, resp) -> {
            if(checkAuth(req)) resp.redirect("/portal/");
                 return renderContent("/public/index/content.html");
        });

        get("/register/", (req, resp) -> renderContent("/public/register/content.html"));

        get("/portal/", (req, resp) -> renderContent("/public/portal/content.html"));

        get("/profile/", (req, resp) -> renderContent("/public/profile/content.html"));

        get("/card/", (req, resp) -> renderContent("/public/card/content.html"));

        get("/machine/", (req, resp) -> renderContent("/public/machine/content.html"));


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
                post("/auth",(req,resp) -> userHandler.handleRequest("auth", req, resp));
                post("/register",(req,resp) -> userHandler.handleRequest("register", req, resp));
                post("/logout",(req,resp) -> userHandler.handleRequest("logout", req, resp));
            });

            before("/admin/*",(req,resp)-> {
                if(!checkAuth(req)){
                    halt(401, JSONUtil.errorMsg("Unauthenticated"));
                }else if(!getUser(req).getUser_group().equals("admin")){
                    halt(401, JSONUtil.errorMsg("No Permission"));
                }
            });

            path("/admin", ()->{
                post("/musiclist",(req,resp)-> matixxManageHandler.handleRequest("set_musiclist", req, resp));
            });

            before("/card/*",(req,resp)-> {
                if(!checkAuth(req)){
                    halt(401, JSONUtil.errorMsg("Unauthenticated"));
                }
            });

            path("/card",()->{
                get("/list",(req,resp) -> cardHandler.handleRequest("get", getUser(req), req,resp));
                post("/bind",(req,resp) -> cardHandler.handleRequest("bind", getUser(req), req,resp));
                post("/unbind",(req,resp) -> cardHandler.handleRequest("unbind", getUser(req), req,resp));
            });

            before("/machine/*",(req,resp)-> {
                if(!checkAuth(req)){
                    halt(401, JSONUtil.errorMsg("Unauthenticated"));
                }
            });

            path("/machine",()->{
                get("/list",(req,resp) -> machineHandler.handleRequest("get", getUser(req), req,resp));
                post("/generate",(req,resp) -> machineHandler.handleRequest("generate", getUser(req), req,resp));
                post("/delete",(req,resp) -> machineHandler.handleRequest("delete", getUser(req), req,resp));
            });


            path("/game",()->{

                path("/matixx",()->{
                   get("/profile", (req,resp)-> matixxHandler.handleRequest("get_profile", getUser(req),req,resp));
                   post("/profile", (req,resp)-> matixxHandler.handleRequest("update_profile", getUser(req),req,resp));
                   get("/musiclist", (req,resp)-> matixxHandler.handleRequest("musiclist", getUser(req),req,resp));
                   get("/recordlist", (req,resp)-> matixxHandler.handleRequest("play_record_list", getUser(req),req,resp));
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



    }

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
                if(now.isBefore(token.getExpireTime())){
                    return true;
                }
            }

        }
        return false;
    }

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

    private String renderContent(String htmlFile) throws Exception {
        final URI uri = getClass().getResource(htmlFile).toURI();
        FileSystem zipfs = initFileSystem(uri);
        Path path = Paths.get(uri);
        String result = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        zipfs.close();
        return result;
    }

    private FileSystem initFileSystem(URI uri) throws IOException
    {
        try
        {
            return FileSystems.getFileSystem(uri);
        }
        catch( FileSystemNotFoundException e )
        {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            return FileSystems.newFileSystem(uri, env);
        }
    }
}
