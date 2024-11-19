#include <stdio.h>
#include <stdlib.h>
#include <iostream>

using namespace std;

struct pilha 
{
    int dado;
    struct pilha *proximo;
};

struct pilha *top = NULL;

void push(int valor)
{
    struct pilha *novo = (struct pilha *)malloc(sizeof(struct pilha));

    novo->dado = valor;
    novo->proximo = top;
    top = novo;

    cout << "Valor empilhado: " << novo->dado;
    cout << endl;
}

void print()
{
    struct pilha *temp;
    temp = top;

    while (temp != NULL)
    {
        cout << "Dado: " << temp->dado;
        cout << endl;
        temp = temp->proximo;
    }
}

void remover()
{
    if (top == NULL)
    {
        cout << "Pilha vazia" << endl;
    }
    else
    {
        struct pilha *temp;
        temp = top;
        top = top->proximo;
        cout << "Valor " << temp->dado << " removido" << endl;
        free(temp);
    }
}

int main()
{
    int valor, opcao;

    do
    {
        cout << "1- Impilhar\n2- Ver\n3- Remover" << endl;
        cin >> opcao;

        switch (opcao)
        {
        case 1:
            cout << "Digite um valor: " << endl;
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