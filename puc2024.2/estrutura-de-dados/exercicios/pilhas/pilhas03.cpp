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

void remove()
{
    struct No *temp = top;
    top = temp->proximo;
    free(temp);
}

void inverter()
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

void sum()
{
    struct No *temp = top;
    int soma = 0;

    while (temp != NULL)
    {
        soma += temp->dado;
        temp = temp->proximo;
    }
    cout << "Soma: " << soma << endl;
}

int main()
{
    int valor, opcao;

    do
    {
        cout << "1-Push, 2-Print, 3-Remove, 4-Inverte, 5-Soma" << endl;
        cin >> opcao;
        switch (opcao)
        {
        case 1:
            cout << "Valor" << endl;
            cin >> valor;
            push(valor);
            break;
        case 2:
            print();
            break;
        case 3:
            remove();
            break;
        case 4:
            inverter();
            break;
        case 5:
            sum();
            break;
        }
    } while (opcao != 0);
}