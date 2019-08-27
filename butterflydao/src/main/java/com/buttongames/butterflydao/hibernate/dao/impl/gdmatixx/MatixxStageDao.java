package com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx;


import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxStageRecord;
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
public class MatixxStageDao extends AbstractHibernateDao<matixxStageRecord> {

    @Autowired
    public MatixxStageDao (final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(matixxStageRecord.class);
    }

    public List<matixxStageRecord> findByCard(Card card){
        final Query<matixxStageRecord> query = this.getCurrentSession().createQuery("from matixxStageRecord where card = :card");
        query.setParameter("card", card);

        return query.getResultList();
    }

    public List<matixxStageRecord> findByCard(Card card,String type){
        final Query<matixxStageRecord> query = this.getCurrentSession().createQuery("from matixxStageRecord where card = :card and type = :type");
        query.setParameter("card", card);
        query.setParameter("type", type);

        return query.getResultList();
    }
}
