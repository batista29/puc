#include <iostream>
using namespace std;

void print(int soma)
{
    cout << "Soma dos valores do vetor = " << soma;
}

int calcVetor(int vet[])
{
    int soma = 0;

    for (int i = 0; i < 10; i++)
    {
        soma += vet[i];
    }

    print(soma);
}

int main()
{
    int vet[10], num;

    for (int i = 0; i < 10; i++)
    {
        cout << "Digite um numero" << endl;
        cin >> num;
        vet[i] = num;
    }

    calcVetor(vet);
}