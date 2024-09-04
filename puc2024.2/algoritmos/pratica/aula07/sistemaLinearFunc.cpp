#include <iostream>
using namespace std;

int adjM[3][3];

void matrizMult(int detM, int adjM)
{

    return 0;
}

void matrizAdjunta(int matriz[3][3], int adjM[3][3])
{
    adjM[0][0] = ((matriz[1][1] * matriz[2][2]) - (matriz[1][2] * matriz[2][1]));
    adjM[0][1] = ((matriz[0][2] * matriz[2][1]) - (matriz[0][1] * matriz[2][2]));
    adjM[0][2] = ((matriz[0][1] * matriz[1][2]) - (matriz[0][2] * matriz[1][1]));
    adjM[1][0] = ((matriz[1][2] * matriz[2][0]) - (matriz[1][0] * matriz[2][2]));
    adjM[1][1] = ((matriz[0][0] * matriz[2][2]) - (matriz[0][2] * matriz[2][0]));
    adjM[1][2] = ((matriz[0][2] * matriz[1][0]) - (matriz[0][0] * matriz[1][2]));
    adjM[2][0] = ((matriz[0][2] * matriz[1][0]) - (matriz[0][0] * matriz[1][2]));
    adjM[2][1] = ((matriz[0][2] * matriz[1][0]) - (matriz[0][0] * matriz[1][2]));
    adjM[2][2]

    return adjM;
}

float determinante(int matriz[3][3])
{
    float detM = 0;
    detM = (matriz[0][0] * matriz[1][1] * matriz[2][2]) + (matriz[0][1] * matriz[1][2] * matriz[2][0]) + (matriz[0][2] * matriz[1][0] * matriz[2][1]) - (matriz[0][0] * matriz[1][2] * matriz[2][1]) - (matriz[0][1] * matriz[1][0] * matriz[2][2]) - (matriz[0][2] * matriz[1][1] * matriz[2][0]);

    return detM;
}

int main()
{
    int vetorIgualdade[3] = {-2, 10, -11}, matriz[3][3] = {5, -2, 7, 2, 8, 16, -5, 12, 9};

    matrizAdjunta(matriz);
    determinante(matriz);
}