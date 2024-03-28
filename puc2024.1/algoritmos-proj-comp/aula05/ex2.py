num_maior = int(input("Digite um numero inteiro"))
valor_soma=0
valor=0

while(num_maior>0):
    if(valor_soma<=0):
        num_menor=num_maior-1
        valor=num_maior*num_menor
        valor_soma=valor_soma+valor
        num_maior=num_menor-1
    else:
        valor_soma = valor_soma*num_maior
        num_maior=num_maior-1
print(valor_soma)