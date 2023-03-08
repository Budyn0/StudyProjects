package lab_jdbc;

import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lab_JDBC {


    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        Connection conn = null;
        String connectionString =
                "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/" +
                        "dblab02_students.cs.put.poznan.pl";
        Properties connectionProps = new Properties();
        connectionProps.put("user", "zao149195");
        connectionProps.put("password", "zao149195");

        try {
            conn = DriverManager.getConnection(connectionString,connectionProps);
            System.out.println("Połączono z bazą danych");
        } catch(SQLException ex) {
            Logger.getLogger(Lab_JDBC.class.getName()).log(Level.SEVERE, "Nie udało się połączyć z bazą danych", ex);
            System.exit(-1);
        }

        dodaj2000(conn);
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Lab_JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Odłączono się od bazy danych");
    }

    public static void zatrudnienieInfo(ConnectionC conn) {
        int count = 0;

        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(
                "select count(*) from pracownicy");) {

            rs.next();
            count = rs.getInt(1);

        } catch (SQLException ex) {
            System.out.println("Błąd wykonania polecenia: " + ex.getMessage());
        }

        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(
                "select nazwisko, z.nazwa from pracownicy p join zespoly z on p.id_zesp=z.id_zesp");) {
            System.out.println("Zatrudniono " + count + " pracowników, w tym:");
            while (rs.next()) {
                String pracownik = rs.getString(1);
                String zespol = rs.getString(2);

                System.out.println("  " + pracownik + " pracuje w " + zespol);
            }
        } catch (SQLException ex) {
            System.out.println("Błąd wykonania polecenia: " + ex.getMessage());
        }
    }

    private static void sprawdzAsystentow(Connection conn) {
        try(Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(
                    "select nazwisko, placa_pod+COALESCE(placa_dod, 0) as placa" +
                            " from pracownicy where etat = 'ASYSTENT'" +
                            "order by placa desc"
            );
        ) {
            while(rs.next()){
                System.out.println(rs.getString(1));
            }
            rs.afterLast();
            if (rs.previous()) {
                System.out.println("Najmniej zarabia " +
                        rs.getString(1) + ", zarabia " + rs.getInt(2));
            }

            rs.absolute(2);
            if (rs.next()) {
                System.out.println("Trzeci asystent to " + rs.getString(1) +
                        ", zarabia " + rs.getInt(2));
            }

            rs.absolute(-3);
            if(rs.next()) {
                System.out.println("Przedostatni asystent to " + rs.getString(1) +
                        ", zarabia " + rs.getInt(2));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private static void zwolnieniaZatrudnienia(Connection conn) {
        int [] zwolnienia={150, 200, 230};
        String z = Arrays.toString(zwolnienia).replace('[', '(').replace(']', ')');
        String [] zatrudnienia={"Kandefer", "Rygiel", "Boczar"};

        try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ) {
            int changes = stmt.executeUpdate(
                    "delete from pracownicy where id_prac in " + z
            );
            System.out.println("Usunieto " + changes + " krotek.");

            int zatrudniono = 0;
            for (int i = 0; i < zatrudnienia.length; i++) {
                zatrudniono += stmt.executeUpdate("INSERT INTO " +
                        "pracownicy(id_prac,nazwisko) " +
                        "select get_id.nextval, '" + zatrudnienia[i] + "' from dual");
            }
            System.out.println("Wstawiono " + zatrudniono + " krotek.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void etatyTransakcje(Connection conn) {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
             Statement stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
        ) {
            ResultSet rs = stmt1.executeQuery("select * from etaty");
            System.out.println("PRZED INSERTEM");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getInt(3));
            }

            int changes = stmt2.executeUpdate(
                    "insert into etaty(nazwa, placa_min, placa_max)" +
                            "VALUES('DOKTOR', 1000, 2000)"
            );

            System.out.println("PO INSERCIE");
            rs = stmt1.executeQuery("select * from etaty");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getInt(3));
            }

            conn.rollback();

            System.out.println("WYCOFANO TRANSAKCJE");
            rs = stmt1.executeQuery("select * from etaty");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getInt(3));
            }

            changes = stmt2.executeUpdate(
                    "insert into etaty(nazwa, placa_min, placa_max)" +
                            "VALUES('DOKTOR', 1000, 2000)"
            );
            conn.commit();

            System.out.println("POTWIERDZONO TRANSAKCJE");
            rs = stmt1.executeQuery("select * from etaty");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getInt(3));
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private static void przedPolecenia(Connection conn) {
        String [] nazwiska={"Woźniak", "Dąbrowski", "Kozłowski"};
        int [] place={1300, 1700, 1500};
        String []etaty={"ASYSTENT", "PROFESOR", "ADIUNKT"};

        try (PreparedStatement pstmt = conn.prepareStatement(
                "insert into pracownicy(id_prac, nazwisko, placa_pod, etat)" +
                        "VALUES(?, ?, ?, ?)");
             Statement stmt = conn.createStatement();
        ) {
            for (int i = 0; i < nazwiska.length; i++){
                System.out.println(i);
                ResultSet rs_id = stmt.executeQuery("select get_id.nextval from dual");
                if (rs_id.next()) {
                    pstmt.setInt(1, rs_id.getInt(1));
                    pstmt.setString(2, nazwiska[i]);
                    pstmt.setInt(3, place[i]);
                    pstmt.setString(4, etaty[i]);
                    int changes = pstmt.executeUpdate();
                    System.out.println("Dodano " + changes + " pracownika " + nazwiska[i]);
                }
                rs_id.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private static void dodanie2k(Connection conn) {
        try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);) {
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // Wsadowe
        try (PreparedStatement pstmt = conn.prepareStatement("insert into pracownicy(id_prac, nazwisko)" +
                "values(?, ?)");) {
            long start = System.nanoTime();
            for (int i = 0; i < 2000; i++) {
                pstmt.setInt(1, 4000+i);
                pstmt.setString(2, "testpracwsad");
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            long czas = System.nanoTime() - start;
            System.out.println(czas); // wynik: 163066300

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private static void funkcja(Connection conn) {
        try (CallableStatement stmt = conn.prepareCall(
                "{? = call LastName(?, ?)}")){
            stmt.setInt(2, 100);
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.registerOutParameter(3, Types.VARCHAR);

            stmt.execute();
            int success = stmt.getInt(1);
            if (success == 1){
                String result = stmt.getString(3);
                System.out.println(result);
            }else if (success == 0){
                System.out.println("Złe ID pracownika!");
            }
        }catch (SQLException ex) {
            System.out.println("Błąd wykonania polecenia: " + ex.getMessage());
        }
    }
}