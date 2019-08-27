package com.buttongames.butterflydao.hibernate.dao.impl.sdvxiv;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.sdvxiv.sdvx4UserProfile;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for interacting with <code>sdvx4UserProfile</code> objects in the database.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
@Transactional
public class Sdvx4ProfileDao extends AbstractHibernateDao<sdvx4UserProfile> {


    @Autowired
    public Sdvx4ProfileDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(sdvx4UserProfile.class);
    }

    /**
     * Find a SDVX4 profile by the owner.
     * @param user The owning account
     * @return The profile for the given user
     */
    public sdvx4UserProfile findByUser(final ButterflyUser user) {
        final Query<sdvx4UserProfile> query = this.getCurrentSession().createQuery("from sdvx4UserProfile where user = :user");
        query.setParameter("user", user);

        return query.uniqueResult();
    }

    /**
     * Find a SDVX4 profile by the card.
     * @param card The card
     * @return The profile for the given user
     */
    public sdvx4UserProfile findByCard(final Card card){
        final Query<sdvx4UserProfile> query = this.getCurrentSession().createQuery("from sdvx4UserProfile where card = :card");
        query.setParameter("card", card);

        return query.uniqueResult();
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
