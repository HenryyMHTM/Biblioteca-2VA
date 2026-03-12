package iu;
import fachada.Biblioteca;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import negocio.entidades.Livro;
import negocio.entidades.Usuario;
import negocio.excecao.CPFApenasNumerosException;
import negocio.excecao.CPFTamanhoException;
import negocio.excecao.LimiteLivrosExcedidosException;
import negocio.excecao.LivroNaoExisteException;
import negocio.excecao.MultaPendenteException;

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

    private void realizarLogin(Biblioteca fachada, Scanner sc) throws CPFTamanhoException, CPFApenasNumerosException {
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
            String perfil = (u instanceof negocio.entidades.Aluno) ? "Aluno" : "Professor";
            
            System.out.println("\n========================================");
            System.out.println("      MENU " + u.getNome());
            System.out.println("      Perfil: " + perfil);
            System.out.println("========================================");
            System.out.printf("Saldo de Multas: R$ %.2f\n", u.getMultaAcumulada());
            System.out.println("Livros em posse: " + u.getQuantidadeLivros() + " de " + u.getLimiteEmprestimo());
            System.out.println();
            System.out.println("1. Ver meus livros emprestados");
            System.out.println("2. Verificar atrasos e Pagar Multas");
            System.out.println("3. Pegar novo livro (Empréstimo)");
            System.out.println("4. Devolver um livro");
            System.out.println("0. Sair (Logout)");
            System.out.println("----------------------------------------");
            System.out.print("Opção: ");

            try {
                op = Integer.parseInt(sc.nextLine());
                
                switch (op) {
                    case 1 -> {
                        System.out.println("\n--- MEUS EMPRÉSTIMOS ---");
                        if (u.getLivrosEmprestados().isEmpty()) {
                            System.out.println("Tu não tem nenhum livro agora.");
                        } else {
                            System.out.println("------------------------------------------------------------");
                            System.out.printf("%-25s | %-14s | %s\n", "TÍTULO", "ISBN", "DATA DEVOLUÇÃO");
                            System.out.println("------------------------------------------------------------");
                            
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            for (Livro l : u.getLivrosEmprestados()) {
                                String dataStr = (l.getDataDevolucao() != null) ? l.getDataDevolucao().format(formatter) : "N/A";
                                String titulo = l.getTitulo();
                                if (titulo.length() > 22) titulo = titulo.substring(0, 19) + "...";
                                 
                                System.out.printf("%-25s | %-14s | %s\n", titulo, l.getIsbn(), dataStr);
                            }
                            System.out.println("------------------------------------------------------------");
                        }   System.out.print("\nEnter pra voltar.");
                        sc.nextLine();
                    }
                    case 2 -> verificarMultas(fachada, u, sc);
                    case 3 -> {
                        System.out.println("\n--- NOVO EMPRÉSTIMO ---");
                        // ja era o limite
                        if (u.getQuantidadeLivros() >= u.getLimiteEmprestimo()) {
                            throw new LimiteLivrosExcedidosException(u.getQuantidadeLivros(), u.getLimiteEmprestimo());
                        }   // ve se o cara ta devendo ou com livro atrasado
                        boolean temAtraso = false;
                        for (Livro l : u.getLivrosEmprestados()) {
                            if (l.getDataDevolucao() != null && LocalDate.now().isAfter(l.getDataDevolucao())) {
                                temAtraso = true;
                                break;
                            }
                        }   if (temAtraso || u.getMultaAcumulada() > 0) {
                            throw new MultaPendenteException(u.getMultaAcumulada());
                        }   int podePegar = u.getLimiteEmprestimo() - u.getQuantidadeLivros();
                        System.out.println("Status: Tu tem " + u.getQuantidadeLivros() + " livros. Pode pegar mais " + podePegar + ".");
                        System.out.print("\nISBN do livro: ");
                        fachada.emprestar(sc.nextLine(), u);
                        System.out.println("SUCESSO! Reservado.");
                    }
                    case 4 -> devolverLivro(fachada, u, sc);
                    default -> {
                    }
                }
            } catch (Exception e) { 
                System.out.println("\nErro: " + e.getMessage()); 
            }
        }
    }
    private void verificarMultas(Biblioteca fachada, Usuario u, Scanner sc) {
        System.out.println("\n--- STATUS DE MULTAS ---");
        //mostra o que já está gravado no CSV
        System.out.println("Saldo de Multa Acumulada: R$ " + u.getMultaAcumulada()); //
        
        if (u.getMultaAcumulada() > 0) {
            System.out.print("Deseja realizar o pagamento agora? (S/N): ");
            if (sc.nextLine().equalsIgnoreCase("S")) {
                fachada.pagarMulta(u); //
                System.out.println("Pagamento realizado! Seu saldo está zerado.");
            }
        } else {
            System.out.println("Você não possui multas pendentes.");
        }
    }

    private void devolverLivro(Biblioteca fachada, Usuario u, Scanner sc) {
        System.out.println("\n--- DEVOLUÇÃO DE LIVRO ---");
        System.out.print("Digite o ISBN do livro: ");
        String isbn = sc.nextLine();

        try {
            fachada.devolverLivro(isbn, u); 
            System.out.println("Livro devolvido com sucesso!");

            //oferece o pagamento caso o usuário tenha multa após devolução
            if (u.getMultaAcumulada() > 0) {
                System.out.printf("\nATENÇÃO: Você possui R$ %.2f em multas.\n", u.getMultaAcumulada());
                System.out.print("Deseja quitar esse valor agora? (S/N): ");
                
                if (sc.nextLine().equalsIgnoreCase("S")) {
                    fachada.pagarMulta(u);
                    System.out.println("Multa paga! Seu saldo foi zerado.");
                }
            }
        } catch (LivroNaoExisteException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }
}