#include <stdio.h>
#include <stdlib.h>

struct no
{
    int data;
    struct no *next;
};

struct pilha
{
    struct no *top;
};

void push(struct pilha *p, int valor)
{
    struct no *novo = (struct no *)malloc(sizeof(struct no));
    (*novo).data = valor;
    (*novo).next = (*p).top;
    (*p).top = novo;
}

int pop(struct pilha *p)
{
    if ((*p).top == NULL)
    {
        printf("Pilha vazia.\n");
        return -1; // Retorna -1 para indicar erro
    }
    struct no *temp = (*p).top;
    int valor = (*temp).data;
    (*p).top = (*p).top->next;
    free(temp);
    return valor;
}

void print(struct pilha *p)
{
    struct no *atual = (*p).top;
    if (atual == NULL)
    {
        printf("Pilha vazia.\n");
        return;
    }
    while (atual != NULL)
    {
        printf("%d -> ", (*atual).data);
        atual = (*atual).next;
    }
    printf("NULL\n");
}

void freePilha(struct pilha *p)
{
    struct no *atual = (*p).top;
    while (atual != NULL)
    {
        struct no *temp = atual;
        atual = (*atual).next;
        free(temp);
    }
    (*p).top = NULL;
}

int main()
{
    struct pilha *p = (struct pilha *)malloc(sizeof(struct pilha));
    (*p).top = NULL;
    push(p, 10);
    push(p, 20);
    push(p, 30);
    print(p);
    printf("Desempilhado: %d\n", pop(p));
    print(p);
    // Liberando a memória da pilha
    freePilha(p);
    // Liberando o ponteiro da pilha
    free(p);
    return 0;
}
