

SELECT V.*
FROM VAGA V
RIGHT JOIN SOLICITA S ON S.ID_VAGA = V.ID_VAGA
AND V.ID_VAGA = 1;


SELECT V.*
FROM VAGA V, SOLICITA S
WHERE V.ID_VAGA = S.ID_VAGA;

SELECT column_name(s)
FROM table1
LEFT JOIN table2 ON table1.column_name = table2.column_name;

--drop table usuario;
--drop table credito;
--drop table compra;
--drop table venda;

--drop SEQUENCE credito_seq;
--drop SEQUENCE credito_seq;
--drop SEQUENCE compra_seq;
--drop SEQUENCE venda_seq;

CREATE TABLE usuario
(
    id_usuario number not null,
    email varchar2(255) not null,
    senha varchar2(12) not null,
    tipo varchar2(8) not null,
    CONSTRAINT usuario_pk PRIMARY KEY (id_usuario)
);

create sequence usuario_seq
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

------------------------

CREATE TABLE credito
(
    id_credito number not null,
    vl_credito number(4,2) not null,
    dt_criacao date not null,
    CONSTRAINT credito_pk PRIMARY KEY (id_credito)
);


create sequence credito_seq
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

-------------

CREATE TABLE compra
(
  id_compra  number not null,
  id_credito number not null,
  id_usuario number not null,
  qt_credito number not null,
  dt_compra  date not null,
  CONSTRAINT compra_pk PRIMARY KEY (id_compra),
  CONSTRAINT compra_credito_fk FOREIGN KEY (id_credito) REFERENCES credito(id_credito),
  CONSTRAINT compra_usuario_fk FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

create sequence compra_seq
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

-----------------------------------

CREATE TABLE venda
(
  id_venda  number not null,
  id_credito number not null,
  id_usuario number not null,
  qt_credito number not null,
  dt_venda   date not null,
  CONSTRAINT venda_pk PRIMARY KEY (id_venda),
  CONSTRAINT venda_credito_fk FOREIGN KEY (id_credito) REFERENCES credito(id_credito),
  CONSTRAINT venda_usuario_fk FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

create sequence venda_seq
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

SELECT
    u.id_usuario,
    u.email,
    u.senha,
    u.tipo
FROM
    usuario u;
insert into usuario values (1,'silvio.cicoti@gmail.com','lunaduna','admin');

select * from credito;
insert into credito values (1,2.55,sysdate);

select * from compra;
insert into compra values (1,1,1,10,sysdate);
insert into compra values (2,1,1,2,sysdate);

SELECT
    SUM(c.qt_credito)
FROM
    compra c,
    credito cr
WHERE
        c.id_credito = cr.id_credito
    AND
        c.id_usuario = 1;

select to_char(dt_venda,'DD/MM/YYYY HH24:MI:ss') from venda;

select dt_venda from venda;

-----------------------------------------------

CREATE TABLE vaga
(
    id_vaga number not null,
    no_zona varchar2(1) not null,
    no_vaga varchar2(3) not null,
    nr_longitude number not null,
    nr_latitude number not null,
    CONSTRAINT vaga_pk PRIMARY KEY (id_vaga)
);

create sequence vaga_seq
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

select * from vaga;
insert into vaga values (1,'A','253',12.32343434,27.453453454);
insert into vaga values (2,'A','254',12.32343434,27.453453454);
insert into vaga values (3,'B','110',12.32343434,27.453453454);
--------------------------
CREATE TABLE placa
(
    id_placa number not null,
    id_usuario number not null,
    nm_placa varchar2(8) not null,
    dt_cadastro date not null,
    CONSTRAINT placa_pk PRIMARY KEY (id_placa),
    CONSTRAINT placa_usuario_fk FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

create sequence placa_seq
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

select * from placa;

insert into placa values (1,1,'AAA-9999',sysdate);
insert into placa values (2,1,'BBB-9999',sysdate);
---------------------------------------------------

INSERT INTO SOLICITA VALUES (1,1,1,1,1,SYSDATE,SYSDATE,0,0,'TESTE');
DELETE FROM SOLICITA;

CREATE TABLE solicita
(
  id_solicita  number not null,
  id_usuario number not null,
  id_venda number not null,
  id_vaga number not null,
  id_placa number not null,
  dt_inicio_periodo date not null,
  dt_fim_periodo date not null,
  bl_extensao number(1) null check (bl_extensao in (0,1)),
  bl_negado number(1) null check (bl_negado in (0,1)),
  ds_motivo varchar2(255) null,
  CONSTRAINT solicita_pk PRIMARY KEY (id_solicita),
  CONSTRAINT solicita_usuario_fk FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  CONSTRAINT solicita_venda_fk FOREIGN KEY (id_venda) REFERENCES venda(id_venda),
  CONSTRAINT solicita_vaga_fk FOREIGN KEY (id_vaga) REFERENCES vaga(id_vaga),
  CONSTRAINT solicita_placa_fk FOREIGN KEY (id_placa) REFERENCES placa(id_placa)
);

create sequence solicita_seq
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;