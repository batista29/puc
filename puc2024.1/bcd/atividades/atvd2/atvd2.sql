DROP TABLE livros;

CREATE TABLE livros(
    livro_id INTEGER PRIMARY KEY,
    titulo varchar2(255),
    autor varchar2(255),
    editora varchar2(255),
    ano_publicacao INTEGER,
    categoria varchar2(255),
    quantidade_exemplares INTEGER
);

SELECT * FROM livros where titulo = 'Dom Quixote';
SELECT * FROM livros WHERE ano_publicacao > 1900;
SELECT * FROM livros WHERE ano_publicacao BETWEEN 1830 AND 1910;
SELECT * FROM livros ORDER BY titulo DESC;
SELECT * FROM livros WHERE titulo LIKE '%Castigo%';
SELECT * FROM livros;

INSERT INTO livros VALUES(1,'O Hobbit',	'J.R.R. Tolkien',	'Martins Fontes',	'1937',	'Fantasia',	8);
INSERT INTO livros VALUES(2,'O Pícaro',	'Charles Dickens',	'Penguin Classics',	'1838',	'Clássico',	7);
INSERT INTO livros VALUES(3,'Os Miseráveis',	'Victor Hugo',	'Bertrand Brasil',	'1862'	,'Drama',	20);
INSERT INTO livros VALUES(4,'Dom Quixote',	'Miguel de Cervantes',	'Editora Nova Fronteira',	'1605',	'Clássico',	9);
INSERT INTO livros VALUES(5,'O Crime e o Castigo'	,'Fiódor Dostoiévski'	,'Editora 34'	,'1866',	'Drama Psicológico',	11);

CREATE TABLE autor(
    autor_id INTEGER PRIMARY KEY,
    nome varchar2(255),
    nacionalidade varchar2(255),
    data_nascimento date
);

INSERT INTO autor VALUES(1,'J.R.R. Tolkien','Reino Unido','10/03/1973');
INSERT INTO autor VALUES(2,'Charles Dickens','Reino Unido','08/02/2001');
INSERT INTO autor VALUES(3,'Victor Hugo',	'Reino Unido',	'29/10/1950');
INSERT INTO autor VALUES(4,'Miguel de Cervantes','França','01/01/1885');
INSERT INTO autor VALUES(5,'Fiódor Dostoiévski','Espanha','20/05/1616');

drop table emprestimos;
CREATE TABLE emprestimos(
	emprestimo_id INTEGER PRIMARY KEY,
    livro_id INTEGER,
	data_emprestimo date,
	data_devolucao_prevista date,
	data_devolucao_real date,
	usuario_id INTEGER
);

ALTER TABLE emprestimos ADD CONSTRAINT lv_livros_id FOREIGN KEY (livro_id) REFERENCES livros (livro_id);

INSERT INTO emprestimos VALUES(1,1,'23-04-2024','07-05-2024','',1);
INSERT INTO emprestimos VALUES(2,3,'15-02-2024','14-05-2024','',2);
INSERT INTO emprestimos VALUES(3,5,'10-01-2024','27-04-2024','20-04-2024',3);
INSERT INTO emprestimos VALUES(4,4,'01-04-2024','30-04-2024','',4);
INSERT INTO emprestimos VALUES(5,2,'25-03-2024','22-04-2024','',5);

UPDATE emprestimos SET data_devolucao_real = '24/04/2024 ' WHERE emprestimo_id = 5;

select * from emprestimos;
select * from emprestimos where data_emprestimo = '' or data_emprestimo = null;

ALTER TABLE emprestimos DROP COLUMN usuario_id;