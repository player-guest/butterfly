package com.buttongames.butterflydao.hibernate.dao.impl.popn24;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.popn24.popn24Account;
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
public class Popn24AccountDao extends AbstractHibernateDao<popn24Account> {

    @Autowired
    public Popn24AccountDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(popn24Account.class);
    }

    public popn24Account findByCard(Card card){
        final Query<popn24Account> query = this.getCurrentSession().createQuery("from popn24Account where card = :card");
        query.setParameter("card", card);
        return query.uniqueResult();
    }

}
