package com.gp.barter.exchange.persistence.dao;


import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.VerificationToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VerificationTokenDao extends AbstractDao<VerificationToken> {

    public VerificationTokenDao() {
        setPersistentClass(VerificationToken.class);
    }

    public Optional<VerificationToken> getVerificationToken(String token) {
        return em.createQuery("from " + persistentClass.getSimpleName()
            + " t where t.token = :token and t.auditRd is null", persistentClass)
                .setParameter("token", token).getResultList().stream().findFirst();
    }
}
