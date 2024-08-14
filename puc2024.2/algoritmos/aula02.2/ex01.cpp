#include <iostream>

int main()
{
    int numero;
    std::cout << "Qual numero voce quer ver a tabuada: ";
    std::cin >> numero;
    
    for (int i = 0; i <= 10; i++)
    {
       std::cout << numero << " X " << i << " = " << numero*i << "\n"; 
    }    
}