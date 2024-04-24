drop table engenheiro;

create table engenheiro(
    id_engenheiro number(4)	PRIMARY KEY,
    nome varchar2 (50)	NOT NULL,
    sexo char(1) NOT NULL,
    cpf_engenheiro varchar2 (11) NOT NULL UNIQUE,
    fone varchar2 (11) NOT NULL
);

INSERT INTO engenheiro VALUES(1,'RICARDO','M','13789993315','1981302034');
INSERT INTO engenheiro VALUES(2,'CLARA','F','12222646585','1732446784');
INSERT INTO engenheiro VALUES(3,'RODRIGO','M','16789663215','2164938324');
INSERT INTO engenheiro VALUES(4,'FELIPE','M','12033335687','1232446784');
INSERT INTO engenheiro VALUES(5,'LUISA','F','11789777214','1933336784');
INSERT INTO engenheiro VALUES(6,'LAURA','F','13389647815','1932550099');
INSERT INTO engenheiro VALUES(7,'FABIO','M','10313456778','1933440332');
INSERT INTO engenheiro VALUES(8,'HELENA','F','10789643215','1522367532');
INSERT INTO engenheiro VALUES(9,'LUIZA','F','15786643335','1232446787');
INSERT INTO engenheiro VALUES(10,'PAULO','M','10333643518','1532315351');
INSERT INTO engenheiro VALUES(11,'PEDRO','M','10375898854','1932555567');
INSERT INTO engenheiro VALUES(12,'LUIZA','F','13456789643','1532445361');
INSERT INTO engenheiro VALUES(13,'MIGUEL','M','13627890643','1932748896');
INSERT INTO engenheiro VALUES(14,'JULIA','F','14446439210','1932114509');
INSERT INTO engenheiro VALUES(15,'RENATO','M','10733211556','1932433455');
INSERT INTO engenheiro VALUES(16,'CLARA','F','10988375542','1932546678');
INSERT INTO engenheiro VALUES(17,'JULIANO','M','10222111215','199346-1012');
select * from engenheiro;

update engenheiro SET nome = 'Ricardo Silva' where nome = 'RICARDO';
update engenheiro SET nome = 'Juliano Rocha' where nome = 'JULIANO';

delete from engenheiro where cpf_engenheiro = '12033335687';

select id_engenheiro, nome, fone from engenheiro;

select * from engenheiro where nome = 'MIGUEL';
select nome from engenheiro where sexo = 'F';

select nome from engenheiro ORDER BY nome ASC;

update engenheiro SET nome = 'Joana' where id_engenheiro = 2;

select fone as "telefone" FROM engenheiro;