#include <iostream>
using namespace std;

struct No
{
    int dado;
    struct No *proximo;
};

struct No *inicializar()
{
    return NULL;
}

struct No *inserir(struct No *lista, int valor)
{
    struct No *novo = (struct No *)malloc(sizeof(struct No));
    novo->dado = valor;
    novo->proximo = NULL;
    if (lista = NULL)
    {
        return novo;
    }
    else
    {
        struct No *atual = lista;
        while (atual != NULL)
        {
            atual = atual->proximo;
        }
        atual->proximo = novo;
        return novo;
    }
}

void print(struct No *lista)
{
    struct No *atual = lista;
    while (atual != NULL)
    {
        cout << atual->dado << "->";
        atual = atual->proximo;
    }
    cout << endl;
}

void excluir(int valor, struct No *lista)
{
    struct No *atual = lista;
    struct No *anterior = NULL;

    while (atual != NULL)
    {
        if (atual->dado == valor)
        {
            anterior = atual->proximo;
        }
    }
}