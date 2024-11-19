#include <iostream>
using namespace std;

struct No
{
    int dado;
    struct No *proximo;
};

struct Fila
{
    struct No *first;
    struct No *last;
};

struct Fila *top = NULL;

void inicializar()
{
    top = (struct Fila *)malloc(sizeof(struct Fila));

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

void remover()
{
    cout << top->first->dado << " removido" << endl;
    top->first = top->first->proximo;
}

void print()
{
    struct No *temp = top->first;
    int qtd = 0;

    cout << "\nFila: ";
    while (temp != NULL)
    {
        if (temp->dado % 2 == 0)
        {
            qtd++;
        }
        cout << temp->dado << "\t";
        temp = temp->proximo;
    }
    cout << "\n"
         << qtd << " numero(s) pare(s)" << endl;
}

int main()
{
    inicializar();

    int valor, opcao;
    do
    {
        cout << "1- push, 2- Print ";
        cin >> opcao;
        switch (opcao)
        {
        case 1:
            cout << "Digite o valor ";
            cin >> valor;
            push(valor);
            break;
        case 2:
            print();
            break;
        case 3:
            remover();
            break;
        }
    } while (opcao != 0);
}