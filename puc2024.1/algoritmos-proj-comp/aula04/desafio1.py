num = 1
qntd_pares = 0
qntd_impares = 0
qntd_numeros = 0

soma_geral = 0
soma_pares = 0
media_pares = "Não tem média"

while(num != 0):
    num = int(input("Digite um numero\n"))
    if(num!=0):
        if (num%2 == 0):
            qntd_pares = qntd_pares + 1
            soma_pares = soma_pares + num
            media_pares = soma_pares / qntd_pares
        else:
            qntd_impares = qntd_impares + 1
        qntd_numeros = qntd_numeros + 1
        soma_geral = soma_geral + num
        media_geral = soma_geral / qntd_numeros
        print(f"Quantidade de numeros pares: {qntd_pares}\n")
        print(f"Quantidade de numeros impares: {qntd_impares}\n")
print(f"Media dos numeros pares: {media_pares}\n")
print(f"Media geral: {media_geral}\n")