-- User A: Jan Podleski ZAO149195
-- User B: Marta Jasik ZAO147237
-- User C: Patryk Szkudlarek ZAO147712

-- Zad 1
SELECT * FROM ZAO147237.pracownicy;

-- Zad 2
GRANT SELECT ON pracownicy to ZAO149195;

-- Zad 3
SELECT * FROM ZAO147237.pracownicy;

-- Zad 4
GRANT UPDATE(placa_pod, placa_dod) ON pracownicy to ZAO147237;

-- Zad 5
UPDATE ZAO149195.pracownicy SET placa_pod = placa_pod * 2;
UPDATE ZAO149195.pracownicy SET placa_pod = 2000 WHERE nazwisko = 'MORZY';
UPDATE ZAO149195.pracownicy SET placa_dod = 700;
-- operacja się nie udała

-- Zad 6
CREATE SYNONYM prac2 FOR ZAO149195.pracownicy;
UPDATE prac2 SET placa_dod = 800;
COMMIT;

-- Zad 7
SELECT placa_dod FROM prac2;

-- Zad 8
SELECT owner, table_name, grantee, grantor, privilege FROM user_tab_privs;
SELECT table_name, grantee, grantor, privilege FROM user_tab_privs_made;
SELECT owner, table_name, grantor, privilege FROM user_tab_privs_recd;
SELECT owner, table_name, column_name, grantee, grantor, privilege FROM user_col_privs; 
SELECT table_name, column_name, grantee, grantor, privilege FROM user_col_privs_made;
SELECT owner, table_name, column_name, grantor, privilege FROM user_col_privs_recd;

-- Zad 9
REVOKE UPDATE ON pracownicy FROM ZAO147237;

UPDATE ZAO149195.pracownicy SET placa_dod = 700;
UPDATE prac2 SET placa_dod = 800;

-- Zad 10
CREATE ROLE ROLA_147237 IDENTIFIED BY 123;
GRANT SELECT, UPDATE ON pracownicy TO ROLA_147237;

CREATE ROLE ROLA_149195;
GRANT SELECT, UPDATE ON pracownicy TO ROLA_149195;

-- Zad 11
GRANT ROLA_147237 TO ZAO147237;

SELECT * FROM ZAO149195.pracownicy;

-- Zad 12
SET ROLE ROLA_147237 identified BY 123;
SELECT * FROM ZAO147237.pracownicy;

SELECT granted_role, admin_option FROM user_role_privs WHERE username = 'ZAO147237';
SELECT role, owner, table_name, column_name, privilege FROM role_tab_privs;


-- Zad 13
REVOKE ROLA_147237 FROM ZAO147237;

SELECT * FROM ZAO149195.pracownicy;

-- Zad 14
SELECT * FROM ZAO149195.pracownicy;

-- Zad 15
UPDATE ZAO147237.pracownicy SET placa_dod = 800;

-- Zad 16
GRANT ROLA_149195 TO ZAO149195;

UPDATE ZAO147237.pracownicy SET placa_dod = 800;


-- Zad 17
UPDATE ZAO147237.pracownicy SET placa_dod = 800;
COMMIT;

-- Zad 18
REVOKE UPDATE ON pracownicy FROM ROLA_149195;

UPDATE ZAO147237.pracownicy SET placa_dod = 800;

-- Zad 19
DROP ROLE ROLA_147237;

DROP ROLE ROLA_149195;

-- Zad 20
GRANT SELECT ON pracownicy TO ZAO147237 WITH GRANT OPTION;

GRANT SELECT ON ZAO149195.pracownicy TO ZAO147712 WITH GRANT OPTION;

SELECT * FROM ZAO149195.pracownicy;

-- Zad 21
SELECT * FROM user_tab_privs;
SELECT * FROM user_tab_privs_made;
SELECT * FROM user_tab_privs_recd;

-- Zad 22
REVOKE SELECT ON pracownicy FROM ZAO147712;

REVOKE SELECT ON pracownicy FROM ZAO147237;

SELECT * FROM user_tab_privs;
SELECT * FROM user_tab_privs_made;
SELECT * FROM user_tab_privs_recd;

-- Zad 23
CREATE VIEW prac20 AS SELECT nazwisko, placa_pod FROM pracownicy WHERE id_zesp=20;
GRANT all ON prac20 TO ZAO147237;

UPDATE ZAO149195.prac20 SET placa_pod = 300;

-- Zad 24
CREATE OR REPLACE FUNCTION funLiczEtaty RETURN NUMBER IS vNum NUMBER(3);
BEGIN
    SELECT COUNT(*) INTO vNum FROM etaty;
    RETURN vNum;
END;

GRANT EXECUTE ON funLiczEtaty TO ZAO147237;

-- Zad 25
SELECT ZAO149195.funLiczEtaty from DUAL;

SELECT count(*) from ZAO149195.etaty;


-- Zad 26
CREATE OR REPLACE FUNCTION funLiczEtaty RETURN NUMBER AUTHID CURRENT_USER IS vNum NUMBER(3);
BEGIN
    SELECT COUNT(*) INTO vNum FROM etaty;
    RETURN vNum;
END;

GRANT EXECUTE ON funLiczEtaty TO ZAO147237;

-- Zad 27
SELECT ZAO149195.funLiczEtaty FROM dual;
-- wynik nie różni się

-- Zad 28
INSERT INTO etaty (nazwa, placa_min, placa_max) VALUES ('WYKŁADOWCA', 1000, 2000);
COMMIT;

-- Zad 29
SELECT ZAO149195.funLiczEtaty FROM dual;
-- bo była wykonywana na tej samej tabeli

-- Zad 30
CREATE TABLE test(id NUMBER(2) PRIMARY KEY, tekst VARCHAR2(20));
INSERT INTO test VALUES(1,'pierwszy');
INSERT INTO test VALUES(2, 'drugi');

CREATE OR REPLACE PROCEDURE procPokazTest AUTHID CURRENT_USER IS
BEGIN
    for vTekst IN (SELECT tekst FROM test) LOOP
        DBMS_OUTPUT.PUT_LINE (vTekst.tekst);
    END LOOP;
END procPokazTest;

GRANT EXECUTE ON procPokazTest TO ZAO149195;
GRANT SELECT ON test TO ZAO149195;

-- Zad 31
EXEC ZAO147237.procPokazTest;

CREATE OR REPLACE PROCEDURE procPokazTest AUTHID CURRENT_USER IS
BEGIN
    for vTekst IN (SELECT tekst FROM ZAO147237.test) LOOP
        DBMS_OUTPUT.PUT_LINE (vTekst.tekst);
    END LOOP;
END procPokazTest;

GRANT EXECUTE ON procPokazTest TO ZAO149195;
GRANT SELECT ON test TO ZAO149195;

EXEC ZAO147237.procPokazTest;

-- Zad 32
CREATE TABLE info_dla_znajomych (NAZWA VARCHAR2(20) PRIMARY KEY, INFO VARCHAR2(200) NOT NULL);
INSERT INTO info_dla_znajomych (NAZWA, INFO) VALUES ('ZAO147237', 'text');
INSERT INTO info_dla_znajomych (NAZWA, INFO) VALUES ('ZAO149195', 'message');

CREATE VIEW info4u as SELECT * FROM info_dla_znajomych WHERE nazwa = User;

GRANT SELECT ON info4u to ZAO147237;

SELECT * FROM ZAO147237.info4u;


