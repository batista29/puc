#include <iostream>
using namespace std;

bool ehPar(int num)
{
    return (num % 2 == 0);
}

int main()
{
    int num;

    cout << "Digite um numero: ";
    cin >> num;

    if (ehPar(num))
    {
        cout << "Par com P maiusculo" << endl;
    }
    else
    {
        cout << "Impar" << endl;
    }
}