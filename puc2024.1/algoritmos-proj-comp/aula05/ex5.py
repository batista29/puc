num = int(input("Digite um numero inteiro"))
primo = 0

for i in range (1,num+1):
    valorDivisao = num/i
    if(num%i == 0 or num%i == num and valorDivisao<i):
        primo=primo+1
if (primo > 2):
     print("Esse número não é primo")
else:
     print("Esse número é primo")