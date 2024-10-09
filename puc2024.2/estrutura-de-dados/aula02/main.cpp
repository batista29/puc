#include <iostream>
using namespace std;

int fatorial(int num)
{
    int fat = 0;
    if (num <= 1)
    {
        return 1;
    }
    else
    {
        fat = num * fatorial(num - 1);
    }

    return fat;
}

int main()
{
    int num, fat;

    cout << "Digite um numero" << endl;
    cin >> num;
    fat = fatorial(num);

    cout << "Fatorial de " << num << " = " << fat;
}