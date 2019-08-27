package com.buttongames.butterflydao.hibernate.dao.impl.popn24;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Card;
import com.buttongames.butterflymodel.model.popn24.popn24CharaParam;
import com.buttongames.butterflymodel.model.popn24.popn24Item;
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
public class Popn24CharaParamDao extends AbstractHibernateDao<popn24CharaParam> {

    @Autowired
    public Popn24CharaParamDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(popn24CharaParam.class);
    }

    public List<popn24CharaParam> findByCard(Card card){
        final Query<popn24CharaParam> query = this.getCurrentSession().createQuery("from popn24CharaParam where card = :card");
        query.setParameter("card", card);
        return query.getResultList();
    }

    public popn24CharaParam findByCharaId(Card card,int chara_id){
        final Query<popn24CharaParam> query = this.getCurrentSession().createQuery("from popn24CharaParam where card = :card and chara_id = :chara_id");
        query.setParameter("card", card);
        query.setParameter("chara_id",chara_id);
        return query.uniqueResult();
    }
    
}
