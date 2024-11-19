#include <iostream>
using namespace std;

struct No
{
    int dado;
    struct No *proximo;
};

struct No *top = NULL;

void push(int valor)
{
    struct No *temp = (struct No *)malloc(sizeof(struct No));

    temp->dado = valor;
    temp->proximo = top;
    top = temp;
}

void print()
{
    struct No *temp = top;

    while (temp != NULL)
    {
        cout << temp->dado << endl;
        temp = temp->proximo;
    }
}

void somarPilha()
{
    struct No *temp = top;
    int soma = 0;

    while (temp != NULL)
    {
        soma += temp->dado;
        temp = temp->proximo;
    }

    cout << "Soma da pilha = " << soma << endl;
}

void inverterPilha()
{
    struct No *aux = NULL;

    while (top != NULL)
    {
        struct No *temp = (struct No *)malloc(sizeof(struct No));

        temp->dado = top->dado;
        temp->proximo = aux;
        aux = temp;
        top = top->proximo;
    }

    top = aux;
}

void remover()
{
    struct No *temp = top;
    top = top->proximo;

    free(temp);
}

int main()
{
    int opcao, valor;

    do
    {
        cout << "1-Push, 2-Print, 3-Inverter, 4-Soma, 5-Remover" << endl;
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
            inverterPilha();
            break;
        case 4:
            somarPilha();
            break;
        case 5:
            remover();
            break;
        }
    } while (opcao != 0);
}