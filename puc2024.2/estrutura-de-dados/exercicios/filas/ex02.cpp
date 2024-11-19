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

int qtd = 0;

void start()
{
    top = (struct fila *)malloc(sizeof(struct fila));

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
    qtd++;
}

void print()
{
    struct No *temp = (struct No *)malloc(sizeof(struct No));
    temp = top->first;

    while (temp != NULL)
    {
        cout << temp->dado << " | ";
        temp = temp->proximo;
    }
    cout << endl;
}

void remover15()
{
    if (qtd < 15) // Verifica se há pelo menos 15 elementos
    {
        cout << "A fila não possui 15 elementos." << endl;
        return;
    }

    struct No *temp = top->first;
    struct No *prev = NULL;

    int pos = 15; // O índice do 15º elemento (baseado em 0)

    for (int i = 0; i < pos; i++)
    {
        prev = temp;          // Atualiza o nó anterior
        temp = temp->proximo; // Avança na lista
    }

    // Agora `temp` aponta para o 15º nó e `prev` para o 14º nó
    if (prev != NULL)
    {
        prev->proximo = temp->proximo; // O nó anterior aponta para o próximo do 15º
    }
    else
    {
        // Caso esteja removendo o primeiro elemento
        top->first = temp->proximo;
    }

    if (temp == top->last)
    {
        // Caso o nó seja o último elemento
        top->last = prev;
    }

    cout << "Dado removido: " << temp->dado << endl;
    free(temp); // Libera a memória do nó removido
    qtd--;
}

int main()
{
    int valor, opcao;

    start();

    push(11);  // 0
    push(12);  // 1
    push(34);  // 2
    push(56);  // 3
    push(78);  // 4
    push(90);  // 5
    push(23);  // 6
    push(45);  // 7
    push(67);  // 8
    push(89);  // 9
    push(101); // 10
    push(123); // 11
    push(145); // 12
    push(167); // 13
    push(189); // 14
    push(202); // 15
    push(224); // 16
    push(268); // 17
    push(290); // 18
    push(312); // 19

    do
    {
        cout << "1-Inserir, 2-Print, 3-Remover numero 15" << endl;
        cin >> opcao;
        switch (opcao)
        {
        case 1:
            cout << "Digite o valor" << endl;
            cin >> valor;
            break;
        case 2:
            print();
            break;
        case 3:
            remover15();
            break;
        }

    } while (opcao != 0);
}