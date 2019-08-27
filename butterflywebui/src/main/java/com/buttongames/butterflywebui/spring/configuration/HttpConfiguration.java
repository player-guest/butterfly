package com.buttongames.butterflywebui.spring.configuration;

import com.buttongames.butterflydao.hibernate.dao.impl.ButterflyUserDao;
import com.buttongames.butterflydao.hibernate.dao.impl.CardDao;
import com.buttongames.butterflydao.hibernate.dao.impl.MachineDao;
import com.buttongames.butterflydao.hibernate.dao.impl.TokenDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxStageDao;
import com.buttongames.butterflywebui.http.WebUiHttpServer;
import com.buttongames.butterflywebui.http.controllers.api.CardHandler;
import com.buttongames.butterflywebui.http.controllers.api.MachineHandler;
import com.buttongames.butterflywebui.http.controllers.api.UserHandler;
import com.buttongames.butterflywebui.http.controllers.api.admin.game.MatixxManageHandler;
import com.buttongames.butterflywebui.http.controllers.api.game.MatixxHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Bean config class for HTTP-related classes.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Configuration
@ComponentScan({"com.buttongames.butterflywebui.spring.configuration",
        "com.buttongames.butterflydao.spring.configuration"})
@PropertySource("classpath:butterflywebui.properties")
public class HttpConfiguration {

    @Bean
    public WebUiHttpServer webUiHttpServer(final TokenDao tokenDao, final UserHandler userHandler, final CardHandler cardHandler, final MachineHandler machineHandler, final MatixxHandler matixxHandler, final MatixxManageHandler matixxManageHandler){
        return new WebUiHttpServer(tokenDao,userHandler,cardHandler,machineHandler,matixxHandler,matixxManageHandler);
    }

    @Bean
    public UserHandler userHandler(final ButterflyUserDao butterflyUserDao, final TokenDao tokenDao){
        return new UserHandler(butterflyUserDao,tokenDao);
    }

    @Bean
    public CardHandler cardHandler(final ButterflyUserDao userDao, final TokenDao tokenDao, final CardDao cardDao){
        return new CardHandler(userDao, tokenDao, cardDao);
    }

    @Bean
    public MachineHandler machineHandler(ButterflyUserDao userDao, TokenDao tokenDao, MachineDao machineDao){
        return new MachineHandler(userDao,tokenDao,machineDao);
    }

    @Bean
    public MatixxHandler matixxHandler(ButterflyUserDao userDao, CardDao cardDao, MatixxProfileDao matixxProfileDao, MatixxStageDao matixxStageDao, MatixxMusicDao matixxMusicDao){
        return new MatixxHandler(userDao,cardDao,matixxProfileDao,matixxStageDao,matixxMusicDao);
    }

    @Bean
    public MatixxManageHandler matixxManageHandler(MatixxMusicDao matixxMusicDao){
        return new MatixxManageHandler(matixxMusicDao);
    }
}
