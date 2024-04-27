num = int(input("Digite um numero"))

pares = []
impares = []

while(num >= 0):
    if(num%2 == 0):
        pares.append(num)
    else:
        impares.append(num)
    num = int(input("Digite um numero"))

print(f"Pares: pares: {pares}", end="")
print(f"Impares: impares: {impares}", end="")