#include <iostream>

int main()
{
    float salario, percentual, novoSalario;
    std::cout << "Digite o salario do funcionario e o percentual de reajuste" << std::endl;
    std::cin >> salario >> percentual;
    novoSalario = salario + (salario*(percentual/100));
    std::cout << "Novo salario = "<< novoSalario << std::endl;
}