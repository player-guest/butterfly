package com.buttongames.butterflydao.hibernate.dao.impl.popn24;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.popn24.popn24Mission;
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
public class Popn24MissionDao extends AbstractHibernateDao<popn24Mission> {

    @Autowired
    public Popn24MissionDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(popn24Mission.class);
    }

    public List<popn24Mission> findByCard(Card card) {
        final Query<popn24Mission> query = this.getCurrentSession().createQuery("from popn24Mission where card = :card");
        query.setParameter("card", card);
        return query.getResultList();
    }

    public popn24Mission findByMissionId(Card card, int mission_id) {
        final Query<popn24Mission> query = this.getCurrentSession().createQuery("from popn24Mission where card = :card and mission_id = :mission_id");
        query.setParameter("card", card);
        query.setParameter("mission_id", mission_id);
        return query.uniqueResult();
    }

}
