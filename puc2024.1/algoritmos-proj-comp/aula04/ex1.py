# EX 1

num=int(input("Digite um numero"))

i1=0
i2=0
i3=0
i4=0

while (num >= 0):
    if num in range (0,25):
        i1=i1+1
    elif num in range (26,50):
        i2=i2+1
    elif num in range (51,75):
        i3=i3+1
    elif num in range (76,100):
        i4=i4+1
    num=int(input("Digite um numero"))
print(f"I1= {i1}\n")
print(f"I2= {i2}\n")
print(f"I3= {i3}\n")
print(f"I4= {i4}\n")
