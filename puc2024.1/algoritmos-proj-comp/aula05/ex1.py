num = int(input("Digite com quantas pessoas deseja fazer a pesquisa\n"))
cont = 0
maiorSalario = 0
somaSalario = 0
somaFilhos = 0
salarioCem = 0

while(cont < num):
    salario = float(input("Digite o salário da pessoa\n"))
    if(salario <= 100):
        salarioCem=salarioCem+1
    numFilhos = int(input("Quanto filhos?\n"))
    if(maiorSalario < salario):
        maiorSalario = salario
    somaSalario = somaSalario + salario
    somaFilhos = somaFilhos + numFilhos
    cont = cont + 1
mediaSalario = somaSalario/num
mediaFilho = somaFilhos/num
mediaSalarioCem = (salarioCem/num)*100
print(f"Maior Salario = {maiorSalario} , soma dos salarios = {somaSalario} , soma dos filhos = {somaFilhos}\n")
print(f"Media dos Salarios = {mediaSalario}\n Media dos filhos = {mediaFilho}\n Maior Salario = {maiorSalario}\n Percentual de pessoas com salario ate R$100,00 = {mediaSalarioCem}%")
