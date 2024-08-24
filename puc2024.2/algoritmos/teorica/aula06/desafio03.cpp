#include <iostream>
using namespace std;

int main()
{
    int matriz[10][10] = {
        18, 205, 61, 716, 994, 148, 286, 309, 344, 631,
        263, 875, 955, 991, 573, 180, 120, 76, 223, 863,
        810, 44, 132, 632, 548, 927, 266, 930, 225, 46,
        845, 949, 261, 16, 617, 495, 230, 318, 661, 22,
        706, 68, 225, 329, 134, 640, 846, 632, 93, 317,
        968, 512, 440, 873, 37, 487, 304, 579, 840, 695,
        113, 716, 920, 972, 763, 426, 74, 893, 192, 252,
        639, 617, 661, 713, 735, 916, 758, 128, 887, 93,
        416, 423, 630, 424, 241, 740, 710, 613, 672, 641,
        690, 254, 187, 218, 122, 987, 732, 399, 415, 93};

    int soma = 0, menor, maior = 0;

    for (int i = 0; i < 10; i++)
    {
        soma += matriz[3][i];
        if (i == 0)
            menor = matriz[i][4];
        if (matriz[i][4] < menor)
        {
            menor = matriz[i][4];
        }
        if (maior < matriz[i][i])
        {
            //875 maior
            maior = matriz[i][i];
        }
    }

    cout << "A soma dos elementos resultou em: " << soma << "\n"
         << "Menor: " << menor << "\n"
         << "Maior da diagonal: " << maior;
}