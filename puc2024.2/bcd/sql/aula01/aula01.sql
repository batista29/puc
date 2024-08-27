CREATE TABLE Taxi (
  Placa VARCHAR(7),
  Marca VARCHAR(30) NOT NULL,
  Modelo VARCHAR(30) NOT NULL,
  AnoFab INTEGER,
  Licenca VARCHAR(9),
  PRIMARY KEY(Placa)
);

CREATE TABLE Cliente (
  CliId VARCHAR(4),
  Nome VARCHAR(80) NOT NULL,
  CPF VARCHAR(14) NOT NULL,
  PRIMARY KEY(CliId)
);

CREATE TABLE Corrida (
  CliId VARCHAR(4),
  Placa VARCHAR(7),
  DataPedido DATE,
  PRIMARY KEY(CliId, Placa, DataPedido),
  FOREIGN KEY(CliId)
    REFERENCES Cliente(CliId),
  FOREIGN KEY(Placa)
    REFERENCES Taxi(Placa)
);

INSERT INTO Cliente VALUES ('1532', 'Asdrúba', '448.754.253-65');
INSERT INTO Cliente VALUES ('1755', 'Doriana', '567.387.387-44');
INSERT INTO Cliente VALUES ('1780', 'Quincas', '546.373.762-02');
INSERT INTO Cliente VALUES ('1788', 'DinoTech', '586.373.762-02');

INSERT INTO Taxi VALUES ('DAE6534', 'Ford', 'Fiesta', 1999, 'MN572345');
INSERT INTO Taxi VALUES ('DKL4598', 'Wolkswagen', 'Gol', 2001, 'AU876543');
INSERT INTO Taxi VALUES ('DKL7878', 'Ford', 'Fiesta', 2001, 'OP102938');
INSERT INTO Taxi VALUES ('JDM8776', 'Wolkswagen', 'Santana', 2002, 'QM365923');
INSERT INTO Taxi VALUES ('JJM3692', 'Chevrolet', 'Corsa', 1999, 'UU335577');
-- Your code here!

INSERT INTO Corrida VALUES ('1755', 'DAE6534', '2003-02-15');
INSERT INTO Corrida VALUES ('1780', 'JDM8776', '2003-02-18');
INSERT INTO Corrida VALUES ('1755', 'DKL7878', '2003-02-16');
INSERT INTO Corrida VALUES ('1780', 'DKL4598', '2003-02-17');
INSERT INTO Corrida VALUES ('1532', 'DKL4598', '2003-02-18');
INSERT INTO Corrida VALUES ('1780', 'DAE6534', '2003-02-16');

-- SELECT Cliente.CliId, Cliente.Nome,
-- Corrida.Placa, Corrida.DataPedido as 'Data do pedido',
-- Taxi.Modelo, Taxi.Placa
-- from Cliente, Corrida, Taxi
-- WHERE Cliente.CliId = Corrida.CliId
-- AND Corrida.Placa = Taxi.Placa;

-- SELECT Cliente.Nome,
-- Taxi.Modelo
-- from Cliente, Corrida, Taxi
-- WHERE Cliente.CliId = Corrida.CliId
-- AND Corrida.Placa = Taxi.Placa;

-- SELECT Cliente.Nome,
-- Taxi.Modelo, Taxi.Placa,
-- Corrida.DataPedido
-- from Cliente, Corrida, Taxi
-- WHERE Cliente.CliId = Corrida.CliId
-- AND Corrida.Placa = Taxi.Placa;

-- SELECT DISTINCT Cliente.Nome,
-- Taxi.Modelo
-- from Cliente, Corrida, Taxi
-- WHERE Cliente.CliId = Corrida.CliId
-- AND Corrida.Placa = Taxi.Placa
-- ORDER BY Taxi.Modelo;

-- SELECT DISTINCT Taxi.Modelo, Cliente.Nome
-- from Cliente, Corrida, Taxi
-- WHERE Cliente.CliId = Corrida.CliId
-- AND Corrida.Placa = Taxi.Placa
-- ORDER BY Taxi.Modelo, Cliente.Nome;

-- SELECT Taxi.Placa, Corrida.CliId
-- FROM Taxi JOIN Corrida ON
-- Taxi.Placa = Corrida.Placa;

-- SELECT *
-- FROM Cliente INNER JOIN Corrida ON 
-- Cliente.CliId = Corrida.CliId;

-- SELECT *
-- FROM Taxi NATURAL JOIN Corrida;

-- SELECT *
-- FROM Cliente LEFT JOIN Corrida
-- ON Corrida.CliId = Cliente.CliId;

-- SELECT *
-- FROM Cliente LEFT JOIN Corrida
-- ON Corrida.CliId = Cliente.CliId
-- WHERE Corrida.CliId IS NULL;

-- SELECT Corrida.Placa, Cliente.Nome
-- FROM Corrida RIGHT JOIN Cliente
-- ON Corrida.CliId = Cliente.CliId;

SELECT Taxi.Placa, Corrida.CliId 
FROM Taxi
LEFT JOIN Corrida
ON Taxi.Placa = Corrida.Placa
UNION
SELECT Corrida.Placa, Cliente.CliId
FROM Corrida
RIGHT JOIN Cliente
ON Cliente.CliId = Corrida.CliId;