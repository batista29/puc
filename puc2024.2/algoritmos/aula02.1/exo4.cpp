#include <iostream>

int main()
{
    int senhaCerta = 2024, senhaDigitada, cont = 1;

    std::cout << "Digite a senha: ";
    std::cin >> senhaDigitada;

    while (senhaDigitada != senhaCerta && cont < 3)
    {
        std::cout << "\nAcesso negado\n";
        std::cout << "Digite a senha: ";
        std::cin >> senhaDigitada;
        cont++;
    }
    
    if (senhaCerta == senhaDigitada)
    {
        std::cout << "\nAcesso liberado\n";
    }
    else
    {
        std::cout << "\nAcesso negado\n";
    }
}