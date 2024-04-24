create table cliente(
	id_cliente INTEGER NOT NULL PRIMARY KEY,
	nome VARCHAR(255),
	cpf VARCHAR(15) UNIQUE,
	data_nascimento DATE,
	telefone VARCHAR(15),
	email VARCHAR(255)
)

create table servico(
id_servico INTEGER NOT NULL PRIMARY KEY,
descricao VARCHAR(255) NOT NULL,
valor_hora DECIMAL(10,2) NOT NULL
)

create table contrato(
id_contrato INTEGER NOT NULL PRIMARY KEY,
id_cliente INTEGER NOT NULL,
data_inicio DATE NOT NULL,
data_termino DATE,
carga_horaria INTEGER NOT NULL
)

ALTER TABLE contrato ADD CONSTRAINT FK_ID_cliente 
FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente);

describe contrato;

ALTER TABLE cliente ADD cidade varchar(100);

ALTER TABLE servico RENAME COLUMN valor_hora TO valor_unitario;

ALTER TABLE cliente MODIFY data_nascimento varchar(15);

ALTER TABLE cliente DROP (telefone);
describe cliente;

ALTER TABLE servico ADD id_contrato INTEGER;
ALTER TABLE servico ADD CONSTRAINT FK_id_contrato
FOREIGN KEY (id_contrato) REFERENCES contrato (id_contrato);

describe contrato;
ALTER TABLE contrato
ADD CHECK (carga_horaria >= 0);

ALTER TABLE servico ADD status varchar(50) DEFAULT 'aberto';

