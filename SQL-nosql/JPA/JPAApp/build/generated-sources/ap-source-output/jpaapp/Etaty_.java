package jpaapp;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpaapp.Pracownicy;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-13T13:35:23")
@StaticMetamodel(Etaty.class)
public class Etaty_ { 

    public static volatile SingularAttribute<Etaty, BigDecimal> placaMin;
    public static volatile CollectionAttribute<Etaty, Pracownicy> pracownicyCollection;
    public static volatile SingularAttribute<Etaty, BigDecimal> placaMax;
    public static volatile SingularAttribute<Etaty, String> nazwa;

}