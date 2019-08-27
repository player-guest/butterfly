package com.buttongames.butterflydao.spring.configuration;

import com.buttongames.butterflydao.hibernate.dao.impl.*;
import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.GhostDataDao;
import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.Ddr16ProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxEventDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxStageDao;
import com.buttongames.butterflydao.hibernate.dao.impl.popn24.*;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4ParamDao;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4ProfileDao;
import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.ShopDao;
import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.GameplayEventLogDao;
import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.PcbEventLogDao;
import com.buttongames.butterflydao.hibernate.dao.impl.ddr16.UserSongRecordDao;
import com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv.Sdvx4SkillDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * Spring configuration for the Hibernate beans.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Configuration
@ComponentScan({"com.buttongames.butterflydao.spring.configuration"})
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement
public class HibernateConfiguration {

    /** The name of the sqlite database file */
    private static final String SQLITE_DATABASE = "butterfly.sqlite";

    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddl;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${hibernate.show_sql}")
    private String showSql;

    @Bean
    public LocalSessionFactoryBean sessionFactory(final DriverManagerDataSource dataSource) {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", this.hbm2ddl);
        hibernateProperties.setProperty("hibernate.dialect", this.dialect);
        hibernateProperties.setProperty("hibernate.show_sql", this.showSql);

        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.buttongames.butterflymodel.model");
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        final DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName(this.driverClassName);
        source.setUsername(this.username);
        source.setPassword(this.password);

        // locate the database in the user directory, and replace backslashes with forward slashes so it works on
        // Windows correctly, per sqlite-jdbc's spec
        final String dbPath = System.getProperty("db_path");
        System.out.println("dbPath: "+dbPath);
        source.setUrl("jdbc:sqlite:" + dbPath.replace('\\', '/'));

        return source;
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager(final SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    public ButterflyUserDao butterflyUserDao(final SessionFactory sessionFactory) {
        return new ButterflyUserDao(sessionFactory);
    }

    @Bean
    public TokenDao tokenDao(final SessionFactory sessionFactory){
        return new TokenDao(sessionFactory);
    }

    @Bean
    public MachineDao machineDao(final SessionFactory sessionFactory) {
        return new MachineDao(sessionFactory);
    }

    @Bean
    public UserPhasesDao userPhasesDao(final SessionFactory sessionFactory) {
        return new UserPhasesDao(sessionFactory);
    }

    @Bean
    public GameplayEventLogDao gameplayEventLogDao(final SessionFactory sessionFactory) {
        return new GameplayEventLogDao(sessionFactory);
    }

    @Bean
    public PcbEventLogDao pcbEventLogDao(final SessionFactory sessionFactory) {
        return new PcbEventLogDao(sessionFactory);
    }

    @Bean
    public ShopDao shopDao(final SessionFactory sessionFactory) {
        return new ShopDao(sessionFactory);
    }

    @Bean
    public CardDao cardDao(final SessionFactory sessionFactory) {
        return new CardDao(sessionFactory);
    }

    @Bean
    public Ddr16ProfileDao ddr16userProfileDao(final SessionFactory sessionFactory) {
        return new Ddr16ProfileDao(sessionFactory);
    }

    @Bean
    public UserSongRecordDao userSongRecordDao(final SessionFactory sessionFactory) {
        return new UserSongRecordDao(sessionFactory);
    }

    @Bean
    public GhostDataDao ghostDataDao(final SessionFactory sessionFactory) {
        return new GhostDataDao(sessionFactory);
    }

    @Bean
    public Sdvx4ProfileDao sdvxuserProfileDao(final SessionFactory sessionFactory) {
        return new Sdvx4ProfileDao(sessionFactory);
    }

    @Bean
    public Sdvx4ParamDao sdvxuserParamDao(final SessionFactory sessionFactory) {
        return new Sdvx4ParamDao(sessionFactory);
    }

    @Bean
    public Sdvx4SkillDao sdvxuserSkillDao(final SessionFactory sessionFactory) {
        return new Sdvx4SkillDao(sessionFactory);
    }

    @Bean
    public MatixxProfileDao matixxProfileDao(final SessionFactory sessionFactory) {
        return new MatixxProfileDao(sessionFactory);
    }

    @Bean
    public MatixxStageDao matixxStageDao(final SessionFactory sessionFactory){
        return new MatixxStageDao(sessionFactory);
    }

    @Bean
    public MatixxEventDao matixxEventDao(final SessionFactory sessionFactory){
        return new MatixxEventDao(sessionFactory);
    }

    @Bean
    public MatixxMusicDao matixxMusicDao(final SessionFactory sessionFactory){
        return new MatixxMusicDao(sessionFactory);
    }

    /** Popn 24 */

    @Bean
    public Popn24AccountDao popn24AccountDao(final SessionFactory sessionFactory){
        return new Popn24AccountDao(sessionFactory);
    }

    @Bean
    public Popn24ProfileDao popn24ProfileDao(final SessionFactory sessionFactory){
        return new Popn24ProfileDao(sessionFactory);
    }

    @Bean
    public Popn24ItemDao popn24ItemDao(final SessionFactory sessionFactory){
        return new Popn24ItemDao(sessionFactory);
    }

    @Bean
    public Popn24StageRecordDao popn24StageRecordDao(final SessionFactory sessionFactory){
        return new Popn24StageRecordDao(sessionFactory);
    }

    @Bean
    public Popn24CharaParamDao popn24CharaParamDao(final SessionFactory sessionFactory){
        return new Popn24CharaParamDao(sessionFactory);
    }

}
