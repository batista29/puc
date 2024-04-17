nome = input("Digite seu nome: ")
sexo = input(f"Digite seu sexo:\n(F para feminino e M para masculino)")
sexo = sexo.upper()
idade = int(input("Digite sua idade: "))

if(sexo == 'F' and idade < 25):
    print("ACEITA")
else:
    print("NÃO ACEITA")