package com.gp.barter.exchange.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import com.gp.barter.exchange.util.constants.TableColumnNames;

import javax.persistence.*;

@Entity
@Table(name = "TRANSACTION_PICTURES")
@AttributeOverrides({
        @AttributeOverride(name = "auditCd", column = @Column(name = TableColumnNames.TRP_AUDIT_CD)),
        @AttributeOverride(name = "auditMd", column = @Column(name = TableColumnNames.TRP_AUDIT_MD)),
        @AttributeOverride(name = "auditRd", column = @Column(name = TableColumnNames.TRP_AUDIT_RD))
})
public class TransactionPictureData extends AbstractAuditFields {

    @Id
    @GeneratedValue
    @Column(name = TableColumnNames.TRP_ID, nullable = false)
    private Long id;

    @Lob
    @Column(name = TableColumnNames.TRP_PICTURE, nullable = false)
    private String picture;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = TableColumnNames.TRP_TRN_ID_FK)
    private TransactionData transaction;
    @Column(name = TableColumnNames.TRP_TRN_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long transactionIdFk;

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

    public TransactionData getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionData transaction) {
        this.transaction = transaction;
    }

    public Long getTransactionIdFk() {
        return transactionIdFk;
    }

    public void setTransactionIdFk(Long transactionIdFk) {
        this.transactionIdFk = transactionIdFk;
    }
}
