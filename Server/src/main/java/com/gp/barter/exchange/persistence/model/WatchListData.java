package com.gp.barter.exchange.persistence.model;


import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import com.gp.barter.exchange.util.constants.TableColumnNames;

import javax.persistence.*;

@Entity
@Table(name = "WATCH_LIST")
@AttributeOverrides({
        @AttributeOverride(name = "auditCd", column = @Column(name = TableColumnNames.WAL_AUDIT_CD)),
        @AttributeOverride(name = "auditMd", column = @Column(name = TableColumnNames.WAL_AUDIT_MD)),
        @AttributeOverride(name = "auditRd", column = @Column(name = TableColumnNames.WAL_AUDIT_RD))
})
public class WatchListData extends AbstractAuditFields {

    @Id
    @GeneratedValue
    @Column(name = TableColumnNames.WAL_ID, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = TableColumnNames.WAL_USR_ID_FK)
    private UserData user;
    @Column(name = TableColumnNames.WAL_USR_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long userIdFk;

    @OneToOne
    @JoinColumn(name = TableColumnNames.WAL_OFR_ID_FK)
    private OfferData offer;
    @Column(name = TableColumnNames.WAL_OFR_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long offerIdFk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public Long getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Long userIdFk) {
        this.userIdFk = userIdFk;
    }

    public OfferData getOffer() {
        return offer;
    }

    public void setOffer(OfferData offer) {
        this.offer = offer;
    }

    public Long getOfferIdFk() {
        return offerIdFk;
    }

    public void setOfferIdFk(Long offerIdFk) {
        this.offerIdFk = offerIdFk;
    }
}
