CREATE TABLE mae(
    id_mae INTEGER PRIMARY KEY NOT NULL,
    cpf VARCHAR2(11) NOT NULL,
    endereco VARCHAR2(30) NOT NULL,
    telefone VARCHAR2(30) NOT NULL,
    data_nasc DATE NOT NULL
);

CREATE TABLE medico(
    crm INTEGER PRIMARY KEY NOT NULL,
    nome VARCHAR2(30) NOT NULL,
    telefone VARCHAR2(30) NOT NULL,
    especialidade VARCHAR2(30) NOT NULL
);

CREATE TABLE bebe(
    codigo INTEGER PRIMARY KEY NOT NULL,
    data_nasc DATE NOT NULL,
    peso INTEGER NOT NULL,
    altura INTEGER NOT NULL,
    id_mae INTEGER NOT NULL,
    crm_medico INTEGER NOT NULL,
    FOREIGN KEY (id_mae) REFERENCES mae(id_mae), 
    FOREIGN KEY (crm_medico) REFERENCES medico(crm)
);

INSERT INTO mae VALUES(1, '11111111111', 'campinas','1999999999', '12/12/2024');
INSERT INTO medico VALUES(123, 'Marcos', '1999559999', 'nenhuma');
INSERT INTO bebe VALUES(1, '12/12/2024', 7, 0.75, 1,123);

SELECT * FROM bebe;

DROP TABLE mae;
DROP TABLE medico;
DROP TABLE bebe;