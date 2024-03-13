# Exercicio 1

planoDeTrabalho = int(input("Digite o seu plano de trabalho\n"))
salario = int(input("Digite o seu salário atual\n"))

if(planoDeTrabalho == 1):
    salario=salario+((salario/100)*10)
    print(f"Seu novo salário é: {salario}")
elif planoDeTrabalho == 2:
    salario=salario+((salario/100)*15)
    print(f"Seu novo salário é: {salario}")
else:
    salario=salario+((salario/100)*20)
    print(f"Seu novo salário é: {salario}")
