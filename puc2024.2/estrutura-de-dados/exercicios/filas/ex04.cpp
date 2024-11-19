#include <iostream>

using namespace std;

// Inverter uma Fila:
// Dada uma fila, escreva uma função para inverter a ordem de seus elementos.

struct No
{
    int dado;
    struct No *proximo;
};

struct fila
{
    struct No *first;
    struct No *last;
};

struct No *top = NULL;
struct fila *fila = NULL;
int qtd = 0;

void iniciar()
{
    top = (struct No *)malloc(sizeof(struct No));
    fila = (struct fila *)malloc(sizeof(struct fila));

    fila->first = NULL;
    fila->last = NULL;
}

void inverter()
{
    struct No *temp = (struct No *)malloc(sizeof(struct No));
    temp = fila->first;

    for (int i = 0; i < qtd; i++)
    {
        cout << "Dado " << temp->dado;
        temp = temp->proximo;
    }

    top = temp;
    cout << "veio??" << top->dado;
}

void push(int valor)
{
    struct No *temp = (struct No *)malloc(sizeof(struct No));

    temp->dado = valor;
    temp->proximo = NULL;

    if (fila->last == NULL)
    {
        fila->first = temp;
    }
    else
    {
        fila->last->proximo = temp;
    }

    fila->last = temp;
    qtd++;
}

void print()
{
    struct No *temp = (struct No *)malloc(sizeof(struct No));
    temp = fila->first;

    while (temp != NULL)
    {
        cout << temp->dado << ' ';
        temp = temp->proximo;
    }
    cout << endl;
}

int main()
{
    int opcao, valor;

    iniciar();

    do
    {
        cout << "1- Push, 2- Print, 3- Inverter" << endl;
        cin >> opcao;
        switch (opcao)
        {
        case 1:
            cout << "Digite o valor" << endl;
            cin >> valor;
            push(valor);
            break;
        case 2:
            print();
            break;
        case 3:
            inverter();
        }
    } while (opcao != 0);
}