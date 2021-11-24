package com.gp.barter.exchange.persistence.dao;


import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.UserData;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao extends AbstractDao<UserData> {

    public UserDao() {
        setPersistentClass(UserData.class);
    }

    public Optional<UserData> getUserByEmail(String email) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.email = :email and t.auditRd is null", persistentClass)
                .setParameter("email", email).getResultList().stream().findFirst();

    }
}
