package com.gp.barter.exchange.persistence.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import com.gp.barter.exchange.util.constants.TableColumnNames;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TRANSACTIONS")
@AttributeOverrides({
        @AttributeOverride(name = "auditCd", column = @Column(name = TableColumnNames.TRN_AUDIT_CD)),
        @AttributeOverride(name = "auditMd", column = @Column(name = TableColumnNames.TRN_AUDIT_MD)),
        @AttributeOverride(name = "auditRd", column = @Column(name = TableColumnNames.TRN_AUDIT_RD))
})
public class TransactionData extends AbstractAuditFields {

    @Id
    @GeneratedValue
    @Column(name = TableColumnNames.TRN_ID, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = TableColumnNames.TRN_OFFERING_USER_ID_FK)
    private UserData offeringUser;
    @Column(name = TableColumnNames.TRN_OFFERING_USER_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long offeringUserFkId;

    @OneToOne
    @JoinColumn(name = TableColumnNames.TRN_PUBLISHER_USER_ID_FK)
    private UserData publisher;
    @Column(name = TableColumnNames.TRN_PUBLISHER_USER_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long publisherFkId;

    @Column(name = TableColumnNames.TRN_MESSAGE, nullable = false, length = 1000)
    private String message;

    @Column(name = TableColumnNames.TRN_ITEMS_FOR_EXCHANGE, nullable = false, length = 500)
    private String itemsForExchange;

    @Column(name = TableColumnNames.TRN_STATUS, nullable = false, length = 1)
    private String status;

    @OneToOne
    @JoinColumn(name = TableColumnNames.TRN_OFR_ID_FK)
    private OfferData offer;
    @Column(name = TableColumnNames.TRN_OFR_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long offerIdFk;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaction", fetch = FetchType.EAGER)
    private Set<TransactionPictureData> pictures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getOfferingUser() {
        return offeringUser;
    }

    public void setOfferingUser(UserData offeringUser) {
        this.offeringUser = offeringUser;
    }

    public Long getOfferingUserFkId() {
        return offeringUserFkId;
    }

    public void setOfferingUserFkId(Long offeringUserFkId) {
        this.offeringUserFkId = offeringUserFkId;
    }

    public UserData getPublisher() {
        return publisher;
    }

    public void setPublisher(UserData publisher) {
        this.publisher = publisher;
    }

    public Long getPublisherFkId() {
        return publisherFkId;
    }

    public void setPublisherFkId(Long publisherFkId) {
        this.publisherFkId = publisherFkId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemsForExchange() {
        return itemsForExchange;
    }

    public void setItemsForExchange(String itemsForExchange) {
        this.itemsForExchange = itemsForExchange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Set<TransactionPictureData> getPictures() {
        return pictures;
    }

    public void setPictures(Set<TransactionPictureData> pictures) {
        this.pictures = pictures;
    }
}
