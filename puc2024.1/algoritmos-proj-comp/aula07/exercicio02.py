lista = []

while(len(lista) < 3):
    num = int(input("Digite um numero"))
    lista.append(num)

outro = int(input("Digite outro numero"))

for i in range(len(lista)):
    if(lista[i] == outro):
        msg = "Esta na lista"
        break
    else:
        msg = "Não tem"
print(msg)