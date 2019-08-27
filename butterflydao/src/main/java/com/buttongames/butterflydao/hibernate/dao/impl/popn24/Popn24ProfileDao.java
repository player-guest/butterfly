package com.buttongames.butterflydao.hibernate.dao.impl.popn24;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.popn24.popn24Account;
import com.buttongames.butterflymodel.model.popn24.popn24Profile;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
@Transactional
public class Popn24ProfileDao extends AbstractHibernateDao<popn24Profile> {

    @Autowired
    public Popn24ProfileDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(popn24Profile.class);
    }

    public popn24Profile findByCard(Card card){
        final Query<popn24Profile> query = this.getCurrentSession().createQuery("from popn24Profile where card = :card");
        query.setParameter("card", card);
        return query.uniqueResult();
    }
    
}
