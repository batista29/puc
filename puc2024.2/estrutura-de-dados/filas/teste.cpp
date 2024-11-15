#include <stdio.h>
#include <stdlib.h>
struct no
{
    int data;
    struct no *next;
};
struct fila
{
    struct no *first;
    struct no *last;
};
void insert(struct fila *f, int valor)
{
    struct no *novo = (struct no *)malloc(sizeof(struct no));
    novo->data = valor;
    novo->next = NULL;
    if (f->last == NULL)
        f->first = novo;
    else
        f->last->next = novo;
    f->last = novo;
}
int remove(struct fila *f)
{
    if (f->first == NULL)
    {
        printf("Fila vazia.\n");
        return -1; // Retorna -1 para indicar erro
    }
    struct no *temp = f->first;
    int valor = temp->data;
    f->first = f->first->next;
    if (f->first == NULL)
        f->last = NULL;
    free(temp);
    return valor;
}
void print(struct fila *f)
{
    struct no *atual = f->first;
    if (atual == NULL)
    {
        printf("Fila vazia.\n");
        return;
    }
    while (atual != NULL)
    {
        printf("%d -> ", atual->data);
        atual = atual->next;
    }
    printf("NULL\n");
}
void freeFila(struct fila *f)
{
    struct no *atual = f->first;
    while (atual != NULL)
    {
        struct no *temp = atual;
        atual = atual->next;
        free(temp);
    }
    f->first = NULL;
    f->last = NULL;
}
int main()
{
    struct fila *f = (struct fila *)malloc(sizeof(struct fila));
    f->first = NULL;
    f->last = NULL;
    insert(f, 10);
    insert(f, 20);
    insert(f, 30);
    print(f);
    printf("Removido: %d\n", remove(f));
    print(f);
    // Liberando a memória da fila
    freeFila(f);
    // Liberando o ponteiro da estrutura fila
    free(f);
    return 0;
}
