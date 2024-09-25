import express from 'express';
import { Router, Request, Response } from 'express'

const backend = express();
const route = Router();
const port = 3000;

backend.use(express.json());

//rotas de serviços
backend.post('/loginAdm', (req: Request, res: Response) => {
    //SE CHEGARAM PARAMETROS E EXISTE O PARAMETRO EMAIL E SENHA
    //EMAIL E SENHA

    const email = req.get('email');
    const senha = req.get('senha');

    if (email && senha) {
        if (email === "admin@gmail.com" && senha === "123") {
            res.send("Ola adm");
        } else {
            res.send("Você não é adminstrador");
        }
        res.send('Login realizado');
    } else {
        res.send('Faltando parametros da requisicao');
    }

})

backend.get('/signUp', (req, res) => {
    res.send('Cadastro funcionando');
});

//FIM DAS ROTAS

//Subindo servidor
backend.listen(port, () => {
    console.log(`Servidor rodando na porta: ${port}`);
})