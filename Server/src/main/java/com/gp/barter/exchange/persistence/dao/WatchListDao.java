package com.gp.barter.exchange.persistence.dao;


import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.WatchListData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WatchListDao extends AbstractDao<WatchListData> {

    public WatchListDao() {
        setPersistentClass(WatchListData.class);
    }

    public Optional<WatchListData> findObservedOffer(Long offerId, Long userId) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.offerIdFk = :offerId and t.userIdFk = :userId and t.auditRd is null", persistentClass)
                .setParameter("offerId", offerId).setParameter("userId", userId).getResultList().stream().findFirst();
    }

    public List<WatchListData> findOffersObservedByUser(Long userId) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.userIdFk = :userId and t.auditRd is null", persistentClass)
                .setParameter("userId", userId).getResultList();
    }

    public List<WatchListData> findObservedOffers(Long offerId) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.offerIdFk = :offerId and t.auditRd is null", persistentClass)
                .setParameter("offerId", offerId).getResultList();
    }
}
