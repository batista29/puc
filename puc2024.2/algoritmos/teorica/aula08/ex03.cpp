#include <iostream>>
using namespace std;

int resultado(int num, int resultado)
{
    cout << num << "! = " << resultado << endl;
}

int fatorial(int num)
{
    int soma = 1, aux = num;
    for (int i = aux; i >= 1; i--)
    {
        soma *= aux;
        aux = i - 1;
    }

    resultado(num, soma);
}

int main()
{
    int num;

    cout << "Digite um numero: " << endl;
    cin >> num;

    fatorial(num);
}