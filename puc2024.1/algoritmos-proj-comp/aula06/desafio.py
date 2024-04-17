nome = "Maria do Carmo Almeida"
letras=nome[0]

for i in range(len(nome)):
    if(nome[i] == ' '):
        nomeletras = nome[i+1]
        letras = letras + nomeletras
print(letras)