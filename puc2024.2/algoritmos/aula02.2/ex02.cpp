#include <iostream>

int main()
{
    int num;
    bool primo = true;

    std::cout << "Digite um numero" << std::endl;
    std::cin >> num;

    for (int i = 2; i < num; i++)
    {
        if (num % i == 0)
        {
            primo = false;
            break;
        }
    }

    if (primo == true)
    {
        std::cout << "Primo";
    }
    else
    {
        std::cout << "Nao primo";
    }
}