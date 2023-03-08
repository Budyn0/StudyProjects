package jpaapp;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpaapp.Etaty;
import jpaapp.Pracownicy;
import jpaapp.Zespoly;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-13T13:35:23")
@StaticMetamodel(Pracownicy.class)
public class Pracownicy_ { 

    public static volatile SingularAttribute<Pracownicy, Pracownicy> idSzefa;
    public static volatile CollectionAttribute<Pracownicy, Pracownicy> pracownicyCollection;
    public static volatile SingularAttribute<Pracownicy, String> nazwisko;
    public static volatile SingularAttribute<Pracownicy, BigDecimal> placaPod;
    public static volatile SingularAttribute<Pracownicy, Integer> idPrac;
    public static volatile SingularAttribute<Pracownicy, Zespoly> idZesp;
    public static volatile SingularAttribute<Pracownicy, BigDecimal> placaDod;
    public static volatile SingularAttribute<Pracownicy, Date> zatrudniony;
    public static volatile SingularAttribute<Pracownicy, Etaty> etat;

}