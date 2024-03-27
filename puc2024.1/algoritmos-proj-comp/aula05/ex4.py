idade50=0
somaAlturas=0
qntsIdade=0
somaAltura10a20 = 0
mediaAltura10a20 = 0
pesoInferior = 0

i=1
while(i<=25):
    print(f"PESSOA NÚMERO {i}\n")
    idade = int(input("Digite a idade"))
    altura = float(input("Digite a altura (em cm)"))
    peso = int(input("Digite o peso"))
    somaAlturas = somaAlturas+altura
    if(idade>=10 and idade<=20):
        qntsIdade = qntsIdade+1
        somaAltura10a20 = somaAltura10a20 + altura
        mediaAltura10a20 = somaAltura10a20 / qntsIdade
    if(idade>50):
        idade50=idade50+1
    if (peso < 50):
        pesoInferior = pesoInferior + 1
        percentualPesoInferior = (pesoInferior/i)*100
    i=i+1

print(f"A quantidade de pessoas com mais de 50 anos é: {idade50}\n")
print(f"A media de altura das pessoas com idade de 10 a 20 é: {mediaAltura10a20}\n")
print(f"A quantidade percentual de pessoas com peso inferior a 50kg é: {percentualPesoInferior}\n")
