#include <iostream>
using namespace std;

int troca(int *posicao)
{
    *posicao = 90;
}

int main()
{
    int *posicao, num1;
    cout << "Digite 2 valores" << endl;
    cin >> num1;

    posicao = &num1;

    troca(posicao);

    cout << "Teste: " << num1;
}