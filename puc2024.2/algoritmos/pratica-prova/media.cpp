#include <iostream>
using namespace std;

float mediaCalc(float nota1, float nota2, float nota3)
{
    return (nota1 + nota2 + nota3) / 3;
}

int main()
{
    float nota1 = 0, nota2 = 0, nota3 = 0, media = 0;

    cout << "Digite o valor para a nota 1, 2 e 3" << endl;
    cin >> nota1 >> nota2 >> nota3;

    media = mediaCalc(nota1, nota2, nota3);
    cout << "A media e: " << media << endl;
}