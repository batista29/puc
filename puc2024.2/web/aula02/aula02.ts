// //tipos de estruturados
// type Pessoa = {
//     nome: string;
//     idade: number;
// };

// //jeito comum de inicializar uma
// //variavel do tipo pessoa
// var p1: Pessoa = {
//     nome: "João",
//     idade: 20,
// };

// var p2: Pessoa = {
//     nome: "Pedro",
//     idade: 19
// }

// //definindo outro tipoo para treinar

// type Disciplina = {
//     nome: string;
//     cargaHoraria: number;
//     nomeProfessor?: string | undefined;
// }

// var disc1: Disciplina = {
//     nome: "Web",
//     cargaHoraria: 40
// }

// disc1.nomeProfessor = "Mateus";
// disc1.nomeProfessor = undefined;

////exemplo de undefined
////o qu eé undefined?? uma variavel declarada e nao atribuida
// var xpto;

// console.log(xpto)

type Credencial = {
    email: string;
    senha: string;
};

function autenticar(c: Credencial): boolean {
    return ((c.email === "admin@dominio.com.br") &&
        (c.senha === "1234"))
}

var c1: Credencial = {
    email: "teste",
    senha: '1234'
}

const r1: boolean = autenticar(c1);
console.log("Teste login 1: " + r1);

var c2: Credencial = {
    email: "admin@dominio.com.br",
    senha: '1234'
}

const r2: boolean = autenticar(c2);
console.log("Teste login 2: " + r2);

/*
        EXECICIO

        CRIAR 10 CREDENCIAIS E COLOCA-LAS EM UM ARRAY
        CHAMADO 'BLOQUEADAS' QUE NÃO PODEM FAZER LOGIN
        CRIAR MAIS 10 CREDENCIAIS DISTINTAS E COLOCA-LAS EM OUTRO ARRAY/VETOR
        CHAMADO 'VALIDAS'

        ATENÇÃO: VOCE TERA 2 VETORES, UM VETOR COM CREDENCIAIS VALIDAS
        E OTRO VETOR COM CREDENCIAIS INVALIDAS

        MODIFICAR A FUNÇÃO LOGIN PARA QUE AO RECEBER UMA CREDENCIAL 
        SE ELA FOR ENCONTRADA NO VETOR VALIDAS, RETORNE TRUE
        SE ELA FOR ENCONTRADA NO VETOR BLOQUEADAS, RETORNE False

        EEEEE SE NAO FOR ENCONTRADA EM NENHUM DOS VETORES RETORNA FALSE TAMBEM

        CORREÇÃO SEMANA QUE VEM !!!!!!!!!!!!!!
 */