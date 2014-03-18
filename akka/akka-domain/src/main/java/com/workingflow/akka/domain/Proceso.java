/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workingflow.akka.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.workingflow.common.domain.Persistable;

/**
 * Documentación de {@link Proceso}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
@Entity
@Table(name = "proceso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proceso.findAll", query = "SELECT p FROM Proceso p"),
    @NamedQuery(name = "Proceso.findByIdUsuario", query = "SELECT p FROM Proceso p WHERE p.idUsuario = :idUsuario"),
    @NamedQuery(name = "Proceso.findByDate", query = "SELECT p FROM Proceso p WHERE p.date = :date"),
    @NamedQuery(name = "Proceso.findByFilename", query = "SELECT p FROM Proceso p WHERE p.filename = :filename"),
    @NamedQuery(name = "Proceso.findById", query = "SELECT p FROM Proceso p WHERE p.id = :id")})
public class Proceso implements Persistable<Long> {

    public enum State {

        ACTIVE, COMPLETE, INCOMPLETE, INACTIVE
    }

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
            name = "GenParcelaSequence",
            sequenceName = "proceso_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GenParcelaSequence")
    @Column(name = "id")
    private Long id;

    @Override
    public boolean isNew() {
        return id != null;
    }

    //TODO agregar Owner del proceso

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @JoinColumn(name = "id_tipo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoProceso idTipo;

    @Enumerated(EnumType.STRING)
    private State estado;

    @Column(name = "name")
    private String comando;

    @Column(name = "parameters")
    private String parameters;

    public Proceso() {
    }

    public Proceso(Long id) {
        this.id = id;
    }

    public Proceso(Long id, int idUsuario) {
        this.id = id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProceso getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(TipoProceso idTipo) {
        this.idTipo = idTipo;
    }

    public State getIdEstado() {
        return estado;
    }

    public void setIdEstado(State idEstado) {
        this.estado = idEstado;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
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
        if (!(object instanceof Proceso)) {
            return false;
        }
        Proceso other = (Proceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.workingflow.domain.Proceso[ id=" + id + " ]";
    }

}
