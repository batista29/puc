# EXERCICIO 4

n=int(input("Digite quantos numeros você quer"))

cont = 1

soma = cont

while(cont <= n):
    if(n%2 == 0):
        soma = soma + cont
print(f"{soma}")