package com.gp.barter.exchange.persistence.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import com.gp.barter.exchange.util.constants.TableColumnNames;

import javax.persistence.*;

@Entity
@Table(name = "PICTURES")
@AttributeOverrides({
        @AttributeOverride(name = "auditCd", column = @Column(name = TableColumnNames.PIC_AUDIT_CD)),
        @AttributeOverride(name = "auditMd", column = @Column(name = TableColumnNames.PIC_AUDIT_MD)),
        @AttributeOverride(name = "auditRd", column = @Column(name = TableColumnNames.PIC_AUDIT_RD))
})
public class PictureData extends AbstractAuditFields {

    @Id
    @GeneratedValue
    @Column(name = TableColumnNames.PIC_ID, nullable = false)
    private Long id;

    @Lob
    @Column(name = TableColumnNames.PIC_PICTURE, nullable = false)
    private String picture;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = TableColumnNames.PIC_OFR_ID_FK)
    private OfferData offer;
    @Column(name = TableColumnNames.PIC_OFR_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long offerIdFk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
