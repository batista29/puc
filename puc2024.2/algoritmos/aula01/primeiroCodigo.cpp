#include <iostream>

int main()
{
    int num;
    std::cout<<"Digite um numero inteiro\n";
    std::cin >> num;
    
    int sucessor = num + 1;
    int antecessor = num - 1;
    
    std::cout<<"Numero digitado: "<<num <<std::endl;
    std::cout << "Antecessor: " << antecessor << std::endl;
    std::cout << "Sucessor: " << sucessor << std::endl;

    return 0;
}