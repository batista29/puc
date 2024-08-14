function print(qualquerCoisa: any) {
    console.log(qualquerCoisa);
}

/*
const nome = "Natã Batista Fernandes";
var idade = 18;
idade = 19;

console.log(nome);
 

const numero = 10;
if(numero%2 != 0){
    console.log("O numero "+numero+" é impar");
}else{
     console.log("O numero "+numero+" é par");
}
*/

// Comprimento da circuferencia
// Calculo da area do circulo
// 2 piR (R e raio)
// const pi = 3.14159235;
// const R = 10;
// const comprimento = 2*pi*R;
// console.log(comprimento);

// Solução 2
// const R = 10;
// const pi = 3.14159235;
// const comprimento = 2*pi*R;
// console.log("Comprimento da circuferencia: "+comprimento);

const R = 10;
const pi = 3.14159265;
const area = pi * (R * R);
print("Area do circulo: " + area);

/*
Fazer um programa que calcule a velocidade de um veiculo,
de acordo com a formula: delta S sobre delta T
*/

// Transformar esse programa em uma função de velocidade media