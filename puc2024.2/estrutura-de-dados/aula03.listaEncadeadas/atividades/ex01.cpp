#include <iostream>
#include <stdio.h>
#include <stdlib.h>

using namespace std;

int fat(int n)
{
    if (n == 0)
        return 1;
    else
        return n * fat(n - 1);
}

int main()
{
    int n = 0;

    cout << "Digite o valor: " << endl;
    cin >> n;

    cout << fat(n);
}