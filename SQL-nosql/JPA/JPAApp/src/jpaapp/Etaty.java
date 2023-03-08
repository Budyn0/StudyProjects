/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaapp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author podly
 */
@Entity
@Table(name = "ETATY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Etaty.findAll", query = "SELECT e FROM Etaty e"),
    @NamedQuery(name = "Etaty.findByNazwa", query = "SELECT e FROM Etaty e WHERE e.nazwa = :nazwa"),
    @NamedQuery(name = "Etaty.findByPlacaMin", query = "SELECT e FROM Etaty e WHERE e.placaMin = :placaMin"),
    @NamedQuery(name = "Etaty.findByPlacaMax", query = "SELECT e FROM Etaty e WHERE e.placaMax = :placaMax")})
public class Etaty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NAZWA")
    private String nazwa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PLACA_MIN")
    private BigDecimal placaMin;
    @Column(name = "PLACA_MAX")
    private BigDecimal placaMax;
    @OneToMany(mappedBy = "etat")
    private Collection<Pracownicy> pracownicyCollection;

    public Etaty() {
    }

    public Etaty(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public BigDecimal getPlacaMin() {
        return placaMin;
    }

    public void setPlacaMin(BigDecimal placaMin) {
        this.placaMin = placaMin;
    }

    public BigDecimal getPlacaMax() {
        return placaMax;
    }

    public void setPlacaMax(BigDecimal placaMax) {
        this.placaMax = placaMax;
    }

    @XmlTransient
    public Collection<Pracownicy> getPracownicyCollection() {
        return pracownicyCollection;
    }

    public void setPracownicyCollection(Collection<Pracownicy> pracownicyCollection) {
        this.pracownicyCollection = pracownicyCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nazwa != null ? nazwa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Etaty)) {
            return false;
        }
        Etaty other = (Etaty) object;
        if ((this.nazwa == null && other.nazwa != null) || (this.nazwa != null && !this.nazwa.equals(other.nazwa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpaapp.Etaty[ nazwa=" + nazwa + " ]";
    }
    
}
