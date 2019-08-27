package com.buttongames.butterflydao.hibernate.dao.impl;

import com.buttongames.butterflydao.hibernate.dao.AbstractHibernateDao;
import com.buttongames.butterflymodel.model.Token;
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
public class TokenDao extends AbstractHibernateDao<Token> {

    @Autowired
    public TokenDao(final SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Token.class);
    }

    public Token findByToken(final String token){
        final Query<Token> query = this.sessionFactory.getCurrentSession().createQuery("from Token where token = :token");
        query.setParameter("token",token);

        return query.uniqueResult();
    }


}
