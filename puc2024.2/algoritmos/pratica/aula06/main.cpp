#include <iostream>
using namespace std;

int main()
{
    int linhaM = 0, colunaM = 0, valor = 0, cont = 0, soma;

    cout << "Digite quantas linhas e colunas a sua matriz tera" << endl;
    cin >> linhaM >> colunaM;

    int matriz[linhaM][colunaM];

    for (int i = 0; i < linhaM; i++)
    {
        for (int j = 0; j < colunaM; j++)
        {
            cout << "Digite o valor que ira na linha " << i + 1 << " da coluna " << j + 1 << endl;
            cin >> valor;
            matriz[i][j] = valor;
        }
    }

    cout << "Sua matriz:\n";
    for (int i = 0; i < linhaM; i++)
    {
        for (int j = 0; j < colunaM; j++)
        {
            cout << matriz[i][j] << "\t";
        }
        cout << "\n";
    }

    int vetor[linhaM], vetorNovo[linhaM];

    while (cont < linhaM)
    {
        cout << "Digite o valor da linha " << cont + 1 << " do vetor" << endl;
        cin >> valor;
        vetor[cont] = valor;
        cont++;
    }

    for (int i = 0; i < linhaM; i++)
    {
        soma = 0;
        for (int j = 0; j < colunaM; j++)
        {
            soma += (matriz[i][j] * vetor[j]);
        }
        vetorNovo[i] = soma;
    }

    for (int i = 0; i < linhaM; i++)
    {
        cout << vetorNovo[i] << "\t";
    }
}