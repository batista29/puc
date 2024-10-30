# Template para criar backends usando express

Este é um projeto apenas de "esqueleto" para você criar seu backend em TypeScript usando express.

## Instalando as dependências

Ao fazer o clone do repositório, basta rodar o comando: 

```
npm install
```
O comando acima instalará todas as dependências que constam no arquivo package.json.

## Iniciando o servidor

Para garantir que seu código esteja funcionando, na raíz do projeto rode o comando: 

```
npm run build
```
Como consequência, será gerado o diretório build constando o arquivo server.js. 

Acesse o diretório onde está o arquivo server.js (build/src) e digite o comando
```
node server.js
```

## Usando o nodemon

Em muitos casos não desejamos a todo momento ficar alterando nosso código e sempre digitando diversos comandos para testar aquilo que estamos fazendo. Pensando nisso, o projeto nodemon resolve esse problema. 

Experimente o comando

```
npm run dev
```

Voilà!