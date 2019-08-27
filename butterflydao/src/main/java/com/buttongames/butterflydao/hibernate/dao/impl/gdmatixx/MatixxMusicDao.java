package com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx;


import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.gdmatixx.matixxMusic;
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
public class MatixxMusicDao extends AbstractHibernateDao<matixxMusic> {

    @Autowired
    public MatixxMusicDao(final SessionFactory sessionFactory){
        super(sessionFactory);
        setClazz(matixxMusic.class);
    }

    public matixxMusic findByMusicId(int musicid) {
        Query<matixxMusic> query = getCurrentSession().createQuery("from matixxMusic where musicid = :musicid");
        query.setParameter("musicid",musicid);
        return query.uniqueResult();
    }

    public List<matixxMusic> findByListType(int list_type) {
        Query<matixxMusic> query = getCurrentSession().createQuery("from matixxMusic where list_type = :list_type");
        query.setParameter("list_type",list_type);
        return query.getResultList();
    }
}
