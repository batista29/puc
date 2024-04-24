lista = []
maior = 0
segMaior = 0

while (len(lista) < 10):
    num = float(input("Digite um numero: "))
    lista.append(num)

for i in range(len(lista)):
    if(maior < lista[i]):
        segMaior = maior
        maior = lista[i]
print(f"Maior: {maior}, Segundo maior: {segMaior}")