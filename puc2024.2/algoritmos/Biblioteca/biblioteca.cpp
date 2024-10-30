#include <iostream>
#include <cstring>
#include <ctype.h>
#include <stdio.h>
using namespace std;

struct Livro
{
    char titulo[100], autor[100];
    int numero_pags, ano_publi, isbn, qtd_disponivel, qtd_emprestimo;
    char pessoas[10][100];
};

void consulta_livro_isbn(int isbn, int *qtd_livros, struct Livro livros[])
{
    bool achou = false;
    int posicao;

    for (int i = 0; i < *qtd_livros; i++)
    {
        if (livros[i].isbn == isbn)
        {
            posicao = i;
            achou = true;
        }
    }

    if (achou == true)
    {
        cout << "Livro encontrado:" << endl;
        cout << "Codigo ISBN: " << livros[posicao].isbn << endl;
        cout << "Titulo: " << livros[posicao].titulo << endl;
        cout << "Autor: " << livros[posicao].autor << endl;
        cout << "Ano de publicacaoo: " << livros[posicao].ano_publi << endl;
        cout << "Numero de paginas: " << livros[posicao].numero_pags << endl;
        cout << "Quantidade disponivel: " << livros[posicao].qtd_disponivel << endl;
    }
    else
    {
        cout << "Nao foi encontrado" << endl;
    }
}

void consulta_livro_nome(char nome_livro[], int *qtd_livros, struct Livro livros[])
{
    bool achou = false;
    char livroAchado[strlen(nome_livro)];
    int posicao;

    for (int i = 0; i < *qtd_livros; i++)
    {
        // usando strlen para pegar o tamanho da string que EU escrevi, e não da capacidade de armazenamento que ela tem
        if (strlen(livros[i].titulo) == strlen(nome_livro))
        {
            for (int i = 0; i < strlen(nome_livro); i++)
            {
                if (toupper(nome_livro[i]) == toupper(livros[i].titulo[i]))
                {
                    achou = true;
                    posicao = i;
                    livroAchado[i] = toupper(livros[i].titulo[i]);
                    break;
                }
            }
        }
    }

    if (achou == true)
    {
        cout << "Livro encontrado:" << endl;
        cout << "Codigo ISBN: " << livros[posicao].isbn << endl;
        cout << "Titulo: " << livros[posicao].titulo << endl;
        cout << "Autor: " << livros[posicao].autor << endl;
        cout << "Ano de publicacaoo: " << livros[posicao].ano_publi << endl;
        cout << "Numero de paginas: " << livros[posicao].numero_pags << endl;
        cout << "Quantidade disponivel: " << livros[posicao].qtd_disponivel << endl;
    }
    else
    {
        cout << "Nao foi encontrado" << endl;
    }
}

void todos_livros(int *qtd_livros, struct Livro livros[])
{
    cout << "Livros da biblioteca: \n\n";

    for (int i = 0; i < *qtd_livros; i++)
    {
        cout << "\nCodigo ISBN: " << livros[i].isbn << endl;
        cout << "Titulo: " << livros[i].titulo << endl;
        cout << "Autor: " << livros[i].autor << endl;
        cout << "Ano de publicacaoo: " << livros[i].ano_publi << endl;
        cout << "Numero de paginas: " << livros[i].numero_pags << endl;
        cout << "Quantidade disponivel: " << livros[i].qtd_disponivel << endl;
    }
}

void ver_livros(int *qtd_livros, struct Livro livros[])
{
    /* Natã - feito */
    int opcao = 0;

    if (*qtd_livros >= 1)
    {
        while (opcao != 3)
        {
            cout << "\n\nEscolha uma opcao:\n1- Ver todos os livros\n2- Procurar livro especifico\n3- Sair" << endl;
            cin >> opcao;
            switch (opcao)
            {
            case 1:
                todos_livros(qtd_livros, livros);
                break;
            case 2:
                int procurar, codigo_livro;
                cout << "\nProcurar por:\n1-Nome\n2-Codigo ISBN" << endl;
                cin >> procurar;
                if (procurar == 1)
                {
                    cout << "Qual o nome do livro?" << endl;
                    char nome_livro[100];
                    cin >> nome_livro;

                    consulta_livro_nome(nome_livro, qtd_livros, livros);
                }
                else if (procurar == 2)
                {
                    cout << "Qual o codigo ISBN do livro?" << endl;
                    cin >> codigo_livro;

                    consulta_livro_isbn(codigo_livro, qtd_livros, livros);
                }

                break;
            case 3:
                break;
            }
        }
    }
    else
    {
        cout << "Nenhum livro cadastrado no sistema" << endl;
    }
}

void emprestimos_livros(int *qtd, struct Livro l[])
{ // Ester
    int isbn;
    bool verificar = false; // val para verificar se o isbn existe no vetor de livros
    cout << "\nDigite o codigo ISBN do livro: ";
    cin >> isbn;
    for (int i = 0; i < *qtd; i++)
    {
        if (isbn == l[i].isbn)
        {
            verificar = true;
            if (l[i].qtd_disponivel == 0)
            {
                cout << "\nNao ha exemplares disponiveis no momento!!";
            }
            else
            {
                cout << "\nQuantidade de exemplares disponiveis: " << l[i].qtd_disponivel;
                cout << "\nDigite o seu nome: ";
                cin.ignore();
                int cont = l[i].qtd_emprestimo; // contador de emprestimos do livro
                cin.getline(l[i].pessoas[cont], 100);
                cout << "\nEmprestimo realizado com sucesso!";
                l[i].qtd_disponivel--;
                (l[i].qtd_emprestimo)++;
            }
            break;
        }
    }
    // verifica se o isbn é valido ou não
    if (verificar == false)
    {
        cout << "Codigo isbn invalido!";
    }
}

void devolucao(int *qtd, Livro l[])
{
    int isbn;
    char nome[100];
    bool verificar = false; // Verifica se o ISBN existe

    cout << "\nDigite o codigo ISBN do livro: ";
    cin >> isbn;

    for (int i = 0; i < *qtd; i++)
    {
        if (isbn == l[i].isbn)
        {
            verificar = true;

            cout << "\nDigite o seu nome: ";
            cin.ignore();
            cin.getline(nome, 100);

            bool nome_encontrado = false; // Flag para verificar se o nome foi encontrado

            for (int j = 0; j < l[i].qtd_emprestimo; j++)
            {
                if (strcmp(nome, l[i].pessoas[j]) == 0)
                {
                    nome_encontrado = true;
                    // Remover a pessoa e ajustar o array
                    for (int k = j; k < l[i].qtd_emprestimo - 1; k++)
                    {
                        strcpy(l[i].pessoas[k], l[i].pessoas[k + 1]);
                    }
                    // Atualizar a quantidade de empréstimos e exemplares disponíveis
                    l[i].qtd_emprestimo--;
                    l[i].qtd_disponivel++;
                    cout << "\nDevolucao realizada com sucesso!";
                    break; // Interromper após encontrar a pessoa
                }
            }
            if (nome_encontrado == false)
            {
                cout << "\nNome nao encontrado na lista de emprestimo!";
            }
            break; // Interromper após encontrar o livro
        }
    }

    if (verificar == false)
    {
        cout << "\nCodigo ISBN invalido!";
    }
}

void remocao(int *qtd_livros, struct Livro livros[])
{
    /* Natã */
    int cod;
    cout << "Digite o isbn do livro: " << endl;
    cin >> cod;

    for (int i = 0; i < *qtd_livros; i++)
    {
        if (cod == livros[i].isbn)
        {
            for (int j = i; j < *qtd_livros - 1; j++)
            {
                livros[j] = livros[j + 1];
            }
            (*qtd_livros)--;
            cout << "Livro removido" << endl;
            break;
        }
        else
        {
            cout << "Livro nao encontrado ou nao existe" << endl;
            break;
        }
    }
}

bool IdUtilizado(int id, struct Livro l[], int nlivros)
{
    for (int i = 0; i < nlivros; i++)
    {
        if (l[i].isbn == id)
            return true;
    }
    return false;
}

void cadastro_livros(int *qtd, struct Livro l[])
{
    int isbn;
    bool ok = false;
    // Ester
    cout << "Digite os dados do cadastro de livro:" << endl;

    bool idEmUso = true;
    while (idEmUso == true)
    {
        cout << "Código ISBN: ";
        cin >> isbn;
        idEmUso = IdUtilizado(isbn, l, *qtd);
        if (idEmUso)
        {
            cout << "Esse isqn estah sendo utilizado!!!\nTente outro isbn.\n";
        }
    }

    l[*qtd].isbn = isbn;

    cout << "Título: ";
    cin.ignore();
    cin.getline(l[*qtd].titulo, 100);
    cout << "Autor: ";
    cin.ignore();
    cin.getline(l[*qtd].autor, 100);
    cout << "Ano de publicação: ";
    cin >> l[*qtd].ano_publi;
    cout << "Numero de páginas: ";
    cin >> l[*qtd].numero_pags;
    cout << "Quantidade disponível: ";
    cin >> l[*qtd].qtd_disponivel;
    (*qtd)++;
    l[*qtd].qtd_emprestimo = 0;
}

int main()
{
    int opcao = 1;
    int qtd_livros = 0, qtd_emprestimos = 0;
    struct Livro livros[100];

    while (opcao != 6)
    {
        cout << "\n=== MENU ===\n"
             << endl;
        cout << "1- Cadastrar livros\n2- Consultar livros\n3- Emprestar livros\n4- Devolver livros\n5- Remover livro\n6- Sair\nEscolha uma das opcoes acima:" << endl;
        cin >> opcao;
        switch (opcao)
        {
        case 1:
            cadastro_livros(&qtd_livros, livros);
            break;
        case 2:
            ver_livros(&qtd_livros, livros);
            break;
        case 3:
            emprestimos_livros(&qtd_livros, livros);
            break;
        case 4:
            devolucao(&qtd_livros, livros);
            break;
        case 5:
            remocao(&qtd_livros, livros);
            break;
        case 6:
            break;
        }
    }
}
