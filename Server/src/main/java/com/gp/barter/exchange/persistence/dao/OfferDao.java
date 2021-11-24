package com.gp.barter.exchange.persistence.dao;


import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.OfferData;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OfferDao extends AbstractDao<OfferData> {

    public OfferDao() {
        setPersistentClass(OfferData.class);
    }

    public List<OfferData> findPaginated(int start, int size) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null", persistentClass).setFirstResult(start).setMaxResults(size).getResultList();
    }

    public List<OfferData> findSearchPaginated(int start, int size, String titlePattern) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and t.title like :titlePattern", persistentClass)
                .setParameter("titlePattern", "%" + titlePattern + "%").setFirstResult(start).setMaxResults(size).getResultList();
    }

    public List<String> findSearchOfferTitles(String titlePattern) {
        return em.createQuery("select t.title from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and t.title like :titlePattern", String.class)
                .setParameter("titlePattern", "%" + titlePattern + "%").getResultList();
    }

    public List<OfferData> findByUserId(Long id) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and t.userFkId = :id", persistentClass)
                .setParameter("id", id).getResultList();
    }

    public List<OfferData> findOldOffers() {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.auditRd is null and DATE(t.endDate) < current_date", persistentClass).getResultList();
    }

    public Long getOffersCount() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<OfferData> root = query.from(OfferData.class);
        query.select(criteriaBuilder.count(root)).where(criteriaBuilder.isNull(root.get("auditRd")));
        return em.createQuery(query).getSingleResult();
    }

    public Long getSearchOffersCount(String titlePattern) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<OfferData> root = query.from(OfferData.class);
        Predicate restriction = criteriaBuilder.and(
                criteriaBuilder.isNull(root.get("auditRd")),
                criteriaBuilder.like(root.get("title"), "%" + titlePattern + "%")
        );
        query.select(criteriaBuilder.count(root)).where(restriction);
        return em.createQuery(query).getSingleResult();
    }
}
