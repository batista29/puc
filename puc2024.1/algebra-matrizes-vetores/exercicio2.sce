//exercicio 2

for i=1:7
    v(i)=((i+1)/2)^2
end

printf("v= ")
disp(v)

//a
printf("v= 7")
disp(v(7))

//b
disp(v(2)*v(6))

//c

v(5)/v(3)

//d
// variavel auxiliar
p = 1;
for i=1:4
    p=p*v(2*i-1)
    produto=p;
    p=v(2*i-1);
end
printf("v(1)v(3)v(5)v(7)= ")
disp(produto)


