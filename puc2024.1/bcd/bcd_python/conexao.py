import oracledb
# import getpass

#Conexão banco de dados

# pw = getpass.getpass("Entre com sua senha")

connection = oracledb.connect(
    user = "BD150224429",
    # password = pw,
    password = 'ta no canvas',
    dsn = "bd-acd/xe"
)

#Vai chamar a conexão
cursor = connection.cursor()
#
# cursor.execute ("""
#                CREATE TABLE teste (
#                id number (8),
#                nome varchar2(255)
#                )""")

# cursor.execute ("""INSERT INTO teste VALUES(1,'Natã Batista')""")

#
cursor.execute("SELECT * FROM teste")
reultado = cursor.fetchall()
#
for row in reultado:
    print("Dados: ",row)

    cursor.close()
    connection.close()

#Para salvar informaçõe na tabela
# connection.commit()
# print("Executou")