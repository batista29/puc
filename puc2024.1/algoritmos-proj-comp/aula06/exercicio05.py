idade = 0
maisJovem = ''

while idade >= 0:
    nome = input("Digite um nome: ")
    nova_idade = int(input("Digite sua idade: "))
    if(idade < nova_idade):
        idade=nova_idade
        maisJovem = nome+" é a mais jovem com +{idade}+ anos de idade"
print(maisJovem)