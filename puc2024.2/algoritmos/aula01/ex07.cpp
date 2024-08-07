#include <iostream>

int main()
{
    double imc, altura, peso;
    std::cout << "Digite a sua altura (em metros) e o seu peso" << std::endl;
    std::cin >> altura >> peso;
    imc = peso/(altura*altura);
    if(imc <= 16){
        std::cout << "magreza grave" << std::endl;
    }else if(imc >= 16 && imc < 17){
        std::cout << "magreza moderada" << std::endl;
    }else if(imc >= 17 && imc < 18.6){
        std::cout << "magreza leve" << std::endl;
    }else if(imc >= 18.6 && imc < 25){
        std::cout << "Peso ideal" << std::endl;
    }else if(imc >=25 && imc < 30){
        std::cout << "sobrepeso" << std::endl;
    }else if(imc >= 30 && imc < 35){
        std::cout << "obesidade grau I" << std::endl;
    }else if(imc >= 35 && imc < 40){
        std::cout << "obesidade grau II" << std::endl;
    }else{
        std::cout << "obesidade grau III" << std::endl;
    }
}