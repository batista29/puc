#include <iostream>

int main()
{
    int fatorial = 1, num;

    while (num >= 0)
    {
        std::cout << "\nDigite um numero: ";
        std::cin >> num;
        for (int i = 1; i <= num; i++)
        {
            fatorial *= i;
        }
        std::cout << "Fatorial: " << fatorial;
        fatorial = 1;
    }
}