/*
Fazer um programa que calcule a velocidade de um veiculo,
de acordo com a formula: delta S sobre delta T
*/

//Solução 1
/*
function print(mensagem: any) {
    console.log(mensagem);
}

function calculo(distancia: number, horas: number) {
    print("Media de " + distancia / horas + "km/h");
}

const distancia: number = 75;
const horas: number = 2;
calculo(distancia, horas);
*/


function print(mensagem: any) {
    console.log(mensagem);
}

function calculo(distancia: number, minutos: number) {
    var horas: number = 0;
    var media: number = 0;
    if (minutos > 60) {
        var media = distancia / (minutos / 60);
        horas = Math.trunc(minutos / 60);
        minutos = minutos % 60;
        if (minutos > 0) {
            print(`Foram andados ${distancia}km em ${horas} hora(s) e ${minutos} minuto(s)\nMedia de ${media}km/h`);
        } else {
            print(`Foram andados ${distancia}km em ${horas} hora(s)\nMedia de ${media}km/h`);
        }
    } else {
        var media = distancia / minutos;
        print(`Foram andados ${distancia}km em ${minutos} minuto(s)\nMedia de ${media}km/m`);
    }
}

const distancia: number = 1164;
const minutos: number = 900;
calculo(distancia, minutos);