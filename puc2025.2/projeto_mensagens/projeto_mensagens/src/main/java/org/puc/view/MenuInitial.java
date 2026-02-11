package org.puc.view;

import org.bson.Document;
import org.puc.user.entity.UserEntity;

import java.util.Scanner;

public class MenuInitial {
    private final Scanner sc = new Scanner(System.in);

    public void exibir(UserEntity user) throws Exception {
        int opcao;

        do {
            System.out.println("\n==============================");
            System.out.println("         BEM-VINDO");
            System.out.println("==============================");
            System.out.println("1. Cadastrar");
            System.out.println("2. Login");
            System.out.println("------------------------------");
            System.out.println("0. Sair");
            System.out.println("==============================");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> {
                    System.out.println("-----------------------------");
                    System.out.println("Cadastrar");
                    System.out.println("Nome: ");
                    String nome = sc.nextLine();
                    System.out.println("Username: ");
                    String username = sc.nextLine();
                    System.out.println("Email: ");
                    String email = sc.nextLine();
                    System.out.println("Senha: ");
                    String senha = sc.nextLine();

                    UserEntity newUser = new UserEntity(nome, email, username, senha);
                    newUser.cadastrarUsuario();
                    System.out.println("Usuário cadastrado com sucesso! Faça login para continuar.");
                }
                case 2 -> {
                    System.out.println("-----------------------------");
                    System.out.println("Log-in");
                    System.out.println("Email: ");
                    String email = sc.nextLine();
                    System.out.println("Senha: ");
                    String senha = sc.nextLine();
                    user.setEmail(email);
                    user.setSenha(senha);
                    Document logged = user.logar();
                    if (!logged.isEmpty()) {
                        MenuLogin menu = new MenuLogin();
                        UserEntity userfound = new UserEntity(logged.getString("nome"), logged.getString("email"), logged.getString("username"), logged.getString("senha"));
                        menu.exibir(userfound);
                    } else {
                        System.out.println("Falha no login. Verifique suas credenciais e tente novamente.");
                    }
                }
                case 0 -> System.out.println("Encerrando o sistema...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
