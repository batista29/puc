#include <iostream>
using namespace std;

struct no
{
    int dado;
    struct no *proximo;
};

struct fila
{
    struct no *first;
    struct no *last;
};

struct fila *top = NULL;

void iniciar_fila()
{
    top = (struct fila *)malloc(sizeof(struct fila));

    top->first = NULL;
    top->last = NULL;
}

void inserir(int valor)
{
    struct no *temp = (struct no *)malloc(sizeof(struct no));

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
    struct no *temp = (struct no *)malloc(sizeof(struct no));
    temp = top->first;

    int valor = top->first->dado;

    if (temp != NULL)
    {
        top->first = top->first->proximo;
    }

    cout << "Dado " << valor << " removido da fila" << endl;
    free(temp);
}

void print()
{
    struct no *temp = top->first;

    cout << endl;
    while (temp != NULL)
    {
        cout << temp->dado << '\t';
        temp = temp->proximo;
    }
    cout << endl;
}

int main()
{
    int valor, opcao;

    iniciar_fila();

    do
    {
        cout << "\n1- Enfileirar\n2- Ver\n3- Remover" << endl;
        cin >> opcao;

        switch (opcao)
        {
        case 1:
            cout << "Digite um valor: ";
            cin >> valor;
            inserir(valor);
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