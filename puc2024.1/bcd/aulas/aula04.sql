create table clientes(
id NUMBER(10),
nome VARCHAR2(50),
email VARCHAR2(50),
telefone VARCHAR(20)
)

alter table clientes ADD(data_nascimento date);

ALTER TABLE clientes RENAME COLUMN data_nascimento TO nascimento_data;

ALTER TABLE clientes MODIFY telefone NUMBER(10); 

ALTER TABLE clientes DROP COLUMN email; 

ALTER TABLE clientes RENAME TO consumidores;

DROP TABLE consumidores;

describe consumidores;