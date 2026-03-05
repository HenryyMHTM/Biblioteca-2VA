package iu;
import fachada.Biblioteca;
import java.util.Scanner;

public class TelaPrincipal {

    public void iniciar(Biblioteca fachada) {
        Scanner sc = new Scanner(System.in);
        int opcao = -1;

        // loop do menu principal ate o cara digitar 0 [cite: 26]
        while (opcao != 0) {
            System.out.println("\n--- BIBLIOTECA ---"); // [cite: 23]
            System.out.println("1. Entrar (Login)"); // [cite: 24]
            System.out.println("2. Criar Conta (Cadastro)"); // [cite: 25]
            System.out.println("0. Sair"); // [cite: 26]
            System.out.print("Escolha uma opção: "); // [cite: 27]

            // try catch pra nao quebrar se botarem letra na opcao
            try {
                opcao = Integer.parseInt(sc.nextLine());

                switch (opcao) {
                    case 1:
                        // Ramon q vai fazer o login dps 
                        System.out.println("\n[Aviso: Área de login ainda em desenvolvimento]");
                        break;
                    case 2:
                        // chama a tela de cadastro q a gente fez
                        TelaCadastroUsuario telaCadastro = new TelaCadastroUsuario();
                        telaCadastro.exibir(fachada);
                        break;
                    case 0:
                        System.out.println("\nSaindo do sistema...");
                        break;
                    default:
                        System.out.println("\nErro: Opção inválida. Tenta dnv.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nErro: Digite apenas o número da opção.");
            }
        }
    }
}