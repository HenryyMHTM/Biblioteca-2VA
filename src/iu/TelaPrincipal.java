package iu;
import fachada.Biblioteca;
import java.util.Scanner;
import negocio.entidades.Usuario;
//import negocio.excecao.LivroNaoExisteException;

public class TelaPrincipal {

    public void iniciar(Biblioteca fachada) {
        Scanner sc = new Scanner(System.in);
        int opcao = -1;

        // loop do menu principal ate o cara digitar 0
        while (opcao != 0) {
            System.out.println("\n--- BIBLIOTECA ---"); 
            System.out.println("1. Entrar (Login)"); 
            System.out.println("2. Criar Conta (Cadastro)");
            System.out.println("3. Consultar Acervo");
            System.out.println("0. Sair"); 
            System.out.print("Escolha uma opção: ");

            
            try {
                opcao = Integer.parseInt(sc.nextLine());
                if (opcao == 1) realizarLogin(fachada, sc);
                else if (opcao == 2) new TelaCadastroUsuario().exibir(fachada);
                else if (opcao == 3) new TelaConsultaAcervo().exibir(fachada);
            } catch (Exception e) { System.out.println("Opção inválida."); }
        }
    }

    private void realizarLogin(Biblioteca fachada, Scanner sc) {
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Senha (Data Nasc.): ");
        String pass = sc.nextLine();

        try {
            Usuario logado = fachada.login(cpf, pass);
            menuLogado(fachada, logado, sc);
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private void menuLogado(Biblioteca fachada, Usuario u, Scanner sc) {
        int op = -1;
        while (op != 0) {
            System.out.println("\nBem-vindo, " + u.getNome());
            System.out.println("1. Emprestar Livro\n2. Devolver Livro\n0. Logout");
            try {
                op = Integer.parseInt(sc.nextLine());
                if (op == 1) {
                    System.out.print("ISBN do Livro: ");
                    fachada.emprestar(sc.nextLine(), u);
                    System.out.println("Sucesso!");
                } else if (op == 2) {
                    System.out.print("ISBN do Livro: ");
                    //parte para Matheus implementar (com comentário para não ficar com erro)
                    //fachada.devolver(sc.nextLine(), u);
                    System.out.println("Devolvido!");
                }
            } catch (Exception e) { System.out.println(e.getMessage()); }
        }
    }

   
}