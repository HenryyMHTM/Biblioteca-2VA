package iu;
import fachada.Biblioteca;
import java.util.Scanner;
import negocio.entidades.Usuario;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import negocio.entidades.Livro;
import negocio.excecao.LimiteLivrosExcedidosException;
import negocio.excecao.MultaPendenteException;
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
            String perfil = (u instanceof negocio.entidades.Aluno) ? "Aluno" : "Professor";
            
            System.out.println("\n========================================");
            System.out.println("      MENU: " + u.getNome());
            System.out.println("      Perfil: " + perfil);
            System.out.println("========================================");
            System.out.printf("Saldo de Multas: R$ %.2f\n", u.getMultaAcumulada());
            System.out.println("Livros em posse: " + u.getQuantidadeLivros() + " de " + u.getLimiteEmprestimo());
            System.out.println();
            System.out.println("1. Ver meus livros emprestados");
            System.out.println("2. Verificar atrasos e multas");
            System.out.println("3. Pegar novo livro (Empréstimo)");
            System.out.println("4. Devolver um livro");
            System.out.println("0. Sair (Logout)");
            System.out.println("----------------------------------------");
            System.out.print("Opção: ");

            try {
                op = Integer.parseInt(sc.nextLine());
                
                if (op == 1) {
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
                    }
                    System.out.print("\nEnter pra voltar.");
                    sc.nextLine(); 
                }
                else if (op == 2) {
                    System.out.println("\n--- STATUS DE MULTAS ---");
                    double totalMultaAtiva = 0.0;
                    boolean temAtraso = false;
                    
                    for (Livro l : u.getLivrosEmprestados()) {
                        // calculando o atraso de cada livro
                        if (l.getDataDevolucao() != null && LocalDate.now().isAfter(l.getDataDevolucao())) {
                            temAtraso = true;
                            long diasAtraso = ChronoUnit.DAYS.between(l.getDataDevolucao(), LocalDate.now());
                            double multaLivro = diasAtraso * 2.0; 
                            totalMultaAtiva += multaLivro;
                            
                            System.out.println("Livro: " + l.getTitulo());
                            System.out.println("Atraso: " + diasAtraso + " dias");
                            System.out.printf("Multa deste livro: R$ %.2f\n", multaLivro);
                            System.out.println("----------------------------------");
                        }
                    }
                    
                    double multaTotalGeral = u.getMultaAcumulada() + totalMultaAtiva;
                    System.out.printf("Total de Multa Pendente: R$ %.2f\n", multaTotalGeral);
                    
                    System.out.print("\nEnter pra voltar.");
                    sc.nextLine(); 
                }
                else if (op == 3) {
                    System.out.println("\n--- NOVO EMPRÉSTIMO ---");
                    
                    // ja era o limite
                    if (u.getQuantidadeLivros() >= u.getLimiteEmprestimo()) {
                        throw new LimiteLivrosExcedidosException(u.getQuantidadeLivros(), u.getLimiteEmprestimo());
                    }
                    
                    // ve se o cara ta devendo ou com livro atrasado
                    boolean temAtraso = false;
                    for (Livro l : u.getLivrosEmprestados()) {
                        if (l.getDataDevolucao() != null && LocalDate.now().isAfter(l.getDataDevolucao())) {
                            temAtraso = true;
                            break;
                        }
                    }
                    if (temAtraso || u.getMultaAcumulada() > 0) {
                        throw new MultaPendenteException(u.getMultaAcumulada());
                    }
                    
                    int podePegar = u.getLimiteEmprestimo() - u.getQuantidadeLivros();
                    System.out.println("Status: Tu tem " + u.getQuantidadeLivros() + " livros. Pode pegar mais " + podePegar + ".");
                    System.out.print("\nISBN do livro: ");
                    fachada.emprestar(sc.nextLine(), u);
                    System.out.println("SUCESSO! Reservado.");
                } 
                else if (op == 4) {
                    System.out.println("\n--- DEVOLUÇÃO DE MATERIAL ---");
                    System.out.print("ISBN do livro: ");
                    String isbnDevolucao = sc.nextLine();
                    
                    Livro livroPraDevolver = null;
                    for (Livro l : u.getLivrosEmprestados()) {
                        if (l.getIsbn().trim().equalsIgnoreCase(isbnDevolucao.trim())) {
                            livroPraDevolver = l;
                            break;
                        }
                    }
                    
                    System.out.println("[Verificando data...]");
                    
                    if (livroPraDevolver != null && livroPraDevolver.getDataDevolucao() != null) {
                        // facada da multa se passar do prazo
                        if (LocalDate.now().isAfter(livroPraDevolver.getDataDevolucao())) {
                            long diasAtraso = ChronoUnit.DAYS.between(livroPraDevolver.getDataDevolucao(), LocalDate.now());
                            double valorMulta = diasAtraso * 2.0; 
                            u.setMultaAcumulada(u.getMultaAcumulada() + valorMulta);
                            System.out.printf("Multa de R$ %.2f gerada (%d dias de atraso).\n", valorMulta, diasAtraso);
                        }
                        // tira da mochila do cara
                        u.removerLivro(livroPraDevolver);
                    }
                    
                    fachada.devolverLivro(isbnDevolucao, u);
                    System.out.println("Devolvido com sucesso!");
                } 
            } catch (Exception e) { 
                System.out.println("\nErro: " + e.getMessage()); 
            }
        }
    }
    }