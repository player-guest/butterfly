package com.buttongames.butterflyserver.spring.configuration;

import com.buttongames.butterflycore.util.CardIdUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.*;
import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.*;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.*;
import com.buttongames.butterflydao.hibernate.dao.impl.popn24.*;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4ParamDao;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4ProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4SkillDao;
import com.buttongames.butterflyserver.http.ButterflyHttpServer;
import com.buttongames.butterflyserver.http.api.ApiCardHandler;
import com.buttongames.butterflyserver.http.api.ApiMachineHandler;
import com.buttongames.butterflyserver.http.api.ApiManageHandler;
import com.buttongames.butterflyserver.http.api.ApiUserHandler;
import com.buttongames.butterflyserver.http.api.game.ApiMatixxHandler;
import com.buttongames.butterflyserver.http.handlers.KfcHandler;
import com.buttongames.butterflyserver.http.handlers.M32Handler;
import com.buttongames.butterflyserver.http.handlers.M39Handler;
import com.buttongames.butterflyserver.http.handlers.MdxHandler;
import com.buttongames.butterflyserver.http.handlers.baseImpl.*;
import com.buttongames.butterflyserver.http.handlers.ddr16impl.PlayerDataRequestHandler;
import com.buttongames.butterflyserver.http.handlers.kfcivImpl.GameRequestHandler;
import com.buttongames.butterflyserver.http.handlers.matixxImpl.*;
import com.buttongames.butterflyserver.http.handlers.popn24Impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Bean config class for <code>com.buttongames.butterflyserver.http</code> package.
 *
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Configuration
@ComponentScan({"com.buttongames.butterflyserver.spring.configuration",
        "com.buttongames.butterflydao.spring.configuration"})
@PropertySource("classpath:butterflyserver.properties")
public class HttpConfiguration {

    @Bean
    public ButterflyHttpServer butterflyHttpServer(MdxHandler mdxHandler, MachineDao machineDao, ButterflyUserDao userDao, KfcHandler kfcHandler, M32Handler m32Handler, M39Handler m39Handler, TokenDao tokenDao, ApiUserHandler apiUserHandler, ApiCardHandler apiCardHandler, ApiMachineHandler apiMachineHandler, ApiMatixxHandler apiMatixxHandler, ApiManageHandler apiManageHandler) {
        return new ButterflyHttpServer(mdxHandler, machineDao, userDao, kfcHandler, m32Handler, m39Handler, tokenDao, apiUserHandler, apiCardHandler, apiMachineHandler, apiMatixxHandler, apiManageHandler);
    }

    @Bean
    public MdxHandler mdxHandler(
            final ServicesRequestHandler servicesRequestHandler,
            final PcbEventRequestHandler pcbEventRequestHandler,
            final PcbTrackerRequestHandler pcbTrackerRequestHandler,
            final MessageRequestHandler messageRequestHandler,
            final FacilityRequestHandler facilityRequestHandler,
            final PackageRequestHandler packageRequestHandler,
            final EventLogRequestHandler eventLogRequestHandler,
            final TaxRequestHandler taxRequestHandler,
            final PlayerDataRequestHandler playerDataRequestHandler,
            final CardManageRequestHandler cardManageRequestHandler,
            final SystemRequestHandler systemRequestHandler,
            final EacoinRequestHandler eacoinRequestHandler,
            final MachineDao machineDao,
            final ButterflyUserDao userDao) {
        return new MdxHandler(servicesRequestHandler, pcbEventRequestHandler, pcbTrackerRequestHandler,
                messageRequestHandler, facilityRequestHandler, packageRequestHandler, eventLogRequestHandler,
                taxRequestHandler, playerDataRequestHandler, cardManageRequestHandler, systemRequestHandler,
                eacoinRequestHandler, machineDao, userDao);
    }

    @Bean
    public KfcHandler kfcHandler(final ServicesRequestHandler servicesRequestHandler,
                                 final PcbEventRequestHandler pcbEventRequestHandler,
                                 final PcbTrackerRequestHandler pcbTrackerRequestHandler,
                                 final MessageRequestHandler messageRequestHandler,
                                 final FacilityRequestHandler facilityRequestHandler,
                                 final PackageRequestHandler packageRequestHandler,
                                 final EventLogRequestHandler eventLogRequestHandler,
                                 final TaxRequestHandler taxRequestHandler,
                                 final PlayerDataRequestHandler playerDataRequestHandler,
                                 final CardManageRequestHandler cardManageRequestHandler,
                                 final SystemRequestHandler systemRequestHandler,
                                 final EacoinRequestHandler eacoinRequestHandler,
                                 final GameRequestHandler gameRequestHandler) {
        return new KfcHandler(servicesRequestHandler, pcbEventRequestHandler, pcbTrackerRequestHandler,
                messageRequestHandler, facilityRequestHandler, packageRequestHandler, eventLogRequestHandler,
                taxRequestHandler, playerDataRequestHandler, cardManageRequestHandler, systemRequestHandler,
                eacoinRequestHandler, gameRequestHandler);
    }


    @Bean
    public CardIdUtils cardIdUtils() {
        return new CardIdUtils();
    }

    @Bean
    public EventLogRequestHandler eventLogRequestHandler(final GameplayEventLogDao gameplayEventLogDao) {
        return new EventLogRequestHandler(gameplayEventLogDao);
    }

    @Bean
    public FacilityRequestHandler facilityRequestHandler(final ShopDao shopDao, final MachineDao machineDao) {
        return new FacilityRequestHandler(shopDao, machineDao);
    }

    @Bean
    public MessageRequestHandler messageRequestHandler() {
        return new MessageRequestHandler();
    }

    @Bean
    public PackageRequestHandler packageRequestHandler() {
        return new PackageRequestHandler();
    }

    @Bean
    public PcbEventRequestHandler pcbEventRequestHandler(final PcbEventLogDao pcbEventLogDao) {
        return new PcbEventRequestHandler(pcbEventLogDao);
    }

    @Bean
    public PcbTrackerRequestHandler pcbTrackerRequestHandler() {
        return new PcbTrackerRequestHandler();
    }

    @Bean
    public ServicesRequestHandler servicesRequestHandler() {
        return new ServicesRequestHandler();
    }

    @Bean
    public PlayerDataRequestHandler playerDataRequestHandler(final ButterflyUserDao userDao, final CardDao cardDao,
                                                             final Ddr16ProfileDao ddr16ProfileDao, final GhostDataDao ghostDataDao,
                                                             final UserSongRecordDao songRecordDao) {
        return new PlayerDataRequestHandler(userDao, cardDao, ddr16ProfileDao, ghostDataDao, songRecordDao);
    }

    @Bean
    public TaxRequestHandler taxRequestHandler(final MachineDao machineDao, final UserPhasesDao userPhasesDao) {
        return new TaxRequestHandler(machineDao, userPhasesDao);
    }

    @Bean
    public CardManageRequestHandler cardManageRequestHandler(final CardDao cardDao, final ButterflyUserDao userDao,
                                                             final CardIdUtils cardIdUtils) {
        return new CardManageRequestHandler(cardDao, userDao, cardIdUtils);
    }

    @Bean
    public SystemRequestHandler systemRequestHandler(final CardIdUtils cardIdUtils) {
        return new SystemRequestHandler(cardIdUtils);
    }

    @Bean
    public EacoinRequestHandler eacoinRequestHandler(final CardDao cardDao) {
        return new EacoinRequestHandler(cardDao);
    }

    /**
     * SDVX4
     */
    @Bean
    public GameRequestHandler gameRequestHandler(final ButterflyUserDao userDao, final CardDao cardDao, final Sdvx4ProfileDao sdvx4ProfileDao, final Sdvx4SkillDao sdvx4SkillDao, final Sdvx4ParamDao sdvx4ParamDao) {
        return new GameRequestHandler(userDao, cardDao, sdvx4ProfileDao, sdvx4SkillDao, sdvx4ParamDao);
    }

    /**
     * Gitadora Matixx
     */

    @Bean M32Handler m32Handler(final MatixxHandler matixxHandler){
        return new M32Handler(matixxHandler);
    }

    @Bean
    public MatixxHandler matixxHandler(final ServicesRequestHandler servicesRequestHandler,
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
        return new MatixxHandler(servicesRequestHandler, pcbEventRequestHandler,
                pcbTrackerRequestHandler, messageRequestHandler,
                facilityRequestHandler, packageRequestHandler,
                eventLogRequestHandler ,taxRequestHandler,
                cardManageRequestHandler, systemRequestHandler,
                eacoinRequestHandler,matixxShopInfoRequestHandler,
                matixxGameInfoRequestHandler, matixxCardUtilRequestHandler,
                matixxGameTopRequestHandler, matixxGameEndRequestHandler,
                matixxPlayableMusicRequestHandler, matixxBemaniGakuenRequestHandler);
    }

    @Bean
    public MatixxShopInfoRequestHandler matixxShopInfoRequestHandler() {
        return new MatixxShopInfoRequestHandler();
    }

    @Bean
    public MatixxBemaniGakuenRequestHandler matixxBemaniGakuenRequestHandler() {
        return new MatixxBemaniGakuenRequestHandler();
    }

    @Bean
    public MatixxCardUtilRequestHandler matixxCardUtilRequestHandler(ButterflyUserDao userDao, CardDao cardDao, MatixxProfileDao matixxProfileDao) {
        return new MatixxCardUtilRequestHandler(userDao, cardDao, matixxProfileDao);
    }

    @Bean
    public MatixxGameEndRequestHandler matixxGameEndRequestHandler(CardDao cardDao, MatixxProfileDao matixxProfileDao, MatixxStageDao matixxStageDao, MatixxEventDao matixxEventDao, MatixxMusicDao matixxMusicDao) {
        return new MatixxGameEndRequestHandler(cardDao, matixxProfileDao, matixxStageDao, matixxEventDao, matixxMusicDao);
    }

    @Bean
    public MatixxGameInfoRequestHandler matixxGameInfoRequestHandler() {
        return new MatixxGameInfoRequestHandler();
    }

    @Bean
    public MatixxGameTopRequestHandler matixxGameTopRequestHandler(CardDao cardDao, MatixxProfileDao matixxProfileDao, MatixxStageDao matixxStageDao, MatixxPlayerboardDao matixxPlayerboardDao, MatixxEventDao matixxEventDao) {
        return new MatixxGameTopRequestHandler(cardDao, matixxProfileDao, matixxStageDao, matixxPlayerboardDao, matixxEventDao);
    }

    @Bean
    public MatixxPlayableMusicRequestHandler matixxPlayableMusicRequestHandler(MatixxMusicDao matixxMusicDao) {
        return new MatixxPlayableMusicRequestHandler(matixxMusicDao);
    }

    @Bean
    public ApiUserHandler apiUserHandler(final ButterflyUserDao butterflyUserDao, final TokenDao tokenDao) {
        return new ApiUserHandler(butterflyUserDao, tokenDao);
    }

    @Bean
    public ApiCardHandler apiCardHandler(final ButterflyUserDao userDao, final TokenDao tokenDao, final CardDao cardDao) {
        return new ApiCardHandler(userDao, tokenDao, cardDao);
    }

    @Bean
    public ApiMachineHandler apiMachineHandler(ButterflyUserDao userDao, TokenDao tokenDao, MachineDao machineDao) {
        return new ApiMachineHandler(userDao, tokenDao, machineDao);
    }

    @Bean
    public ApiMatixxHandler apiMatixxHandler(ButterflyUserDao userDao, CardDao cardDao, MatixxProfileDao matixxProfileDao, MatixxStageDao matixxStageDao, MatixxMusicDao matixxMusicDao, MatixxPlayerboardDao matixxPlayerboardDao) {
        return new ApiMatixxHandler(userDao, cardDao, matixxProfileDao, matixxStageDao, matixxMusicDao, matixxPlayerboardDao);
    }

    @Bean
    public ApiManageHandler matixxManageHandler(MatixxMusicDao matixxMusicDao, MatixxStageDao matixxStageDao, ButterflyUserDao userDao, MatixxProfileDao matixxProfileDao, CardDao cardDao) {
        return new ApiManageHandler(matixxMusicDao,matixxStageDao,userDao,matixxProfileDao,cardDao);
    }

    /**
     * popn
     */
    @Bean
    public M39Handler m39Handler(Popn24Handler popn24Handler) {
        return new M39Handler(popn24Handler);
    }

    @Bean
    public Popn24Handler popn24Handler(final ServicesRequestHandler servicesRequestHandler,
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
                                       final Info24Handler info24Handler,
                                       final Lobby24Handler lobby24Handler,
                                       final Pcb24Handler pcb24Handler,
                                       final Player24Handler player24Handler) {
        return new Popn24Handler(servicesRequestHandler, pcbEventRequestHandler, pcbTrackerRequestHandler,
                messageRequestHandler, facilityRequestHandler, packageRequestHandler, eventLogRequestHandler,
                taxRequestHandler, cardManageRequestHandler, systemRequestHandler,
                eacoinRequestHandler, info24Handler, lobby24Handler, pcb24Handler, player24Handler);
    }

    @Bean
    public Info24Handler info24Handler(){
        return new Info24Handler();
    }

    @Bean
    public Lobby24Handler lobby24Handler(){
        return new Lobby24Handler();
    }

    @Bean
    public Pcb24Handler pcb24Handler(){
        return new Pcb24Handler();
    }

    @Bean
    public Player24Handler player24Handler(CardDao cardDao, Popn24AccountDao popn24AccountDao, Popn24ProfileDao popn24ProfileDao, Popn24StageRecordDao popn24StageRecordDao, Popn24ItemDao popn24ItemDao, Popn24CharaParamDao popn24CharaParamDao, Popn24MissionDao popn24MissionDao) {
        return new Player24Handler(cardDao, popn24AccountDao, popn24ProfileDao, popn24StageRecordDao, popn24ItemDao, popn24CharaParamDao, popn24MissionDao);
    }
}
