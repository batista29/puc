#include <iostream>
using namespace std;

long long fatorial(int num)
{
    long long somaFat = 1;
    for (int i = 1; i <= num; i++)
    {
        somaFat *= i;
    }
    return somaFat;
}

int main()
{
    int num;
    cout << "Digite um numero" << endl;
    cin >> num;

    cout << fatorial(num);
}