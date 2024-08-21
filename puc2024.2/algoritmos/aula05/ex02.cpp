#include <iostream>
using namespace std;

int main()
{
    int vetor[20] =
        {5, 47, 22, 14, 39, 10, 27, 34, 2, 48,
         17, 6, 29, 45, 11, 38, 20, 31, 49, 7},
        mult;

    cout << "Digite por qual numero voce quer multiplicar o valores do vetor\n";
    cin >> mult;

    for (int i = 0; i < 20; i++)
    {
        cout << vetor[i] * mult << "\t";
    }
}