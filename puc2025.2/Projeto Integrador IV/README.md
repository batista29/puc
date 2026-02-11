# 🐾 PetCare  
Seu app inteligente para conectar tutores e veterinários 🐶🐱

---

## 📱 Sobre o Projeto

**PetCare** é um aplicativo mobile desenvolvido em **Kotlin (Android)** com o objetivo de facilitar o agendamento de consultas veterinárias. Ele conecta tutores, clínicas e profissionais autônomos em uma plataforma intuitiva, moderna e eficiente.

Este projeto foi desenvolvido como parte do **Projeto Integrador IV** do curso de **Engenharia de Software – PUC Campinas**.

---

## 🎯 Objetivos

- Centralizar busca, agendamento e pagamento de serviços veterinários  
- Garantir transparência de preços e planos de saúde aceitos  
- Permitir teleconsultas e comunicação prática entre tutores e profissionais  
- Melhorar a gestão e visibilidade de clínicas e veterinários  

---

## 🧩 Principais Funcionalidades (MVP)

- ✅ Cadastro e autenticação de usuários  
- ✅ Cadastro de clínicas e veterinários  
- ✅ Busca inteligente com filtros avançados  
- ✅ Perfis detalhados com avaliações  
- ✅ Agendamento online de serviços  
- ✅ Notificações automáticas  
- ✅ Sistema de favoritos  
- ✅ Canal de suporte (FAQ / chat)  

---

## 🧱 Arquitetura do Projeto

<img width="653" height="311" alt="image" src="https://github.com/user-attachments/assets/2f40113f-0833-4224-aef4-88acb6c6c809" />

---

## Diagrama

<img width="1230" height="721" alt="image" src="https://github.com/user-attachments/assets/db039b43-347a-4821-a60d-ff7ffbdf7469" />

---


## ⚙️ Tecnologias Utilizadas

| Categoria       | Ferramenta              |
|----------------|--------------------------|
| Linguagem       | Kotlin                   |
| IDE             | Android Studio           |
| UI              | Jetpack Compose          |
| Backend Servidor| Java                     |
| Banco de dados  | MongoDB                  |
| Versionamento   | GitHub                   |
| Design          | Figma                    |
| Planejamento    | Git Project              |

---

## 🚀 Como Executar o Projeto

1. Clone o repositório:  
   git clone https://github.com/batista29/PI4.git

2. *🔑 Configurar API Key (Importante):*
   Para que o Google Maps funcione, abra o arquivo local.properties na raiz do projeto (se não existir, crie um) e adicione sua chave de API:
   ```properties
   MAPS_API_KEY=Sua_Chave_Google_Maps_Aqui
3.Abra o projeto no Android Studio

4.Sincronize o Gradle

5.Antes de rodar, configure o Servidor Backend (instruções abaixo).

6.Execute no emulador ou dispositivo físico

---
## 🖥 Como Rodar o Servidor (Backend)

Para que as funcionalidades de comunicação (como o Chatbot) funcionem, é necessário rodar o servidor Java localmente. Siga os passos abaixo no seu terminal (CMD ou PowerShell).

1. Navegar até a pasta do servidor
   Entre no diretório onde o código do servidor está localizado (ajuste o caminho conforme onde você clonou o projeto):


    cd .\PI_IV_ES_TIME15\Servidor\untitled\
2. Compilar o código
   Compile as classes Java, garantindo que a biblioteca do MongoDB seja incluída:

 ```bash
javac -cp "lib\mongo-java-driver-3.12.14.jar" -d bin src\*.java
```
3. ⚠ Configurar o IP (Importante)
   Antes de rodar o app no celular, você precisa informar ao Android qual é o IP do seu computador na rede local.

No terminal, execute o comando para ver seu IP:


```bash
ipconfig
```
Procure pelo Endereço IPv4 (ex: 192.168.1.1 ou 172.16...).

No projeto Android (Android Studio), abra o arquivo: app/src/main/java/com/example/petcare/viewmodel/ChatBotViewModel.kt

Atualize a variável private val IP com esse número.

4. Executar o Servidor
   Com tudo pronto, inicie o servidor:
```bash
java -cp "bin;lib\mongo-java-driver-3.12.14.jar" Servidor
```

Nota: Mantenha o terminal aberto rodando o servidor enquanto usa o aplicativo. O celular e o computador devem estar conectados à mesma rede Wi-Fi.

---


## 🧑‍💻 Equipe de Desenvolvimento

- Gabriel de Oliveira Mansur  
- Geovana Barbosa  
- Mariana Costa Gonçalves da Silva  
- Natã Batista Fernandes  
- Sofia de Abreu Guimarães

---

## 📅 Planejamento (Git Project)

O desenvolvimento segue as seguintes fases:

1. Planejamento e análise  
2. Design e prototipação  
3. Implementação das telas principais  
4. Integração com backend e banco de dados  
5. Testes e entrega final  

---


## 📋 Perguntas Frequentes (FAQ)
❓ Pergunta 1
Como faço para agendar uma consulta pelo aplicativo? 
Resposta: Para agendar uma consulta, acesse a aba "Agendamentos", escolha o tipo de serviço desejado (ex: consulta, banho, tosa), selecione o veterinário e o horário disponível, e confirme o agendamento.

❓ Pergunta 2
- Posso cancelar ou reagendar uma consulta? 
- Resposta: Sim! Vá até "Meus Agendamentos", selecione a consulta desejada e clique em "Cancelar" ou "Reagendar". O sistema mostrará as novas datas e horários disponíveis.

❓ Pergunta 3
- Como adiciono meu pet no aplicativo? 
- Resposta: Na tela inicial, toque em "Meus Pets" e depois em "Adicionar Pet". Informe o nome, espécie, raça, idade e outras informações importantes. Assim, os dados ficarão salvos para futuros agendamentos.

❓ Pergunta 4
- Receberei lembretes sobre as consultas do meu pet? 
- Resposta: Sim! O aplicativo envia notificações automáticas lembrando sobre consultas, vacinas e retornos agendados, para você não esquecer nenhum compromisso do seu pet.

❓ Pergunta 5
- O aplicativo tem suporte ou atendimento ao cliente? 
- Resposta: Sim! Basta acessar o menu "Ajuda" e selecionar "Falar com o suporte". Nossa equipe responderá suas dúvidas pelo chat ou e-mail.

---

## Protótipo
Link do protótipo: https://www.figma.com/design/k8jVD6K4Vfi8A8tGXvfZft/Projeto-Integrador-IV?node-id=0-1&p=f&t=raeAvyfilRQmsynX-0
---

## 📄 Licença

Este projeto é de uso acadêmico e sem fins comerciais.



