/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workingflow.akka.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.workingflow.common.domain.Persistable;

/**
 * Documentación de {@link TipoProceso}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
@Entity
@Table(name = "tipo_proceso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoProceso.findAll", query = "SELECT t FROM TipoProceso t"),
    @NamedQuery(name = "TipoProceso.findById", query = "SELECT t FROM TipoProceso t WHERE t.id = :id"),
    @NamedQuery(name = "TipoProceso.findByName", query = "SELECT t FROM TipoProceso t WHERE t.name = :name"),
    @NamedQuery(name = "TipoProceso.findByDescription", query = "SELECT t FROM TipoProceso t WHERE t.description = :description"),
    @NamedQuery(name = "TipoProceso.findByPrefixfilename", query = "SELECT t FROM TipoProceso t WHERE t.prefixfilename = :prefixfilename")})
public class TipoProceso implements Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
            name = "GenParcelaSequence",
            sequenceName = "TipoProceso_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GenParcelaSequence")
    @Column(name = "id")
    private Long id;

    @Size(max = 2147483647)
    @Column(name = "name")
    private String name;
    
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipo")
    private List<Proceso> procesoList;

    public TipoProceso() {
    }

    public TipoProceso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    @JsonIgnore
    public List<Proceso> getProcesoList() {
        return procesoList;
    }

    public void setProcesoList(List<Proceso> procesoList) {
        this.procesoList = procesoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoProceso)) {
            return false;
        }
        TipoProceso other = (TipoProceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.workingflow.core.domain.TipoProceso[ id=" + id + " ]";
    }

    @Override
    public boolean isNew() {
        return id != null;
    }

}
