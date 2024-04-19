primeira = input("Digite algo\n")
segunda = input("Digite outra coisa\n")

posicaoSegunda = primeira.find(segunda) + (len(segunda))

if(len(primeira) == posicaoSegunda):
    print("É a ultima")
else:
    print("Não é a ultima")