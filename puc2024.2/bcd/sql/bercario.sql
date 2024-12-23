CREATE TABLE MAE(
CODIGO INTEGER PRIMARY KEY,
CPF VARCHAR2(12) NOT NULL,
NOME VARCHAR2(30) NOT NULL,
ENDERECO VARCHAR2(30) NOT NULL,
TELEFONE VARCHAR2(20) NOT NULL
);

CREATE TABLE MEDICO(
CRM INTEGER PRIMARY KEY,
NOME  VARCHAR2(30) NOT NULL,
TELEFONE  VARCHAR2(30) NOT NULL,
ESPECIALIDADE  VARCHAR2(30) NOT NULL
);
CREATE TABLE BEBE(
CODIGO INTEGER PRIMARY KEY,
NOME VARCHAR2(3) NOT NULL,
DATA_NASCIMENTO DATE NOT NULL,
PESO NUMBER(5,2) NOT NULL,
ALTURA NUMBER(5,2) NOT NULL,
CRM INTEGER NOT NULL,
CODIGO_MAE INTEGER NOT NULL,
FOREIGN KEY(CRM) REFERENCES MEDICO(CRM),
FOREIGN KEY(CODIGO_MAE) REFERENCES MAE(CODIGO)
);


INSERT INTO MAE (CODIGO, CPF, NOME, ENDERECO, TELEFONE)
VALUES (1, '12345678901', 'Maria Souza', 'Rua das Flores, 123', '(11) 98765-4321');

INSERT INTO MAE (CODIGO, CPF, NOME, ENDERECO, TELEFONE)
VALUES (2, '98765432100', 'Ana Silva', 'Avenida Paulista, 456', '(21) 91234-5678');

SELECT * FROM MAE;

INSERT INTO MEDICO (CRM, NOME, TELEFONE, ESPECIALIDADE)
VALUES (12345, 'Dr. Jo�o Pereira', '(11) 99876-5432', 'Pediatria');

INSERT INTO MEDICO (CRM, NOME, TELEFONE, ESPECIALIDADE)
VALUES (67890, 'Dra. Carla Santos', '(21) 92345-6789', 'Ginecologia');

SELECT *FROM MEDICO;

-- TEM QUE USAR O TO_DATE OU DATE
INSERT INTO BEBE VALUES (1, 'Leo', DATE '2024-10-15', 3.500, 50.0, 12345, 1);

INSERT INTO BEBE VALUES (2, 'Lia', DATE '2024-11-01', 3.200, 48.5, 67890, 2);
SELECT * FROM BEBE;








CREATE OR REPLACE FUNCTION OBTER_INFO_MAE_BEBE(p_codigo_bebe INTEGER) 
RETURN VARCHAR2 
IS
  v_nome_bebe VARCHAR2(30);
  v_nome_mae VARCHAR2(30);
  v_resultado VARCHAR2(255);
BEGIN
 
  SELECT b.NOME, m.NOME
  INTO v_nome_bebe, v_nome_mae
  FROM BEBE b
  JOIN MAE m ON b.CODIGO_MAE = m.CODIGO
  WHERE b.CODIGO = p_codigo_bebe;
  
  v_resultado := 'Beb�: ' || v_nome_bebe || ' e M�e: ' || v_nome_mae;
  
  RETURN v_resultado;
END OBTER_INFO_MAE_BEBE;

SELECT OBTER_INFO_MAE_BEBE(2) AS INFO_BEBE_MAE FROM DUAL;
SELECT * FROM bebe;

DROP TABLE BEBE;
DROP TABLE MAE;
DROP TABLE MEDICO;
DROP FUNCTION OBTER_INFO_MAE_BEBE; 