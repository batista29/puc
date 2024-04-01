senha_correta = 2005
tentativas = 3

while (tentativas > 0):
    senha_user = int(input("Digite a senha correta"))
    if(senha_user == senha_correta):
        print("Acesso liberado")
        break
    else:
        print(f"Acesso negado")
        tentativas = tentativas-1