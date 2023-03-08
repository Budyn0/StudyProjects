/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaapp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author JanPodleski
 */
public class JPAApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAAppPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //Ćwiczenie 5
        Zespol team = entityManager.find(Zespol.class, 10);
        System.out.println(team.getIdZesp());
        System.out.println(team.getNazwa());
        System.out.println(team.getAdres());
        Query query = entityManager.createQuery("SELECT t FROM Zespol t ORDER BY t.nazwa");
        List<Zespol> teams = query.getResultList();
        for (Zespol team1 : teams) {
            System.out.println(team1.getIdZesp() + " " + team1.getNazwa() + " " + team1.getAdres());
        }
        List<Zespol> teams1 = entityManager.createNamedQuery("Zespol.findAll", Zespol.class).getResultList();
        for (Zespol team2 : teams1) {
            System.out.println("ID_ZESP: " + team2.getIdZesp() + ", nazwa: " + team2.getNazwa() + ", adres: " + team2.getAdres());
        }
        System.out.println("");
        //Ćwiczenie 6.1 
        entityManager.getTransaction().begin();
        Zespol team2 = new Zespol();
         team2.setIdZesp(70);
        team2.setNazwa("Zespół Testowy");
        team2.setAdres("Kutrzeby");
        entityManager.persist(team2);
        entityManager.getTransaction().commit();
        Query query1 = entityManager.createNamedQuery("Zespol.findAll");
        List<Zespol> teams2 = query1.getResultList();
        for (Zespol team2 : teams2) {
            System.out.println(team2.getIdZesp() + " " + team2.getNazwa() + " " + team2.getAdres());
        }
        //6.2
        //create sequence SEQ_JPA_ZESPOLY start with 71 increment by 1;
        //6.3
        entityManager.getTransaction().begin();
        Zespol team1 = new Zespol();
        team1.setNazwa("Test Sekwencji");
        entityManager.persist(team1);
        Zespol team2 = new Zespol();
        team2.setNazwa("Zespół Testowy");
        entityManager.persist(team2);
        entityManager.getTransaction().commit();
        List<Zespol> teams = entityManager.createNamedQuery("Zespol.findAll", Zespol.class).getResultList();
        for (Zespol team : teams) {
            System.out.println("ID_ZESP: " + team.getIdZesp() + ", nazwa: " + team.getNazwa() + ", adres: " + team.getAdres());
        }
        //6.4
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("DELETE FROM Zespol z WHERE z.nazwa = :nazwa");
        query.setParameter("nazwa", "Test Sekwencji");
        int deleted = query.executeUpdate();
        System.out.println("Zespół \"Test Sekwencji\" został usunięty!");
        query = entityManager.createQuery("DELETE FROM Zespol z WHERE z.nazwa = :nazwa");
        query.setParameter("nazwa", "Zespół Testowy");
        deleted = query.executeUpdate();
        System.out.println("Zespół \"Zespół Testowy\" został usunięty!");
        entityManager.getTransaction().commit();
         List<Zespol> teams2 = entityManager.createNamedQuery("Zespol.findAll", Zespol.class).getResultList();
        for (Zespol team : teams2) {
        System.out.println("ID_ZESP: " + team.getIdZesp() + ", nazwa: " + team.getNazwa() + ", adres: " + team.getAdres());
        }
        
        //7.3
        // Wyświetlenie listy pracowników
        entityManager.getTransaction().begin();
        List<Pracownicy> pracownicy = entityManager.createNamedQuery("Pracownicy.findAll", Pracownicy.class).getResultList();
        System.out.println("Lista pracowników:");
        for (Pracownicy p : pracownicy) {
            System.out.println("Imię: " + p.getNazwisko() + ", nazwisko: " + p.getNazwisko() + ", płaca podstawowa: " + p.getPlacaPod() + ", zespół: " + p.getIdZesp());
        }

        // Dodanie pracownika
        Pracownicy nowyPracownik = new Pracownicy();
        nowyPracownik.setNazwisko("Jan");
        nowyPracownik.setNazwisko("Podleski");
        nowyPracownik.setPlacaPod(new BigDecimal(4000));
        System.out.println("Dodano nowego pracownika: " + nowyPracownik.getNazwisko() + " " + nowyPracownik.getNazwisko());

        // Podwyżka
        BigDecimal prog = new BigDecimal(3000);
        for (Pracownicy p : pracownicy) {
            if (p.getPlacaPod() != null) {
                if (p.getPlacaPod().compareTo(prog) < 0) {
                    p.setPlacaPod(p.getPlacaPod().multiply(new BigDecimal(1.1)));
                }
            }
        }
        System.out.println("Zakończono podwyżkę");

        List<Zespoly> zespolyy = entityManager.createNamedQuery("Zespoly.findAll", Zespoly.class).getResultList();

        //7.4
        for (Zespoly zespol : zespolyy) {
            System.out.println("Nazwa Zespolu: " + zespol.getNazwa());
            for (Pracownicy pracZespol : pracownicy) {
                if (pracZespol.getIdZesp().getIdZesp() == zespol.getIdZesp()) {
                    System.out.println("Imię: " + pracZespol.getNazwisko() + ", nazwisko: " + pracZespol.getNazwisko() + ", płaca podstawowa: " + pracZespol.getPlacaPod() + ", zespół: " + pracZespol.getIdZesp());
                }
            }
            System.out.println("");

        }
        entityManager.close();
        entityManagerFactory.close();
    }
}
