#include <iostream>
using namespace std;

int main()
{
    int vetorInverso[20], vetor[20] =
                              {5, 47, 22, 14, 39, 10, 27, 34, 2, 48,
                               17, 6, 29, 45, 11, 38, 20, 31, 49, 7};

    for (int j = 0; j < 20; j++)
    {
        cout << vetor[j] << "\t";
    }
    cout << "\n";
    for (int i = 19, j = 0; i >= 0; i--, j++)
    {
        vetorInverso[j] = vetor[i];

        cout << vetorInverso[j] << "\t";
    }
}