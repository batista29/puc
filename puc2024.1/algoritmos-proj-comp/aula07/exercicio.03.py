lista = []
soma = 0
maior = 0
menor = 999
qntdMaiores29 = 0

while(len(lista)<10):
    num = float(input("Digite um numero: "))
    lista.append(num)

for i in range(len(lista)):
    soma = soma+lista[i]
    if(lista[i] > 29):
        qntdMaiores29 =+ 1
    if(lista[i] > maior):
        maior = lista[i]
    if(lista[i] < menor):
        menor = lista[i]

media = soma/(len(lista))
print(f"Media: {media}, Maior: {maior}, Menor: {menor}, Maiores que 29: {qntdMaiores29}")