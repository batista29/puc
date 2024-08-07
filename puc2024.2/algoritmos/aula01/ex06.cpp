#include <iostream>

int main()
{
    float comprimento, largura, preco;
    std::cout << "Digite quantos metros de comprimento e largura tem a sua sala" << std::endl;
    std::cin >> comprimento >> largura;
    std::cout << "Digite o valor do metro do carpete" << std::endl;
    std::cin >> preco;
    preco = preco*(comprimento*largura);
    std::cout << "A tamanho da sala é de: " << comprimento*largura << " e o valor final para cobri-lá é de: " << preco << std::endl;
}