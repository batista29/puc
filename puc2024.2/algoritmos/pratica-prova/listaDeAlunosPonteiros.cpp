#include <iostream>
using namespace std;

struct produto
{
    int codigo;
    float valorCompra, valorVenda, lucro;
};

void print(int *cod, float *compra, float *venda)
{
    struct produto prod[10];
    float lucro = *venda / *compra;

    prod[0].codigo = *cod;
    prod[0].valorCompra = *compra;
    prod[0].valorVenda = *venda;
    prod[0].lucro = lucro;

    cout << prod[0].codigo << endl;
    cout << prod[0].valorCompra << endl;
    cout << prod[0].valorVenda << endl;
    cout << prod[0].lucro << endl;
}

int main()
{
    int cod = 20;
    float compra = 90.5, venda = 130.65;

    print(&cod, &compra, &venda);
}