package com.workingflow.common.domain.jpa.auditory;

import com.workingflow.common.domain.jpa.JpaPersistable;
import com.workingflow.common.domain.auditory.Auditable;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Implementación de {@link JpaPersistable} con capacidad de ser auditada.
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 */
@MappedSuperclass
public abstract class JpaAuditable<S, ID extends Serializable> extends JpaPersistable<ID> implements
        Auditable<S, ID> {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    @JoinColumn(name = "created_by_id", updatable = false)
    @JsonIgnore
    private S createdBy;
    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdDate;
    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    @JsonIgnore
    private S updatedBy;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedDate;

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public S getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(S createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Date getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public S getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(S updatedBy) {
        this.updatedBy = updatedBy;
    }
}
