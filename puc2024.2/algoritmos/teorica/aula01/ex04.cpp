#include <iostream>

int main()
{
    float aresta, volume;
    std::cout << "Digite o comprimento da aresta: " << std::endl;
    std::cin >> aresta;
    volume = aresta*aresta*aresta;
    std::cout << "O volume do seu cube é de: "<< volume << std::endl;
}