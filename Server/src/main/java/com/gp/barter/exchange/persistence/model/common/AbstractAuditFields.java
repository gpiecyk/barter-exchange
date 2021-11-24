package com.gp.barter.exchange.persistence.model.common;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class AbstractAuditFields implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    private Date auditCd;

    @Temporal(TemporalType.TIMESTAMP)
    private Date auditMd;

    @Temporal(TemporalType.TIMESTAMP)
    private Date auditRd;

    @PrePersist
    public void onPersist() {
        auditCd = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        auditMd = new Date();
    }

    public Date getAuditCd() {
        return auditCd;
    }

    public void setAuditCd(Date auditCd) {
        this.auditCd = auditCd;
    }

    public Date getAuditMd() {
        return auditMd;
    }

    public void setAuditMd(Date auditMd) {
        this.auditMd = auditMd;
    }

    public Date getAuditRd() {
        return auditRd;
    }

    public void setAuditRd(Date auditRd) {
        this.auditRd = auditRd;
    }
}
