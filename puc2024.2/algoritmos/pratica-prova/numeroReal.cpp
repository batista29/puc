#include <iostream>
using namespace std;

void printNumReal(float numReal)
{
    cout << "Numero real: " << numReal << endl;
}

void parteFracionada(float numReal)
{
    int num = numReal;
    cout << "Parte fracionada: " << numReal - num << endl;
}

int main()
{
    float numReal;

    cout << "Digite o numero: " << endl;
    cin >> numReal;
    printNumReal(numReal);
    parteFracionada(numReal);
}