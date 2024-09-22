#include <iostream>
using namespace std;

void trocaValor(int *ponteiro)
{
    cout << ponteiro << endl;
    cout << *ponteiro << endl;
}

int main()
{
    int num, *ponteiro;
    ponteiro = &num;

    cout << "Digite um valor para o numero";
    cin >> num;

    cout << ponteiro << endl;
    cout << *ponteiro << endl;
    num = 55;
    cout << ponteiro << endl;
    cout << *ponteiro << endl;

    trocaValor(ponteiro);

    num = 550;

    trocaValor(ponteiro);
}