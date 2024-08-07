#include <iostream>

int main()
{
    int altura, base;
    float area;
    std::cout << "Digite a base e a altura do triangulo: " << std::endl;
    std::cin >> base >> altura;
    area = (base * altura)/2;
    std::cout << "A area do seu triangulo é: " << area << std::endl;
}