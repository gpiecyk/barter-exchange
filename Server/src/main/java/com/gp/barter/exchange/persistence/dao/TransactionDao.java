package com.gp.barter.exchange.persistence.dao;

import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.TransactionData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionDao extends AbstractDao<TransactionData> {

    public TransactionDao() {
        setPersistentClass(TransactionData.class);
    }

    public List<TransactionData> findOfferedTransactionsByStatus(Long offeringUserId, String status) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and t.offeringUserFkId = :offeringUserId "
                + " and t.status = :status", TransactionData.class)
                .setParameter("offeringUserId", offeringUserId).setParameter("status", status).getResultList();
    }

    public List<TransactionData> findRequestTransactionsByStatus(Long publisherId, String status) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and t.publisherFkId = :publisherId "
                + " and t.status = :status", TransactionData.class)
                .setParameter("publisherId", publisherId).setParameter("status", status).getResultList();
    }

    public Optional<TransactionData> findTransaction(Long offeringUserId, Long offerId) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and t.offeringUserFkId = :offeringUserId "
                + " and t.offerIdFk = :offerId", TransactionData.class).setParameter("offeringUserId", offeringUserId)
                .setParameter("offerId", offerId).getResultList().stream().findFirst();
    }

    public List<TransactionData> findTransactions(Long offerId) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and t.offerIdFk = :offerId", TransactionData.class)
                .setParameter("offerId", offerId).getResultList();
    }
}
