#include <iostream>

int main()
{
    int num = 1, min, max, cont = 0;

    while (num > 0)
    {
        std::cout << "Digite um numero: ";
        std::cin >> num;
        if (num <= 0)
            break;
        if (cont == 0)
            min = max = num;
        if (num > max)
            max = num;
        if (num < min)
            min = num;
        cont++;
    }
    std::cout << "Numeros validos: " << cont << "\nMaior numero: " << max << "\nMenor numero: " << min;
}