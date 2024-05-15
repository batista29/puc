CREATE TABLE PACIENTES(
    id_paciente INTEGER PRIMARY KEY,
	nome_paciente VARCHAR(255),
	tipo_animal VARCHAR(255),
	data_nascimento DATE,
	tutor VARCHAR(255),
	telefone VARCHAR(255)
);

INSERT INTO PACIENTES VALUES (107,'João Silva'	,'Cachorro'	,'01/01/2010',	'Maria Oliveira'	,'(11)9999-9999');
INSERT INTO PACIENTES VALUES(108,	'Ana Costa',	'Gato',	'05/03/2015','Pedro Souza',	'(12)8888-8888');
INSERT INTO PACIENTES VALUES(109,	'Carlos Oliveira'	,'Coelho'	,'12/07/2018','Carla Santos	','(13)7777-7777');
INSERT INTO PACIENTES VALUES(110,	'Sofia Mendes',	'Hamster'	,'14/11/2021',	'Diego Mendes',	'(14)6666-6666');
INSERT INTO PACIENTES VALUES(111,	'Bruno Santos'	,'Cachorro'	,'16/02/2012'	,'Isabela Costa',	'(15)5555-5555');
INSERT INTO PACIENTES VALUES(112,	'Laura Oliveira',	'Gato',	'18/04/2019'	,'Felipe Silva',	'(16)4444-4444');

DROP TABLE PACIENTES;

CREATE TABLE ANIMAIS(
    id_animal INTEGER PRIMARY KEY,
    id_paciente INTEGER,
	nome_animal VARCHAR(255),
    especie VARCHAR(255),
	raca VARCHAR(255),
    sexo VARCHAR(255)
);

ALTER TABLE ANIMAIS ADD CONSTRAINT fk_id_paciente FOREIGN KEY (id_paciente) REFERENCES PACIENTES(id_paciente);

INSERT INTO ANIMAIS VALUES (1,107,'Rex','Cachorro','Golden Retriever','Macho');
INSERT INTO ANIMAIS VALUES (2,108,'Mia','Gato','Siamesa','Fêmea');
INSERT INTO ANIMAIS VALUES (3,109,'Bunitinho','Coelho','Angorá','Macho');
INSERT INTO ANIMAIS VALUES (4,110,'Hammy','Hamster','Sírio Dourado','Macho');
INSERT INTO ANIMAIS VALUES (5,111,'Max','Cachorro','Labrador','Macho');
INSERT INTO ANIMAIS VALUES (6,112,'Luna','Gato','Persa','Fêmea');

describe animais;

ALTER TABLE ANIMAIS ADD idade INTEGER;

DROP TABLE ANIMAIS;

CREATE TABLE CONSULTAS (
	id_consulta INTEGER PRIMARY KEY,
    id_animal INTEGER,
	data_consulta DATE,
	hora_consulta VARCHAR(255),
    veterinario VARCHAR(255),
	descricao VARCHAR(255)
);

ALTER TABLE CONSULTAS ADD CONSTRAINT fk_id_animal FOREIGN KEY (id_animal) REFERENCES ANIMAIS(id_animal);

INSERT INTO CONSULTAS VALUES(1,1,'19/05/2024','10:00','Dra. Ana Souza1','Vacinação anual');
INSERT INTO CONSULTAS VALUES(2,2,'20/05/2024',	'14:00','Dr. Carlos Santos',	'Vacinação anual');
INSERT INTO CONSULTAS VALUES(3,3,'21/05/2024',	'09:00','Dra. Ana Souza1',	'Vacinação anual');
INSERT INTO CONSULTAS VALUES(4,4,'22/05/2024',	'11:30','Dr. Carlos Santos',	'Vacinação anual');
INSERT INTO CONSULTAS VALUES(5,5,'23/05/2024',	'15:00','Dra. Ana Souza',	'Vacinação anual');
INSERT INTO CONSULTAS VALUES(6,6,'24/05/2024',	'10:30','Dr. Carlos Santos',	'Vacinação anual');

describe CONSULTAS;
DROP TABLE consultas;

INSERT INTO PACIENTES VALUES(117,'João Silva','Cachorro','01/01/2010','Maria Oliveira','(11) 9999-9999');

ALTER TABLE PACIENTES MODIFY telefone VARCHAR(255) not null;

describe PACIENTES;

ALTER TABLE ANIMAIS MODIFY nome_animal VARCHAR(255) UNIQUE;
ALTER TABLE ANIMAIS MODIFY id_paciente INTEGER UNIQUE;


UPDATE ANIMAIS SET nome_animal = UPPER(nome_animal);
SELECT id_animal,id_paciente,nome_animal AS nome_animal_maiúsculo, especie,raca,sexo FROM ANIMAIS;

SELECT * FROM ANIMAIS WHERE nome_animal LIKE 'B%';
 
SELECT * FROM CONSULTAS WHERE DESCRICAO LIKE '%Vacina%';

describe ANIMAIS;

ALTER TABLE PACIENTES RENAME COLUMN data_nascimento TO data_de_nascimento;
DESCRIBE PACIENTES;


ALTER TABLE ANIMAIS MODIFY especie CHECK( especie = 'Cachorro, Gato, Coelho, Hamster');