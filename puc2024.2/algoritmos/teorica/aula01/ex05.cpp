#include <iostream>

int main()
{
    float km, litros;
    std::cout << "Digite quantos km o seu carro percorreu, e quantos litros de combustivel ele tinha" << std::endl;
    std::cin >> km >> litros;
    std::cout << "O seu carro faz " << litros/km << " litros por km" << std::endl;
}