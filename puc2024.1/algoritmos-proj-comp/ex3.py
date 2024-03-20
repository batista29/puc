valor = 0
i=0

while(i < 10):
    num = int(input("Digite um numero"))
    if(num>valor):
        valor = num
    i=i+1
    print(f"{valor}")
