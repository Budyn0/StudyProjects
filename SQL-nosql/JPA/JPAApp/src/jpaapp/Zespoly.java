/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaapp;

import java.io.Serializable;
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
@Table(name = "ZESPOLY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zespoly.findAll", query = "SELECT z FROM Zespoly z"),
    @NamedQuery(name = "Zespoly.findByIdZesp", query = "SELECT z FROM Zespoly z WHERE z.idZesp = :idZesp"),
    @NamedQuery(name = "Zespoly.findByNazwa", query = "SELECT z FROM Zespoly z WHERE z.nazwa = :nazwa"),
    @NamedQuery(name = "Zespoly.findByAdres", query = "SELECT z FROM Zespoly z WHERE z.adres = :adres")})
public class Zespoly implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ZESP")
    private Short idZesp;
    @Column(name = "NAZWA")
    private String nazwa;
    @Column(name = "ADRES")
    private String adres;
    @OneToMany(mappedBy = "idZesp")
    private Collection<Pracownicy> pracownicyCollection;

    public Zespoly() {
    }

    public Zespoly(Short idZesp) {
        this.idZesp = idZesp;
    }

    public Short getIdZesp() {
        return idZesp;
    }

    public void setIdZesp(Short idZesp) {
        this.idZesp = idZesp;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
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
        hash += (idZesp != null ? idZesp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zespoly)) {
            return false;
        }
        Zespoly other = (Zespoly) object;
        if ((this.idZesp == null && other.idZesp != null) || (this.idZesp != null && !this.idZesp.equals(other.idZesp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpaapp.Zespoly[ idZesp=" + idZesp + " ]";
    }
    
}
