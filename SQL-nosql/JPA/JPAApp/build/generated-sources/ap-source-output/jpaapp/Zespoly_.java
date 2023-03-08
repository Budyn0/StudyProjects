package jpaapp;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpaapp.Pracownicy;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-13T13:35:23")
@StaticMetamodel(Zespoly.class)
public class Zespoly_ { 

    public static volatile CollectionAttribute<Zespoly, Pracownicy> pracownicyCollection;
    public static volatile SingularAttribute<Zespoly, Short> idZesp;
    public static volatile SingularAttribute<Zespoly, String> adres;
    public static volatile SingularAttribute<Zespoly, String> nazwa;

}