#include <iostream>
using namespace std;

int main()
{
    float soma = 0, media = 0, cont = 0, vetor[10] = {12, 13, 13, 13, 14, 17, 18, 15, 19, 14};
    for (int i = 0; i < 10; i++)
    {
        soma += vetor[i];
    }

    media = soma / 10;

    for (int i = 0; i < 10; i++)
    {
        if (vetor[i] < media)
            cont++;
    }

    cout << "Media de idades: " << media << "\nQuantidade de pessoas com idade menor que a media: " << cont;
}