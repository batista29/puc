#include <iostream>
using namespace std;

int main()
{
    int vetorX[20] = {685, 842, 557, 367, 42, 326, 852, 455, 393,
                      587, 404, 467, 699, 482, 178, 712, 791, 933, 675, 359},
        vetorY[20] = {746, 417, 17, 905, 693, 559, 209, 975, 624, 27,
                      963, 777, 938, 267, 412, 829, 720, 691, 481, 698},
        vetorZ[20];

    for (int i = 0; i < 20; i++)
    {
        vetorZ[i] = vetorX[i] * vetorY[i];
        cout << vetorZ[i] << "\t";
    }
}