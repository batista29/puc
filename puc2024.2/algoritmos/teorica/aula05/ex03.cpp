#include <iostream>
using namespace std;

int main()
{
    int soma = 0, vetor[20] =
                      {5, 47, 22, 14, 39, 10, 27, 34, 2, 48,
                       17, 6, 29, 45, 11, 38, 20, 31, 49, 7};

    for (int i = 0; i < 20; i++)
    {
        soma += vetor[i];
    }

    cout << soma;
}