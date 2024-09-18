/*
    Servidor simples
    usando o http sem express.
*/
import http from "http";

const port = 3000;
const pi: number = 3.14;

// criar duas funções, uma calcula a rea do circulo, outra o comprimento da circunferencia

function calcAreaCirculo(raio: number): number {
    return pi * raio * raio;
}

function calcComprimentoCircunferencia(raio: number): number {
    return 2 * pi * raio;
}

const server = http.createServer((req, res) => {

    if (req.url === "/calcComprimentoCircunferencia") {
        res.writeHead(200, { 'Content-type': 'text/plain' });
        const comp = calcComprimentoCircunferencia(10);
        res.end(comp.toString());
    } else if (req.url === "/calcAreaCirculo") {
        res.writeHead(200, { 'Content-type': 'text/plain' });
        const area = calcAreaCirculo(10);
        res.end(area.toString());
    } else {
        res.end('Serviço inexistente');
    }
});

server.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});