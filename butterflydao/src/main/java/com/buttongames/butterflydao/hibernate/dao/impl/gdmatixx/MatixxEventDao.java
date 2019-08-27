package com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx;


import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxEventData;
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
public class MatixxEventDao extends AbstractHibernateDao<matixxEventData> {

    @Autowired
    public MatixxEventDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(matixxEventData.class);
    }


    public List<matixxEventData> findByCard(Card card){
        final Query<matixxEventData> query = this.getCurrentSession().createQuery("from matixxEventData where card = :card");
        query.setParameter("card", card);

        return query.getResultList();
    }

}
