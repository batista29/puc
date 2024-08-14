# Folha de pagamento em python
funcionarios = {}
salarioLiq = 0

def descontoPorFaltas(num_faltas,salarioBruto,salarioFixo):
    if(num_faltas != 0):
        salarioBruto = salarioBruto - (num_faltas*(salarioFixo/30))
    else:
        salarioBruto = salarioBruto
    return salarioBruto 

def lerDados(funcionarios):
    matricula = int(input("Digite a matrícula do funcionário: "))

    while(matricula in funcionarios.keys()):
        matricula = int(input("Esse nº de matrícula já existe, digite outra:  "))
    else:
        nome =  input("Nome do funcioário: ")
        cod_funcao = int(input("Digite o código da função do funcionário (101 ou 102): "))
        if cod_funcao == 101:
            sal_fixo = 1500
            volume_venda = float(input("Digite o valor de vendas no mês:"))
            comissao = volume_venda*0.09 
            salarioBruto = (sal_fixo+comissao)
        elif cod_funcao == 102:
            sal_fixo = float(input("Salário do funcionário (R$ 2150 - 6950): "))
            while sal_fixo < 2150 or sal_fixo > 6950:
                print("Faixa salarial é de R$2150 a R$6950!")
                sal_fixo = float(input("Salário do funcionário (R$ 2150 - 6950): "))
            salarioBruto = sal_fixo
        num_faltas = int(input("Quantidade de faltas no mês: "))
        salarioBruto = descontoPorFaltas(num_faltas,salarioBruto,sal_fixo)
    return matricula,nome,cod_funcao,num_faltas,salarioBruto

def adicionarFuncionario(matricula,nome,cod_funcao,num_faltas,salarioBruto):
    pessoa = []
    pessoa.append(nome)
    pessoa.append(cod_funcao)
    pessoa.append(num_faltas)
    pessoa.append(salarioBruto)
    
    funcionarios[matricula] = pessoa
    print("Funcionário adicionado com sucesso")
    return funcionarios

def salarioLiquido(salarioBruto):
    if salarioBruto <=2259.20:
        salarioLiq = salarioBruto
        percentual = 0
    elif salarioBruto >= 2259.21 and salarioBruto <=2828.65:
        imposto = salarioBruto * 0.075
        percentual = 7.5
        salarioLiq = salarioBruto - imposto
    elif salarioBruto >= 2828.66 and salarioBruto <=3751.05:
        imposto = salarioBruto * 0.15
        percentual = 15
        salarioLiq = salarioBruto - imposto
    elif salarioBruto >=3751.06 and salarioBruto <=4664.68:
        imposto = salarioBruto * 0.225
        percentual = 22.5
        salarioLiq = salarioBruto - imposto
    elif salarioBruto > 4664.68:
        imposto = salarioBruto * 0.275
        percentual = 27.5
        salarioLiq = salarioBruto - imposto
    return salarioLiq,percentual

def removerFuncionario(funcionarios):
    excluir = int(input("Digite a matricula do funcionario que deseja remover: "))
    while not (excluir in funcionarios.keys()):
        excluir = int(input(f"Funcionário com matrícula {excluir} não encontrado.\nDigite um funcionario valido"))
    else:
        del funcionarios[excluir]
        print(f"Funcionário com matrícula {excluir} removido com sucesso.")

def relatorioFuncionario(funcionarios,matricula,salario_liquido,percentual):
    
    print("\nFolha de pagamento\n")
    print("Nº matrícula: ",matricula)
    print("Nome: "+ funcionarios[matricula][0])
    if funcionarios[matricula][1] == 101:
        funcao = "Vendedor"
    elif funcionarios[matricula][1] == 102:
        funcao = "Administrativo"
    print(f"Código da Função: {funcionarios[matricula][1]} - {funcao}")
    print(f"Quantidade de faltas no mês: {funcionarios[matricula][2]}")
    print(f"Salário Bruto: {funcionarios[matricula][3]}")
    print(f"Salário Líquido:  {salario_liquido}")
    print(f"Alíquota:  {percentual}")

def relatorioDosFuncionarios(funcionarios):

    for matricula in (funcionarios.keys()):
        salarioBruto = funcionarios[matricula][3]
        salario_liquido,porcentagem_desconto = salarioLiquido(salarioBruto)
        print(f"Matricula: {matricula}, Nome: {funcionarios[matricula][0]}, Código da função: {funcionarios[matricula][1]},Faltas: {funcionarios[matricula][2]}, Salário bruto: {funcionarios[matricula][3]}, Salário liquido: {salario_liquido}, Porcentagem de desconto: {porcentagem_desconto}%\n")

def maiorSalario(funcionarios):
    maior_salario = 0

    for matricula in (funcionarios.keys()):
        salarioBruto = funcionarios[matricula][3]
        salario_liquido, percentual = salarioLiquido(salarioBruto)
        if ( salario_liquido > maior_salario):
            maior_salario = salario_liquido
            matriculafuncionario = matricula
            percentual = percentual

    print(f"Matricula: {matriculafuncionario}, {funcionarios[matriculafuncionario][0]}, Código da função: {funcionarios[matriculafuncionario][1]}, Faltas: {funcionarios[matriculafuncionario][2]}, Salário bruto: {funcionarios[matriculafuncionario][3]}, Salário Líquido: {salario_liquido} ")
            
def maisFaltas(funcionarios):
    maior_num_faltas = 0
    
    for matricula in (funcionarios.keys()):
        if(maior_num_faltas < funcionarios[matricula][2]):
            maior_num_faltas = funcionarios[matricula][2]
    print("Funcionário(s) com mais faltas:")
    for matricula in (funcionarios.keys()):
        if(0 != maior_num_faltas == funcionarios[matricula][2]):
            print("Matricula: ", matricula,", Nome: ", funcionarios[matricula][0], ", Código da função: ", funcionarios[matricula][1], ", Faltas: ", funcionarios[matricula][2], ", Salário bruto: ", funcionarios[matricula][3])
        else:
            print("Não temos faltas no sistema")
print("MENU\n")
opcao = int(input("1-Adicionar funcionario\n2-Remover funcionario\n3-Ver relatorio do funcionario\n4-Ver relatorios dos funcionarios\n5-Ver o maior salario\n6-Ver funcionario com mais faltas\n\nEscolha uma das opções acima: \n"))

while opcao !=0:
    if(opcao == 1):
        matricula,nome,cod_funcao,num_faltas,salarioBruto = lerDados(funcionarios)
        adicionarFuncionario(matricula,nome,cod_funcao,num_faltas,salarioBruto)
    elif(opcao == 2):
        removerFuncionario(funcionarios)
    elif(opcao == 3):
        matricula = int(input("Por Favor digite nº da matricula do funcionário que deseja ver relatório: "))
        while not(matricula in funcionarios.keys()):
            matricula = int(input("Por Favor digite nº de matricula existente: "))
        else:
            salarioBruto = funcionarios[matricula][3]
            salarioLiq,percentual = salarioLiquido(salarioBruto)
            relatorioFuncionario(funcionarios,matricula,salarioLiq,percentual)
    elif(opcao == 4):
        relatorioDosFuncionarios(funcionarios)
    elif(opcao == 5):
        maiorSalario(funcionarios)
    elif(opcao == 6):
        maisFaltas(funcionarios)
    elif(opcao == 0):
        break
    else:
        print("Essa opcao não existe!")
        
    print("MENU\n")
    opcao = int(input("1-Adicionar funcionario\n2-Remover funcionario\n3-Ver relatorio do funcionario\n4-Ver relatorios dos funcionarios\n5-Ver o maior salario\n6-Ver funcionario com mais faltas\n\nEscolha uma das opções acima: \n"))