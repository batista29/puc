#include <iostream>>
using namespace std;

float numReal(float num)
{
    cout << "Numero real: " << num << endl;
}

float fracionaria(float num)
{
    int fracao = num;
    num = num - fracao;
    cout << "Parte fracionaria do numero real: " << num << endl;
}

int main()
{
    float num;
    cout << "Digite um numero: ";
    cin >> num;

    numReal(num);
    fracionaria(num);
}