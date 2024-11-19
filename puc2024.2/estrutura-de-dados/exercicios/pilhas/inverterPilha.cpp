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
    struct No *temp;
    temp = top;

    while (temp != NULL)
    {
        cout << temp->dado << endl;
        temp = temp->proximo;
    }
}

void remove()
{
    cout << "Dado removido: " << top->dado << endl;
    top = top->proximo;
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

int main()
{
    int valor, opcao;

    do
    {
        cout << "1-Push, 2-Print, 3-Inverter, 4-remove" << endl;
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
            remove();
            break;
        }
    } while (opcao != 0);
}