package com.buttongames.butterflydao.hibernate.dao.impl.popn24;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.popn24.popn24Item;
import com.buttongames.butterflymodel.model.popn24.popn24StageRecord;
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
public class Popn24StageRecordDao extends AbstractHibernateDao<popn24StageRecord> {

    @Autowired
    public Popn24StageRecordDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(popn24StageRecord.class);
    }

    public List<popn24StageRecord> findByCard(Card card){
        final Query<popn24StageRecord> query = this.getCurrentSession().createQuery("from popn24StageRecord where card = :card");
        query.setParameter("card", card);
        return query.getResultList();
    }
    
}
