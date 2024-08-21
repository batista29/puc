#include <iostream>

int main()
{
    int matrizM[5][5] = {12, 13, 44, 55, 22, 55555, 65, 32, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 12, 33, 44, 100, 1010};
    int maior = 0, linha = 0, coluna = 0;
    for (int i = 0; i < 5; i++)
    {
        for (int j = 0; j < 5; j++)
        {
            if (matrizM[i][j] > maior)
            {
                maior = matrizM[i][j];
                linha = i;
                coluna = j;
            }
        }
    }
    std::cout << "Maior: " << maior << "\nLinha: " << linha << "\nColuna: " << coluna;
}