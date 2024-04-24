lista = []
qntd = 0

while (len(lista) < 20):
    num = int(input("Digite um numero: "))
    if(num%3 == 0):
        lista.append(num)
    print(f"{lista}")