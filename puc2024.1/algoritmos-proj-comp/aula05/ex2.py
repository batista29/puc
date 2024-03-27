idade50=0
somaAlturas=0
qntsIdade=0
while(0<5):
    idade = int(input("Digite a idade"))
    altura = float(input("Digite a altura"))
    peso = int(input("Digite o peso"))
    somaAlturas = somaAlturas+altura
    if(idade>=10 and idade<=20):
        qntsIdade = qntsIdade+1
    if(idade>50):
        idade50=idade50+1
    print(idade50)
    print(qntsIdade/25)
