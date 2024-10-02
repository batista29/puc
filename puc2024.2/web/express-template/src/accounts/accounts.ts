import { Request, RequestHandler, Response } from "express";

/*
    Nampespace que contém tudo sobre "contas de usuários"
*/
export namespace AccountsHandler {

    /**
     * Tipo UserAccount
     */
    export type UserAccount = {
        name: string;
        email: string;
        password: string;
        birthdate: string;
    };

    // Array que representa uma coleção de contas. 
    let accountsDatabase: UserAccount[] = [];

    /**
     * Salva uma conta no banco de dados. 
     * @param ua conta de usuário do tipo @type {UserAccount}
     * @returns @type { number } o código da conta cadastrada como posição no array.
     */
    export function saveNewAccount(ua: UserAccount): number {
        accountsDatabase.push(ua);
        return accountsDatabase.length;
    }

    /**
     * Função para tratar a rota HTTP /signUp. 
     * @param req Requisição http tratada pela classe @type { Request } do express
     * @param res Resposta http a ser enviada para o cliente @type { Response }
     */
    export const createAccountRoute: RequestHandler = (req: Request, res: Response) => {
        // Passo 1 - Receber os parametros para criar a conta
        const pName = req.get('name');
        const pEmail = req.get('email');
        const pPassword = req.get('password');
        const pBirthdate = req.get('birthdate');

        if (pName && pEmail && pPassword && pBirthdate) {
            // prosseguir com o cadastro... 
            const newAccount: AccountsHandler.UserAccount = {
                name: pName,
                email: pEmail,
                password: pPassword,
                birthdate: pBirthdate
            }
            const ID = AccountsHandler.saveNewAccount(newAccount);
            res.statusCode = 200;
            res.send(`Nova conta adicionada. Código: ${ID}`);
        } else {
            res.statusCode = 400;
            res.send("Parâmetros inválidos ou faltantes.");
        }
    }

    //licao 1
    export function findAccount(email: String): boolean {
        let exists = false;
        const account = accountsDatabase.find(e => {
            if (e.email === email) {
                exists = true;
                return
            }
        });
        return exists;
    }
}

/* Exercicio 1
criar uma funcao que verifica se ja existe uma conta com aquele email no banco 

2

Na rota de criação de contas existe yum problema, resolva

3

criar uma rota chamada login que receba os parametros: email e senha e verifique se existe no banco de dados uma conta com aquele email e senha confirmados caso 
exista devolver codigo 200e o texto 'sucesso' caso contrario retornar codigo http 400 usuario e senha invalidos

4

criar uma rota chamada getall accounts que retorne unma lista de contas cadastradas todas

*/