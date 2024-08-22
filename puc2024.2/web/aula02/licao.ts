/*
        EXECICIO

        CRIAR 10 CREDENCIAIS E COLOCA-LAS EM UM ARRAY
        CHAMADO 'BLOQUEADAS' QUE NÃO PODEM FAZER LOGIN
        CRIAR MAIS 10 CREDENCIAIS DISTINTAS E COLOCA-LAS EM OUTRO ARRAY/VETOR
        CHAMADO 'VALIDAS'

        ATENÇÃO: VOCE TERA 2 VETORES, UM VETOR COM CREDENCIAIS BLOQUEADAS
        E OUTRO VETOR COM CREDENCIAIS VALIDAS

        MODIFICAR A FUNÇÃO LOGIN PARA QUE AO RECEBER UMA CREDENCIAL 
        SE ELA FOR ENCONTRADA NO VETOR VALIDAS, RETORNE TRUE
        SE ELA FOR ENCONTRADA NO VETOR BLOQUEADAS, RETORNE False

        EEEEE SE NAO FOR ENCONTRADA EM NENHUM DOS VETORES RETORNA FALSE TAMBEM

        CORREÇÃO SEMANA QUE VEM !!!!!!!!!!!!!!
 */

        var credenciais = ["@juliano", "@batista", "@lucas", "@pedro", "@leandro",
            "@marcos", "@vini", "@marcos", "@tiago", "@marcelo"];
        var validas = ["@gabriel", "@batista", "@bruno", "@pedro", "@felipe",
            "@renato", "@marcos", "@carlos", "@rafael", "@gustavo"];
        
        function autenticar(credenciais: any, validas: any) {
        
            var bloqueadas: any[10];
            var contBlock: number = 0;
        
            for (let i: number = 0; i < credenciais.length; i++) {
                for (let j: number = 0; j < credenciais.length; j++) {
                    if (credenciais[j] !== validas[i]) {
                        //erro aqui
                        bloqueadas[contBlock] = credenciais[j];
                    }
                }
            }
            console.log(bloqueadas)
        }
        
        autenticar(credenciais, validas);