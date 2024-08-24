#include <iostream>
using namespace std;

int main()
{
    int vetor[15] = {12, 13, 44, 55, 22, 55555, 65, 32, 12, 0, 1, 2, 3, 4, 5}, pares = 0, multiplos = 0;

    for (int i = 0; i < 15; i++)
    {
        if (vetor[i] % 2 == 0)
            pares++;
        if (vetor[i] % 5 == 0)
            multiplos++;
    }

    cout << "Quantidade de numeros pares: " << pares << " quantidade de multiplos de 5: " << multiplos;
}