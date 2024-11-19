// Elabore uma função que dado uma fila, transforme-a
// em duas filas uma com os números pares e outra com
// os ímpares

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
struct fila *filaPar = NULL;
struct fila *filaImpar = NULL;

void iniciar()
{
    filaPar = (struct fila *)malloc(sizeof(struct fila));
    filaImpar = (struct fila *)malloc(sizeof(struct fila));
    top = (struct fila *)malloc(sizeof(struct fila));

    top->first = NULL;
    top->last = NULL;
}

void anularFilas()
{
    filaPar->first = NULL;
    filaPar->last = NULL;

    filaImpar->first = NULL;
    filaImpar->last = NULL;
}

void separar()
{
    anularFilas();

    struct No *principal = top->first;

    int valor;

    while (principal != NULL)
    {
        valor = principal->dado;
        struct No *temp = (struct No *)malloc(sizeof(struct No));
        temp->dado = valor;
        temp->proximo = NULL;

        if (valor % 2 == 0)
        {

            if (filaPar->last == NULL)
            {
                filaPar->first = temp;
            }
            else
            {
                filaPar->last->proximo = temp;
            }
            filaPar->last = temp;
        }
        else
        {
            if (filaImpar->last == NULL)
            {
                filaImpar->first = temp;
            }
            else
            {
                filaImpar->last->proximo = temp;
            }
            filaImpar->last = temp;
        }
        principal = principal->proximo;
    }
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

    separar();
}

void print()
{
    struct No *tempPar = filaPar->first;
    struct No *tempImpar = filaImpar->first;

    if (filaPar != NULL)
    {
        cout << "Pares ";

        while (tempPar != NULL)
        {
            cout << tempPar->dado << " ";
            tempPar = tempPar->proximo;
        }
        cout << endl;
    }
    if (filaImpar != NULL)
    {
        cout << "Impares ";

        while (tempImpar != NULL)
        {
            cout << tempImpar->dado << " ";
            tempImpar = tempImpar->proximo;
        }
        cout << endl;
    }
}

int main()
{
    int valor, opcao;

    iniciar();

    do
    {
        cout << "1-Push, 2-Print: ";
        cin >> opcao;
        switch (opcao)
        {
        case 1:
            cout << "Digite um valor ";
            cin >> valor;
            push(valor);
            break;
        case 2:
            print();
            break;
        }
    } while (opcao != 0);
}