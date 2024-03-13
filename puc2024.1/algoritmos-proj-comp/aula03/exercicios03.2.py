num=int(input("Digite um numero"))
n=int(input("Digite um valor para potenciação"))

cont = 1
result = num
while(cont < n):
    result = result * num
    cont=cont+1
print(f"Potenciação = {result}")
