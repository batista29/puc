#include <iostream>
using namespace std;

int main()
{
    int matrizInteiros[5][3] = {12, 33, 41,
                                66, 77, 55,
                                44, 90, 10,
                                0, 6, 4,
                                45, 32, 88};
                                
    int transposta[3][5];

    cout << "Matriz: ";
    for (int i = 0; i < 5; i++)
    {
        cout << "\n";
        for (int j = 0; j < 3; j++)
        {
            cout << matrizInteiros[i][j] << "\t";
            transposta[j][i] = matrizInteiros[i][j];
        }
    }

    cout << "\n\nMatriz transposta: ";
    for (int i = 0; i < 3; i++)
    {
        cout << "\n";
        for (int j = 0; j < 5; j++)
        {
            cout << transposta[i][j] << "\t";
        }
    }
}