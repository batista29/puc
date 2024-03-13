//EXERCICIO 4

for i=1:10
    for j=i:6
        if i>j then
            A(i,j)=2*i
        elseif i<j
            A(i,j)=i+j;
        elseif i==j
            A(i,j)=i+1
        end
        
     end
end

printf("A= ")
disp(A)
