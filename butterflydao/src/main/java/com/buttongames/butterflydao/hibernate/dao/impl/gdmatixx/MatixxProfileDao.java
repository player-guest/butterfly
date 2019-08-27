package com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerProfile;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for interacting with <code>matixxPlayerProfile</code> objects in the database.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
@Transactional
public class MatixxProfileDao extends AbstractHibernateDao<matixxPlayerProfile> {

    @Autowired
    public MatixxProfileDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(matixxPlayerProfile.class);
    }

    public matixxPlayerProfile findByUser(ButterflyUser user){
        final Query<matixxPlayerProfile> query = this.getCurrentSession().createQuery("from matixxPlayerProfile where user = :user");
        query.setParameter("user", user);

        return query.uniqueResult();
    }

    public matixxPlayerProfile findByCard(Card card){
        final Query<matixxPlayerProfile> query = this.getCurrentSession().createQuery("from matixxPlayerProfile where card = :card");
        query.setParameter("card", card);

        return query.uniqueResult();
    }
}
