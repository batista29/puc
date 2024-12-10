#include <iostream>
#include <cstdlib>
#include <cstring>
#include <cctype>
using namespace std;

// Estrutura para um contato
struct Contato
{
    char nome[50];
    char celular[15];
    struct Contato *esquerda;
    struct Contato *direita;
};

// Funcões
struct Contato *inserir(struct Contato *raiz, struct Contato *novo);
struct Contato *buscar(struct Contato *raiz, const char *busca, int modo);
struct Contato *remover(struct Contato *raiz, const char *nome);
void listar(struct Contato *raiz);
bool validarCelular(const char *celular);
bool verificarContato(struct Contato *raiz, const char *nome);
int strcasecmp(const char *s1, const char *s2);
void menu();

int main()
{
    struct Contato *raiz = nullptr;
    int opcao;
    do
    {
        menu();
        cin >> opcao;
        cin.ignore();
        switch (opcao)
        {
        case 1:
        {
            struct Contato *novo = (struct Contato *)malloc(sizeof(struct Contato));
            cout << "Nome: ";
            cin.getline(novo->nome, 50);

            // ve se o contato ta salvo
            if (verificarContato(raiz, novo->nome))
            {
                cout << "Erro: Contato com nome '" << novo->nome << "' ja existe." << endl;
                free(novo);
                break;
            }

            cout << "Celular: ";
            cin.getline(novo->celular, 15);

            // Verificar se o celular e valido
            if (!validarCelular(novo->celular))
            {
                cout << "Erro: Celular invalido. Apenas numeros sao permitidos." << endl;
                free(novo);
                break;
            }

            novo->esquerda = nullptr;
            novo->direita = nullptr;
            raiz = inserir(raiz, novo);
            cout << "Contato adicionado com sucesso!" << endl;
            break;
        }
        case 2:
        {
            // Buscar contato
            int modo;
            cout << "Buscar por:\n1. Nome\n2. Numero de celular\nEscolha uma opcao: ";
            cin >> modo;
            cin.ignore(); // Consumir a quebra de linha

            char busca[50];
            if (modo == 1)
            {
                cout << "Digite o nome para buscar: ";
            }
            else if (modo == 2)
            {
                cout << "Digite o numero de celular para buscar: ";
            }
            else
            {
                cout << "Opcao invalida!" << endl;
                break;
            }

            cin.getline(busca, 50);
            struct Contato *encontrado = buscar(raiz, busca, modo);
            if (encontrado)
            {
                cout << "Contato encontrado: " << encontrado->nome << ", Celular: " << encontrado->celular << endl;
            }
            else
            {
                cout << "Contato nao encontrado." << endl;
            }
            break;
        }
        case 3:
        {
            // Remover contato
            char nome[50];
            cout << "Nome para remover: ";
            cin.getline(nome, 50);
            raiz = remover(raiz, nome);
            break;
        }
        case 4:
        {
            // Listar contatos
            cout << "Contatos:" << endl;
            listar(raiz);
            break;
        }
        case 5:
            cout << "Saindo..." << endl;
            break;
        default:
            cout << "Opcao invalida! Tente novamente." << endl;
        }
    } while (opcao != 5);
    return 0;
}

void menu()
{
    cout << "\nMenu:\n";
    cout << "1. Adicionar contato\n";
    cout << "2. Buscar contato\n";
    cout << "3. Remover contato\n";
    cout << "4. Listar contatos\n";
    cout << "5. Sair\n";
    cout << "Escolha uma opcao: ";
}

struct Contato *inserir(struct Contato *raiz, struct Contato *novo)
{
    if (raiz == nullptr)
    {
        return novo;
    }
    if (strcasecmp(novo->nome, raiz->nome) < 0)
    {
        raiz->esquerda = inserir(raiz->esquerda, novo);
    }
    else
    {
        raiz->direita = inserir(raiz->direita, novo);
    }
    return raiz;
}

struct Contato *buscar(struct Contato *raiz, const char *busca, int modo)
{
    if (raiz == nullptr)
    {
        return nullptr;
    }

    if (modo == 1) // Buscar por nome
    {
        if (strcasecmp(busca, raiz->nome) == 0)
        {
            return raiz;
        }
    }
    else if (modo == 2) // Buscar por celular
    {
        if (strcmp(busca, raiz->celular) == 0)
        {
            return raiz;
        }
    }

    // Continuar na arvore
    struct Contato *resultadoEsquerda = buscar(raiz->esquerda, busca, modo);
    if (resultadoEsquerda)
    {
        return resultadoEsquerda;
    }

    return buscar(raiz->direita, busca, modo);
}

struct Contato *remover(struct Contato *raiz, const char *nome)
{
    if (raiz == nullptr)
    {
        cout << "Contato '" << nome << "' nao encontrado." << endl;
        return nullptr;
    }
    if (strcasecmp(nome, raiz->nome) < 0)
    {
        raiz->esquerda = remover(raiz->esquerda, nome);
    }
    else if (strcasecmp(nome, raiz->nome) > 0)
    {
        raiz->direita = remover(raiz->direita, nome);
    }
    else
    {
        if (raiz->esquerda == nullptr)
        {
            struct Contato *temp = raiz->direita;
            free(raiz);
            return temp;
        }
        else if (raiz->direita == nullptr)
        {
            struct Contato *temp = raiz->esquerda;
            free(raiz);
            return temp;
        }

        struct Contato *temp = raiz->direita;
        while (temp->esquerda != nullptr)
        {
            temp = temp->esquerda;
        }
        strcpy(raiz->nome, temp->nome);
        strcpy(raiz->celular, temp->celular);
        raiz->direita = remover(raiz->direita, temp->nome);
    }
    return raiz;
}

void listar(struct Contato *raiz)
{
    if (raiz != nullptr)
    {
        listar(raiz->esquerda);
        cout << "Nome: " << raiz->nome << ", Celular: " << raiz->celular << endl;
        listar(raiz->direita);
    }
}

bool validarCelular(const char *celular)
{
    for (int i = 0; celular[i] != '\0'; i++)
    {
        if (!isdigit(celular[i]))
        {
            return false; // Retorna falso se encontrar algo que nao seja um dígito
        }
    }
    return true; // Retorna verdadeiro se todos os caracteres forem dígitos
}

bool verificarContato(struct Contato *raiz, const char *nome)
{
    return buscar(raiz, nome, 1) != nullptr;
}

int strcasecmp(const char *s1, const char *s2)
{
    while (*s1 && *s2)
    {
        if (tolower(*s1) != tolower(*s2))
        {
            return tolower(*s1) - tolower(*s2);
        }
        s1++;
        s2++;
    }
    return tolower(*s1) - tolower(*s2);
}