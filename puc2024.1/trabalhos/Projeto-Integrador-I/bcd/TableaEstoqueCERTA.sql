drop table Estoque;

CREATE TABLE Estoque(
    id_prod INTEGER PRIMARY KEY NOT NULL,
    nome_prod VARCHAR2(255) NOT NULL,
    desc_prod VARCHAR2(255) NOT NULL,
    custo_prod NUMBER(10,2) NOT NULL,
    custo_fixo NUMBER(10,2) NOT NULL,
    comissao NUMBER (5,3) NOT NULL,
    imposto NUMBER (10,2) NOT NULL,
    rentabilidade NUMBER (6,3) NOT NULL
);

describe Estoque;

INSERT into Estoque values (1,'Caneta', 'Caneta profissional', 36, 15, 5, 12, 20);
INSERT into Estoque values (2, 'Lapis Preto', 'B2', 1, 1, 1, 1, 1);
INSERT into Estoque values (3, 'Caderno' ,'Palmeiras' ,10 ,10 ,10 ,10, 50);
INSERT into Estoque values (4, 'Caderno' ,'São Paulo' ,10 ,10 ,10 ,10, 0);
INSERT into Estoque values (5, 'Caderno' ,'Corinthians' ,10 ,10 ,10 ,10 ,-20);
INSERT into Estoque values (6,'Caderno','Ponte Preta',10,30,20,20,29.99);

SELECT * FROM Estoque;

-- UPDATE Estoque SET custo_fixo = 250 WHERE id_prod = 36;

-- DELETE FROM Estoque WHERE id_prod = 36;

-- INSERT into Estoque values (3,'l?pis','chines',0.21,10,5,18,0);

COMMIT;