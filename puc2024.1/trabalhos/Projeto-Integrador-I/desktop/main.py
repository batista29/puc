import oracledb

connection = oracledb.connect( #conexão com o banco de dados
    user="seu usuario", # usuário
    password='sua senha', #senha
    dsn="seu ip/xe" #domínio do servidor
)

cursor = connection.cursor() # variável que controla o banco de dados
def criptografia(frase): # função para criptografar descrição
    alfabeto = 'ZABCDEFGHIJKLMNOPQRSTUVWXY' # string para pegar os indices das letras
    frase = frase.replace(" ","") # remove espaços em branco
    frase = frase.upper() # transforma a descrição em letras maiúsculas.
    texto_cifrado = '' # variável para guardar a frase criptografada
    vetorFrase = [] # vetor que pega os pares da frase
    chave = [[4,3], # matriz chave da criptografia
            [1,2]] 

    if(len(frase)%2 != 0): #verifica se a frase é impar
        frase = frase + frase[len(frase)-1] #duplica a ultima letra
    for i in range(len(frase)):
        posicoes = alfabeto.find(frase[i]) #subistitui as letras pelos seus índices
        vetorFrase.append(posicoes) # preenche o vetor com os índices

    for i in range(0,len(vetorFrase),2):
        par = vetorFrase[i:i+2] #pega os pares da frase
        cifrado = [0,0] # guarda par cifrado
        for x in range(2):
            cifrado[x] = (chave[x][0]*par[0]+chave[x][1]*par[1])%26  #multiplica os pares pela matriz chave
            texto_cifrado += alfabeto[cifrado[x]] # transforma o resultado em letras novamente
    return texto_cifrado #Retorna a frase criptografada

def descriptografia(frase): # função para descriptografar

    alfabeto = 'ZABCDEFGHIJKLMNOPQRSTUVWXY' # string para pegar os indices das letras
    frase = frase.upper() # transforma a descrição em letras maiúsculas.
    texto_descifrado = '' # variável para guardar a frase descriptografada
    vetorNome = [] # vetor que pega os pares da frase
    chave_inversa = [[2,-3], # matriz invertida
                    [-1,4]]
    determinante = 21 #determinante da matriz chave da criptografia
    #multiplicação de cada elemento da matriz inversa pelo determinante para obter a chave de criptografia
    chave_inversa[0][0] = (determinante*chave_inversa[0][0])%26
    chave_inversa[0][1] = (determinante*chave_inversa[0][1])%26
    chave_inversa[1][0] = (determinante*chave_inversa[1][0])%26
    chave_inversa[1][1] = (determinante*chave_inversa[1][1])%26

    if(len(frase)%2 != 0): #verifica se a frase não é impar
     frase = frase + frase[len(frase)-1] #duplica a ultima letra da frase
    for i in range(len(frase)):
        posicoes = alfabeto.find(frase[i]) # substitui as letras pelos seus indices
        vetorNome.append(posicoes) # preenche o vetor com os indices 

    for i in range(0,len(vetorNome),2): 
        par = vetorNome[i:i+2] # pega os pares cifrados da frase
        descifrado = [0,0] # guarda o par descifrado
        for x in range(2):
            descifrado[x] = (chave_inversa[x][0]*par[0]+ chave_inversa[x][1]*par[1])%26 #multiplica os pares pela chave de descriptografia
            texto_descifrado += alfabeto[descifrado[x]] # transforma os resultados em letras
    texto_descifrado =texto_descifrado.title() # Torna apenas a primeira letra da frase maiúscula
    return texto_descifrado # retorna o texto descifrado
def imprimir_tabela(id_prod, nome_prod, desc_prod, pv, custo_prod, porcentcusto_prod, receitaBruta, porcentReceitaBruta, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade):
    desc_prod = descriptografia(desc_prod) # chama a função de descriptografia
    print("\t"*6 + f"{id_prod}:\t {nome_prod}\t {desc_prod}")
    print("\t"*6 + "Descrição"+"\t"*4+"Valor"+"\t"*3+"%")
    print("\t"*6 + f"A. Preço de venda"+"\t"*3+f"R$ {pv:.2f}"+"\t"*3+"100%")
    print("\t"*6 + f"B. Custo de Aquisição (Fornecedor)\tR$ {custo_prod:.2f}"+"\t"*2+f"{round(porcentcusto_prod):.2f}%")
    print("\t"*6 + f"C. Receita Bruta (A-B)"+"\t"*3 +
          f"R$ {receitaBruta:.2f}"+"\t"*2+f"{round(porcentReceitaBruta):.2f}%")
    print("\t"*6 + f"D.Custo Fixo/Administrativo"+"\t"*2 +
          f"R$ {valor_custoFixo:.2f}"+"\t"*2+f"{round(valor_custoFixo/pv*100):.2f}%")
    print("\t"*6 + f"E.Comissão de Vendas"+"\t"*3 +
          f"R$ {valor_comissao:.2f}"+"\t"*3+f"{round(valor_comissao/pv*100):.2f}%")
    print("\t"*6 + f"F.Impostos"+"\t"*4 +
          f"R$ {valor_imposto:.2f}"+"\t"*3+f"{round(valor_imposto/pv*100):.2f}%")
    print("\t"*6 + f"G. Outros Custos (D+E+F)"+"\t"*2 +
          f"R$ {valor_outroCusto:.2f}"+"\t"*3+f"{round(valor_outroCusto/pv*100):.2f}%")
    print("\t"*6 + f"H. Rentabilidade"+"\t"*3 +
          f"R$ {valor_rentabilidade:.2f}"+"\t"*3+f"{round(valor_rentabilidade/pv*100):.2f}%\n")

def listar_produtos():
    print("\t"*3 + "="*106)
    print("\t"*7+"LISTAGEM")
    connection.commit() 
    cursor.execute("SELECT * FROM Estoque order by id_prod")

    for row in cursor: 

        id_prod = row[0] 
        nome_prod = row[1]
        desc_prod = row[2]
        custo_prod = row[3] 
        custo_fixo = row[4]
        comissao = row[5]
        imposto = row[6] 
        rentabilidade = row[7]

        pv, receitaBruta, porcentcusto_prod, porcentReceitaBruta, porcentOutroCusto, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade, porcentRentab = calcular_valores(
            custo_prod, custo_fixo, comissao, imposto, rentabilidade)

        imprimir_tabela(id_prod, nome_prod, desc_prod, pv, custo_prod, porcentcusto_prod, receitaBruta, porcentReceitaBruta, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade)
        
        if rentabilidade > 20:
            print("\t"*7+"Lucro:\tLucro alto")
        elif rentabilidade > 10:
            print("\t"*7+"Lucro:\tLucro médio")
        elif rentabilidade > 0:
            print("\t"*7+"Lucro:\tLucro baixo")
        elif rentabilidade == 0:
            print("\t"*7+"Lucro:\tEquilíbrio")
        else:
            print("\t"*7+"Lucro:\tPrejuízo")
        print("\n")


def cadastrar_produto():
    print("\t"*3 + "="*106)
    print("\t"*7 +"CADASTRO DE PRODUTOS\n")
    id_prod = input("\t"*7 +"Digite o código do produto: ")
    nome_prod = input("\t"*7 +"Nome do produto: ")
    desc_prod = input("\t"*7 +"Digite uma breve descrição do produto: ")
    custo_prod = float(input("\t"*7 +"Custo do produto: "))
    custo_fixo = float(input("\t"*7 +"Custo fixo do produto (%): "))
    comissao = float(input("\t"*7 +"Comissão de venda (%): "))
    imposto = float(input("\t"*7 +"Imposto sobre a venda (%): "))
    rentabilidade = float(input("\t"*7 +"Rentabilidade (%): "))
    desc_prod = criptografia(desc_prod)

    cursor.execute("INSERT INTO Estoque  VALUES (:id_prod, :nome_prod, :desc_prod, :custo_prod, :custo_fixo, :comissao, :imposto, :rentabilidade)", {'id_prod' : id_prod, 'nome_prod': nome_prod , 'desc_prod' : desc_prod , 'custo_prod': custo_prod , 'custo_fixo': custo_fixo, 'comissao' : comissao, 'imposto' : imposto, 'rentabilidade' : rentabilidade} )
    connection.commit()
    print("\t"*7+"Produto cadastrado com sucesso!\n")

def pesquisar_prod(codigo):
    sql = "SELECT * FROM ESTOQUE WHERE id_prod = :codigo"
    cursor.execute(sql, {
        'codigo': codigo})
    return cursor

def alterar_produto_descricao(id_prod):
    desc_prod=input("\t"*7+"Digite uma nova descriçâo: ")
    desc_prod = criptografia(desc_prod)
    alterar = "UPDATE Estoque SET desc_prod=:desc_prod where id_prod=:id"
    cursor.execute(alterar, {'desc_prod': desc_prod, 'id': id_prod})

    print("\t"*7+"Item alterado com sucesso!")
    connection.commit()

def alterar_produto_custo(id_prod):
    custo_prod=float(input("\t"*7+"Digite um novo custo: "))
   
    alterar = "UPDATE Estoque SET custo_prod=:custo_prod where id_prod=:id"
    cursor.execute(alterar, {'custo_prod':custo_prod, 'id': id_prod})

    print("\t"*7+"Item alterado com sucesso!")
    connection.commit()
   
def alterar_produto_custo_fixo(id_prod):
    custo_fixo=float(input("\t"*7+"Digite um novo custo fixo: "))
   
    alterar = "UPDATE Estoque SET custo_fixo=:custo_fixo where id_prod=:id"
    cursor.execute(alterar, {'custo_fixo':custo_fixo, 'id': id_prod})

    print("\t"*7+"Item alterado com sucesso!")
    connection.commit()

def alterar_produto_comissao(id_prod):
    comissao=float(input("\t"*7+"Digite um novo valor para a comissâo: "))
   
    alterar = "UPDATE Estoque SET comissao=:comissao where id_prod=:id"
    cursor.execute(alterar, {'comissao':comissao, 'id': id_prod})

    print("\t"*7+"Item alterado com sucesso!")
    connection.commit()
   
def alterar_produto_imposto(id_prod):
    imposto=float(input("\t"*7+"Digite um novo valor para o imposto: "))
   
    alterar = "UPDATE Estoque SET imposto=:imposto where id_prod=:id"
    cursor.execute(alterar, {'imposto':imposto, 'id': id_prod})

    print("\t"*7+"Item alterado com sucesso!")
    connection.commit()

def alterar_produto_rentabilidade(id_prod):
    rentabilidade=float(input("\t"*7+"Digite um novo valor de rentabilidade: "))
    while rentabilidade > 100:
        print("\t"*7+"A rentabilidade não deve ser maior que 100% ! ")
        rentabilidade=float(input("\t"*7+"Digite um novo valor de rentabilidade: "))
    alterar = "UPDATE Estoque SET rentabilidade=:rentabilidade where id_prod=:id"
    cursor.execute(alterar, {'rentabilidade':rentabilidade, 'id': id_prod})

    print("\t"*7+"Item alterado com sucesso!")
    connection.commit()

def alterar_produto_nome(id_prod):
    nome_prod=input("\t"*7+"Digite um novo nome: ")
   
    alterar = "UPDATE Estoque SET nome_prod=:nome_prod where id_prod=:id"
    cursor.execute(alterar, {'nome_prod':nome_prod, 'id': id_prod})

    print("\t"*7+"Item alterado com sucesso!")
    connection.commit()  

def escolher_update():
    print("\t"*3 + "="*106)
    print("\t"*7 +"ALTERAR PRODUTOS\n")
    codigo = int(input("\t"*7+"Digite o id do produto: "))
    pesquisar_prod(codigo)
    for row in cursor:
            id_prod = row[0]
            nome_prod = row[1]
            desc_prod = row[2]
            custo_prod = row[3]
            custo_fixo = row[4]
            comissao = row[5]
            imposto = row[6]
            rentabilidade = row[7]
            pv, receitaBruta, porcentcusto_prod, porcentReceitaBruta, porcentOutroCusto, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade, porcentRentab = calcular_valores(custo_prod, custo_fixo, comissao, imposto, rentabilidade)
            imprimir_tabela(id_prod, nome_prod, desc_prod, pv, custo_prod, porcentcusto_prod, receitaBruta,porcentReceitaBruta, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade)
            if rentabilidade > 20:
                print("\t"*7+"Lucro:\tLucro alto")
            elif rentabilidade > 10:
                print("\t"*7+"Lucro:\tLucro médio")
            elif rentabilidade > 0:
                print("\t"*7+"Lucro:\tLucro baixo")
            elif rentabilidade == 0:
                print("\t"*7+"Lucro:\tEquilíbrio")
            else:
                print("\t"*7+"Lucro:\tPrejuízo")
            print("\n")
    confirmacao = input("\t"*7+"Deseja realmente alterar este produto? (S/N)")

    if confirmacao.upper() == 'S':
        print("\n"+"\t"*7+"Menu de alteração:")
        print("\t"*7+"1- Nome\n"+"\t"*7+"2- Descrição\n"+"\t"*7+"3- Custo do produto\n"+"\t"*7+"4- Custo fixo\n"+"\t"*7+"5- Comissão de venda\n"+"\t"*7+"6- Imposto\n"+"\t"*7+"7- Rentabilidade")
        escolher = int(input("\t"*7+"O que deseja alterar? "))
        if(escolher == 1):
            alterar_produto_nome(id_prod)
        elif(escolher == 2):
            alterar_produto_descricao(id_prod)
        elif(escolher == 3):
            alterar_produto_custo(id_prod)
        elif(escolher == 4):
            alterar_produto_custo_fixo(id_prod)
        elif(escolher == 5):
            alterar_produto_comissao(id_prod)
        elif(escolher == 6):
            alterar_produto_imposto(id_prod)
        elif(escolher == 7):
            alterar_produto_rentabilidade(id_prod)
        else:
            print("\t"*7+"Essa opção não existe!")

def calcular_valores(custo_prod, custo_fixo, comissao, imposto, rentabilidade):
    pv = custo_prod / (1 - ((custo_fixo + comissao + imposto + rentabilidade) / 100))
    porcentcusto_prod = (custo_prod / pv) * 100
    receitaBruta = pv - custo_prod
    porcentReceitaBruta = (receitaBruta / pv) * 100
    porcentOutroCusto = custo_fixo + comissao + imposto
    valor_custoFixo = (custo_fixo * pv) / 100
    valor_comissao = (comissao * pv) / 100
    valor_imposto = (imposto * pv) / 100
    valor_outroCusto = valor_custoFixo + valor_comissao + valor_imposto
    valor_rentabilidade = receitaBruta - valor_outroCusto
    porcentRentab = (valor_rentabilidade / pv) * 100
    return pv, receitaBruta, porcentcusto_prod, porcentReceitaBruta, porcentOutroCusto, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade, porcentRentab

def apagar_produto():
    print("\t"*3 + "="*106)
    print("\t"*7 +"EXCLUIR PRODUTOS\n")
    codigo = int(input("\t"*7+"Digite o id do produto: "))
    pesquisar_prod(codigo)
    for row in cursor:
            id_prod = row[0]
            nome_prod = row[1]
            desc_prod = row[2]
            custo_prod = row[3]
            custo_fixo = row[4]
            comissao = row[5]
            imposto = row[6]
            rentabilidade = row[7]
            pv, receitaBruta, porcentcusto_prod, porcentReceitaBruta, porcentOutroCusto, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade, porcentRentab = calcular_valores(custo_prod, custo_fixo, comissao, imposto, rentabilidade)
            imprimir_tabela(id_prod, nome_prod, desc_prod, pv, custo_prod, porcentcusto_prod, receitaBruta,porcentReceitaBruta, valor_custoFixo, valor_comissao, valor_imposto, valor_outroCusto, valor_rentabilidade)
            if rentabilidade > 20:
                print("\t"*7+"Lucro:\tLucro alto")
            elif rentabilidade > 10:
                print("\t"*7+"Lucro:\tLucro médio")
            elif rentabilidade > 0:
                print("\t"*7+"Lucro:\tLucro baixo")
            elif rentabilidade == 0:
                print("\t"*7+"Lucro:\tEquilíbrio")
            else:
                print("\t"*7+"Lucro:\tPrejuízo")
            print("\n")

    escolha=input("\t"*7+f"Tem certeza que deseja excluir o produto de id {id_prod}? (S/N)")
    escolha=escolha.upper()
        
    if escolha=='S':
            try:
                cursor.execute("DELETE FROM Estoque WHERE id_prod= :id", {'id': id_prod})
                print("\t"*7+"Item excluido com sucesso!")
                connection.commit() 
            except oracledb.Error as erro:
                print(erro)
    else:
            print("\t"*7+"Seu item não foi excluído!")

print("\t"*9+"MENU\n")
print("\t"*3+"="*106)
print("\t"*3+"|1- Cadastrar produto |"+" 2- Listar produto |"+" 3-Alterar produto |"+" 4- Excluir produto |"+" 0- Sair do programa |")
print("\t"*3+"="*106)
opcao = int(input("\t"*7+"Escolha uma das opções acima: "))

while opcao !=0:
    if opcao == 1:
        cadastrar_produto()
    elif opcao == 2:
        listar_produtos()
    elif opcao == 3:
        escolher_update()
    elif opcao == 4:
        apagar_produto()
    else:
        print("\t"*7+"Opção inválida!")
        
    print("\t"*9+"MENU\n")
    print("\t"*3+"="*106)
    print("\t"*3+"|1- Cadastrar produto |"+" 2- Listar produto |"+" 3-Alterar produto |"+" 4- Excluir produto |"+" 0- Sair do programa |")
    print("\t"*3+"="*106)
    opcao = int(input("\t"*7+"Escolha uma das opções acima: "))

connection.commit()
cursor.close()   
connection.close()
