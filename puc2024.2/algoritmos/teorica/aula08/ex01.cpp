#include <iostream>>
using namespace std;

int ehPar(int num)
{
    return (num % 2 == 0);
}

int main()
{
    int num;
    cout << "Digite um numero" << endl;
    cin >> num;
    if (ehPar(num))
    {
        cout << "E par";
    }
    else
    {
        cout << "Nao e par";
    }
}