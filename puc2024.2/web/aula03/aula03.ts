/*
ESTE ARTQUIVO EM TYPE É O PONTO DE ENTRADA DO BACKEND
ISSO SIGNIFICA QUE UM BACKEND PRECISA INICIALIZAR UM SERVIDOR WEB ACEITAR REQUISIÇÕES HTTP
E DEVOLVER RESPOSTAS HTTP E TER ROTAS DE SERVIÇOS, ISSO É UM SERVER 

ENTENDENDO OQUE SAO SERVIÇOS
PARA NOS, INICIALMENTE, UM SERVIÇO É UMA ROTA OU SEJA TEM UM ENDEREÇO,
EXEMPLO: /CalcularSeno, todo serviço recebe uma requisição http, processa oque vem nela e pode ou não devolver uma resposta

O servidor wweb opera no protocolo http, o protocolo http, que é o protocolo de apçlicações, funciona com partes basicas,
requisição: oque chega no serviço, verbo/modo de operação (como os dados vão carregados na requisição, e por fim a resposta)

Quando criamos um servidor ele ja disponibiliza uma rota default, que é a rota padrão, que não tem nome, é representada por / (barra web)
*/



// Importaremos o pacote http completo
import http from "http";

//rota principal, que é /
function rotaDefault(requisicao: http.IncomingMessage,
    resposta: http.ServerResponse) {
    resposta.writeHead(200, { 'Content-Type': 'text/plain' });
    resposta.end('Oi, servidor do Nata, na rota default\n');
}

//criei um objeto tipo servidor
const server = http.createServer(rotaDefault);

/*chamar a função listen, colocar no ar o servidor,
listen significa que o servidor vai escutar requisições em uma determinada porta*/

server.listen(3000, () => {
    console.log("Servidor funcionando");
})







//Tarefa
/*
CRIAR 3 ROTAS
/CalcularSeno
/CalcularCoseno
/CalcularTangente

Cada rota é um serviço e deve ter o calculo,
seu desafio é receber na requisição o dado para calculo,
resolver o calculo e devolver a resposta
*/
