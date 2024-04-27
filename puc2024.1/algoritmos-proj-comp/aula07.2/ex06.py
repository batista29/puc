alunos = []
idade = []
alturas = []

while(len(alunos) == 30):
    numIdade = int(input("Digite sua idade: "))
    numAltura = int(input("Digite sua altura em cm: "))
    idade.append(numIdade)
    alturas.append(numAltura)
    alunos.append(idade)
    alunos.append(alturas)
    
print(alunos)

# somaAlturas = 0
# for i in range(len(alunos)):
#     somaAlturas = somaAlturas + alunos[i][2]
#     print(alunos[i][2])