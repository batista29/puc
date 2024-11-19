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

void iniciar()
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

void remover()
{
    struct No *temp = top->first;
    top->first = temp->proximo;

    free(temp);
}

void somarFila()
{
    struct No *temp = top->first;
    int soma = 0;

    while (temp != NULL)
    {
        soma += temp->dado;
        temp = temp->proximo;
    }

    cout << "Soma " << soma << endl;
}

void inverterFila()
{
    struct Fila *aux = (struct Fila *)malloc(sizeof(struct Fila));
    aux->first = NULL;
    aux->last = NULL;

    while (top->first != NULL)
    {
        struct No *temp = (struct No *)malloc(sizeof(struct No));
        temp->dado = top->first->dado;
        temp->proximo = NULL;

        if (aux->last == NULL)
        {
            aux->first = temp;
            aux->last = temp;
        }
        else
        {
            temp->proximo = aux->first;
            aux->first = temp;
        }
        top->first = top->first->proximo;
    }

    free(top);
    top = aux;
}

int main()
{
    iniciar();

    int opcao, valor;

    do
    {
        cout << "1-Push, 2-Print, 3-Remove, 4-Soma, 5-Inverter" << endl;
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
            remover();
            break;
        case 4:
            somarFila();
            break;
        case 5:
            inverterFila();
            break;
        }

    } while (opcao != 0);
}