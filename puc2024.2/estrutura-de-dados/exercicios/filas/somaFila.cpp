#include <iostream>
using namespace std;

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

struct fila *top = NULL;

void iniciar()
{
    top = (struct fila *)malloc(sizeof(struct fila));

    top->first = NULL;
    top->last = NULL;
}

void push(int valor)
{
    struct No *temp = (struct No *)malloc(sizeof(struct No));

    temp->dado = valor;
    temp->proximo = NULL;

    if (top->last == NULL)
    {
        top->first = temp;
    }
    else
    {
        top->last->proximo = temp;
    }
    top->last = temp;
}

void print()
{
    struct No *temp = top->first;

    while (temp != NULL)
    {
        cout << temp->dado << " ";
        temp = temp->proximo;
    }
    cout << endl;
}

void somarFila()
{
    int soma = 0;
    struct No *temp = top->first;
    while (temp != NULL)
    {
        soma += temp->dado;
        temp = temp->proximo;
    }
    cout << "Soma da fila =" << soma << endl;
}

void remove()
{
    cout << "Dado removido: " << top->first->dado << endl;
    top->first = top->first->proximo;
}

int main()
{
    iniciar();

    int opcao, valor;

    do
    {
        cout << "1-Push, 2-print, 3-somar, 4-remove" << endl;
        cin >> opcao;
        switch (opcao)
        {
        case 1:
            cout << "Digite um valor" << endl;
            cin >> valor;
            push(valor);
            break;
        case 2:
            print();
            break;
        case 3:
            somarFila();
            break;
        case 4:
            remove();
            break;
        }
    } while (opcao != 0);
}