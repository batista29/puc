#include <iostream>

int main()
{
    double premio, primeiro, segundo, terceiro;
    std::cout << "Digite o valor do prêmio: " << std::endl;
    std::cin >> premio;
    primeiro = premio*0.46;
    segundo = premio*0.32;
    terceiro = premio*0.22;
    std::cout << "O primeiro irá ganhar: " << primeiro << ", O segundo irá ganhar: " << segundo << ", O terceiro irá ganhar: " << terceiro << std::endl;
}