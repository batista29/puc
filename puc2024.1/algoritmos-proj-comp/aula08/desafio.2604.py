estoque = {}
valores = []

num = int(input("Quantos produtos deseja cadastrar: \n"))

for i in range(num):
    nome_prod = input("Digite o nome do produto: \n")
    custo = int(input("Digite o custo do produto: \n"))
    venda = int(input("Digite o valor de venda do produto: \n"))
    quantidade = int(input("Digite a quantidade de produtos: \n"))

    valores.append(nome_prod)
    valores.append(custo)
    valores.append(venda)
    valores.append(quantidade)
    estoque[i+1]=nome_prod,custo,venda,quantidade

for x in estoque.keys():
    lucro = (estoque[x][2] - estoque[x][1])*estoque[x][3]
    if(lucro >= 100):
        print(f"Produto: {estoque[x][0]}")
        print(f"Custo: {estoque[x][1]}")
        print(f"Venda: {estoque[x][2]}")
        print(f"Quantidade: {estoque[x][3]}")
        print(f"Lucro: {lucro}")