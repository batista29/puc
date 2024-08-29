#include <iostream>
using namespace std;

int main()
{
    double detM, soma, vetorIgualdade[3] = {-2, 10, -11}, vetorNovo[3], matriz[3][3] = {5, -2, 7, 2, 8, 16, -5, 12, 9}, matrizMult[3][3],
                       adjM[3][3] = {((matriz[0][0] * matriz[2][2]) - (matriz[1][2] * matriz[2][1])),
                                     ((matriz[0][2] * matriz[2][1]) - (matriz[0][1] * matriz[2][2])),
                                     ((matriz[0][1] * matriz[1][2]) - (matriz[0][2] * matriz[1][1])),
                                     ((matriz[1][2] * matriz[2][0]) - (matriz[1][0] * matriz[2][2])),
                                     ((matriz[0][0] * matriz[2][2]) - (matriz[0][2] * matriz[2][0])),
                                     ((matriz[0][2] * matriz[1][0]) - (matriz[0][0] * matriz[1][2])),
                                     ((matriz[1][0] * matriz[2][1]) - (matriz[1][1] * matriz[2][0])),
                                     ((matriz[0][1] * matriz[2][0]) - (matriz[0][0] * matriz[2][1])),
                                     ((matriz[0][0] * matriz[1][1]) - (matriz[0][1] * matriz[1][0]))};

    detM = (matriz[0][0] * matriz[1][1] * matriz[2][2]) + (matriz[0][1] * matriz[1][2] * matriz[2][0]) + (matriz[0][2] * matriz[1][0] * matriz[2][1]) - (matriz[0][0] * matriz[1][2] * matriz[2][1]) - (matriz[0][1] * matriz[1][0] * matriz[2][2]) - (matriz[0][2] * matriz[1][1] * matriz[2][0]);

    for (int i = 0; i < 3; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            matrizMult[i][j] = (1 / detM) * adjM[i][j];
        }
    }

    // terminar
    for (int i = 0; i < 3; i++)
    {
        soma = 0;
        for (int j = 0; j < 3; j++)
        {
            soma += (matrizMult[i][j] * vetorIgualdade[j]);
        }
        vetorNovo[i] = soma;
    }

    for (int i = 0; i < 3; i++)
    {
        cout << vetorNovo[i] << "\t";
    }
}
