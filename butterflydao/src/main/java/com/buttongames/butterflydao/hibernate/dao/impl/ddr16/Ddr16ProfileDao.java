package com.buttongames.butterflydao.hibernate.dao.impl.ddr16;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.ddr16.ddr16UserProfile;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for interacting with <code>sdvx4UserParam</code> objects in the database.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository("ddr16")
@Transactional
public class Ddr16ProfileDao extends AbstractHibernateDao<ddr16UserProfile> {

    @Autowired
    public Ddr16ProfileDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(ddr16UserProfile.class);
    }

    /**
     * Find a DDR profile by the owner.
     * @param user The owning account
     * @return The profile for the given user
     */
    public ddr16UserProfile findByUser(final ButterflyUser user) {
        final Query<ddr16UserProfile> query = this.getCurrentSession().createQuery("from ddr16UserProfile where user = :user");
        query.setParameter("user", user);

        return query.uniqueResult();
    }

    /**
     * Find a DDR profile by card.
     * @param card The owning card
     * @return The profile for the given user
     */
    public ddr16UserProfile findByCard(final Card card) {
        final Query<ddr16UserProfile> query = this.getCurrentSession().createQuery("from ddr16UserProfile where card = :card");
        query.setParameter("card", card);

        return query.uniqueResult();
    }

    /**
     * Finds a DDR profile by the dancer code.
     * @param dancerCode The dancer code for the profile
     * @return The profile for the given dancer code
     */
    public ddr16UserProfile findByDancerCode(final int dancerCode) {
        final Query<ddr16UserProfile> query = this.getCurrentSession().createQuery("from ddr16UserProfile where dancer_code = :dancerCode");
        query.setParameter("dancerCode", dancerCode);

        return query.uniqueResult();
    }
}
