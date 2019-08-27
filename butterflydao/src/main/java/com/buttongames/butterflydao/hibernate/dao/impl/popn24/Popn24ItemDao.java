package com.buttongames.butterflydao.hibernate.dao.impl.popn24;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.popn24.popn24Item;
import com.buttongames.butterflymodel.model.popn24.popn24Profile;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
@Transactional
public class Popn24ItemDao extends AbstractHibernateDao<popn24Item> {

    @Autowired
    public Popn24ItemDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(popn24Item.class);
    }

    public List<popn24Item> findByCard(Card card){
        final Query<popn24Item> query = this.getCurrentSession().createQuery("from popn24Item where card = :card");
        query.setParameter("card", card);
        return query.getResultList();
    }
    
}
