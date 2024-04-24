num_maior = 0

while(num_maior==0):
        num_maior = int(input("Digite um numero inteiro"))
        if(num_maior>0):
                num_menor = num_maior - 1
                valor = num_menor * num_maior
                num_maior = num_menor - 1
                while (num_maior > 0):
                        valor = valor*num_maior
                        num_maior = num_maior- 1
                print(valor)