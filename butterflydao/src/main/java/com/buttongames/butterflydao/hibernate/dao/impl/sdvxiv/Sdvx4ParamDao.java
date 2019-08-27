package com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.sdvxiv.sdvx4UserParam;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DAO for interacting with <code>sdvx4UserProfile</code> objects in the database.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
@Transactional
public class Sdvx4ParamDao extends AbstractHibernateDao<sdvx4UserParam> {


    @Autowired
    public Sdvx4ParamDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(sdvx4UserParam.class);
    }

    /**
     * Find a SDVX4 param by the owner.
     * @param user The owning account
     * @return The profile for the given user
     */
    public List<sdvx4UserParam> findByUser(final ButterflyUser user) {
        final Query<sdvx4UserParam> query = this.getCurrentSession().createQuery("from sdvx4UserParam where user = :user");
        query.setParameter("user", user);

        return query.getResultList();
    }

    /**
     * Find a SDVX4 param by card.
     * @param card The card
     * @return The profile for the given user
     */
    public List<sdvx4UserParam> findByCard(final Card card) {
        final Query<sdvx4UserParam> query = this.getCurrentSession().createQuery("from sdvx4UserParam where card = :card");
        query.setParameter("card", card);

        return query.getResultList();
    }

//    /**
//     * Finds a DDR profile by the dancer code.
//     * @param dancerCode The dancer code for the profile
//     * @return The profile for the given dancer code
//     */
//    public sdvx4UserProfile findByDancerCode(final int dancerCode) {
//        final Query<sdvx4UserProfile> query = this.getCurrentSession().createQuery("from sdvx4UserProfile where dancer_code = :dancerCode");
//        query.setParameter("dancerCode", dancerCode);
//
//        return query.uniqueResult();
//    }
}
