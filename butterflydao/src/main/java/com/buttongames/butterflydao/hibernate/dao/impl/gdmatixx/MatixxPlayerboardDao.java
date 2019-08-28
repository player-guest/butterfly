package com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx;


import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.gdmatixx.matixxPlayerboard;
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
public class MatixxPlayerboardDao extends AbstractHibernateDao<matixxPlayerboard> {

    @Autowired
    public MatixxPlayerboardDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(matixxPlayerboard.class);
    }


    public List<matixxPlayerboard> findByCard(Card card) {
        Query<matixxPlayerboard> query = getCurrentSession().createQuery("from matixxPlayerboard where card = :card");
        query.setParameter("card", card);
        return query.getResultList();
    }

    public matixxPlayerboard findBySticker(Card card, int stickerId) {
        Query<matixxPlayerboard> query = getCurrentSession().createQuery("from matixxPlayerboard where card = :card and stickerId = :stickerId");
        query.setParameter("card",card);
        query.setParameter("stickerId", stickerId);
        return query.uniqueResult();
    }
}
