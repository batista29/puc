menor = 200
for i in range(100, 200):
    if(i%6 == 0):
        multiplo = i
        if(menor>multiplo):
            menor = multiplo
print("Terminado, menor = ", menor)
