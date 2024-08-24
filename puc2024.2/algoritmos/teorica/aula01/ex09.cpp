#include <iostream>

// Elabore um programa que faça a simulação de um caixa
// de uma loja.
// O usuário deverá digitar o Valor da Compra, o Valor Pago
// pelo cliente.
// O programa irá retornar o valor do troco, as cédulas que
// fazem parte do troco e a quantidade de cada cédula.
// Para este programa considere as cédulas de R$20, R$10,
// R$5 e R$1 real
// Considere a possibilidade de não haver troco
// Veja o Exemplo na próxima página

int main()
{
    int valor_compra, pagamento, troco;
    int notasDe20 = 0, notasDe10 = 0, notasDe5 = 0, notasDe1 = 0;

    std::cout << "Digite o valor da compra e quanto o cliente pagou." << std::endl;
    std::cin >> valor_compra >> pagamento;

    troco = pagamento - valor_compra;

    std::cout << "Troco: R$" << troco << std::endl;
    
    while (troco > 0)
    {
        if (troco >= 20)
        {
            notasDe20 = troco / 20;
            troco = troco % 20;
        }
        else if (troco >= 10)
        {
            notasDe10 = troco / 10;
            troco = troco % 10;
        }
        else if (troco >= 5)
        {
            notasDe5 = troco / 5;
            troco = troco % 5;
        }
        else
        {
            notasDe1 = troco / 1;
            troco = troco % 1;
        }
    }

    std::cout << "Quantidade de notas de 20: " << notasDe20 << std::endl;
    std::cout << "Quantidade de notas de 10: " << notasDe10 << std::endl;
    std::cout << "Quantidade de notas de 5: " << notasDe5 << std::endl;
    std::cout << "Quantidade de notas de 1: " << notasDe1 << std::endl;
}