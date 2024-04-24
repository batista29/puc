qntd = int(input("Digite quantos números deseja"))
i = 0
inicial = 2
novo = 1
soma = 0

while(i<qntd):
    soma=soma+inicial
    novo=inicial-novo
    inicial=inicial+novo
    i=i+1
print(soma)