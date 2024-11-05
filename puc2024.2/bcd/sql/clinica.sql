CREATE TABLE especialidade(
  codigo INTEGER PRIMARY KEY,
  nome VARCHAR(20) NOT NULL
);

CREATE TABLE medico(
  crm INTEGER PRIMARY KEY,
  salario decimal(5,2) NOT NULL,
  nome VARCHAR(20) NOT NULL,
  cod_especialidade INTEGER NOT NULL,
  foreign KEY(cod_especialidade) references especialidade(codigo)
);

CREATE TABLE quarto(
  numero INTEGER NOT NULL PRIMARY KEY,
  andar INTEGER NOT NULL
);

CREATE TABLE paciente(
 cpf VARCHAR(11) NOT NULL PRIMARY KEY,
 rg VARCHAR(11) NOT NULL,
 endereco VARCHAR(30) NOT NULL,
 nome VARCHAR(20) NOT NULL,
 telefone VARCHAR(13) NOT NULL,
 data_nascimento date NOT NULL,
 numero_quarto INTEGER NOT NULL,
 foreign KEY (numero_quarto) references quarto(numero)
);

CREATE TABLE tem(
  crm_medico INTEGER NOT NULL,
  cpf_paciente VARCHAR(11) NOT NULL,
  hora_atendimento timestamp NOT NULL,
  foreign key(cpf_paciente) references paciente(cpf),
  primary key (crm_medico, cpf_paciente, hora_atendimento)
);

CREATE TABLE atende(
  crm_medico INTEGER NOT NULL,
  cpf_paciente VARCHAR(11) NOT NULL,
  hora_atendimento timestamp NOT NULL,
  foreign key(cpf_paciente) references paciente(cpf),
  primary key (crm_medico, cpf_paciente)
);
