#include <iostream>
#include <string>
using namespace std;

int main()
{
    /*
    Faça um programa que peça um número inteiro positivo e em
    seguida mostre este número invertido.
    Exemplo: 2349 -> Número Invertido 9432

    Formula inverso de um número A é 1/A, pois A * 1/A = 1
    */

    string numero, inverso;

    std::cout << numero;

    std::cout << "Digite um numero: ";
    std::cin >> numero;

    for (int i = numero.length(); i >= 0; i--)
    {
        inverso += numero[i];
    }

    std::cout << inverso;
}