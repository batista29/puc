#include <stdio.h>
#include <stdlib.h>

// Definição da estrutura do nó
struct No
{
    int dado;
    struct No *proximo;
};
// Função para inicializar uma lista (retorna NULL)
struct No *inicializar()
{
    return NULL;
}
// Função para inserir um elemento no início da lista
struct No *inserir(struct No *lista, int valor)
{
    struct No *novo = (struct No *)malloc(sizeof(struct No));
    novo->dado = valor;
    novo->proximo = lista;
    return novo;
}
// Função para inserir um elemento numa determinada posicao
struct No *inserirPos(struct No *lista, int valor, int pos)
{
    struct No *novo = (struct No *)malloc(sizeof(struct No));
    novo->dado = valor;
    if (pos == 0)
    {
        novo->proximo = lista;
        return novo;
    }
    else
    {
        int counter = 0;
        struct No *anterior = NULL;
        struct No *atual = lista;
        while (counter != pos)
        {
            anterior = atual;
            atual = atual->proximo;
            counter++;
        }
        if (atual == NULL)
        {
            printf("Posicao maior que o tamanho da lista!\n");
        }
        else
        {
            novo->proximo = atual;
            anterior->proximo = novo;
        }
        return lista;
    }
}
// Função para percorrer a lista e imprimir os elementos
void percorrer(struct No *lista)
{
    struct No *atual = lista;
    while (atual != NULL)
    {
        printf("%d -> ", atual->dado);
        atual = atual->proximo;
    }
    printf("NULL\n");
}
// Função para buscar um elemento na lista
struct No *buscar(struct No *lista, int valor)
{
    struct No *atual = lista;
    while (atual != NULL)
    {
        if (atual->dado == valor)
        {
            return atual;
        }
        atual = atual->proximo;
    }
    return NULL;
}
// Função para remover um elemento da lista
struct No *remover(struct No *lista, int valor)
{
    struct No *anterior = NULL;
    struct No *atual = lista;
    while (atual != NULL && atual->dado != valor)
    {
        anterior = atual;
        atual = atual->proximo;
    }
    if (atual == NULL)
    {
        printf("Elemento %d não encontrado!\n", valor);
        return lista;
    }
    if (anterior == NULL)
    {
        // O elemento a ser removido é o primeiro
        lista = atual->proximo;
    }
    else
    {
        // O elemento a ser removido está no meio ou no fim
        anterior->proximo = atual->proximo;
    }
    free(atual);
    return lista;
}
// Função para liberar a lista (liberar memória)
void liberarLista(struct No *lista)
{
    struct No *atual = lista;
    while (atual != NULL)
    {
        struct No *temp = atual;
        atual = atual->proximo;
        free(temp);
    }
}
int main()
{
    struct No *lista = inicializar();
    // Inserindo elementos
    lista = inserir(lista, 10);
    lista = inserir(lista, 20);
    lista = inserir(lista, 30);
    printf("Lista após insercoes: ");
    percorrer(lista);
    lista = inserirPos(lista, 25, 1);
    printf("Lista após inserir 25 na posicao 1: ");
    percorrer(lista);
    // Buscando um elemento
    int valor = 20;
    struct No *busca = buscar(lista, valor);
    if (busca != NULL)
    {
        printf("Elemento %d encontrado!\n", valor);
    }
    else
    {
        printf("Elemento %d não encontrado!\n", valor);
    }
    // Removendo um elemento
    lista = remover(lista, 20);
    printf("Lista após remocao do elemento 20: ");
    percorrer(lista);
    // Liberando a lista
    liberarLista(lista);
    return 0;
}
