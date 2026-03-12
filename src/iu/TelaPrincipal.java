package iu;
import fachada.Biblioteca;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import negocio.entidades.Livro;
import negocio.entidades.Usuario;
import negocio.excecao.CPFApenasNumerosException;
import negocio.excecao.CPFTamanhoException;

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
                switch (opcao) {
                    case 1 -> realizarLogin(fachada, sc);
                    case 2 -> new TelaCadastroUsuario().exibir(fachada);
                    case 3 -> new TelaConsultaAcervo().exibir(fachada);
                    default -> {
                    }
                }
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
                    case 3 -> emprestarLivro(fachada, u, sc);
                    
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

    private void emprestarLivro(Biblioteca fachada, Usuario u, Scanner sc) {
        System.out.println("\n--- PEGAR NOVO LIVRO ---");
        try {
            List<Livro> disponiveis = fachada.listarLivros(); 
            if (disponiveis.isEmpty()) {
                System.out.println("Não há livros disponíveis no momento.");
                return;
            }

            //exibe a lista com índice
            for (int i = 0; i < disponiveis.size(); i++) {
                System.out.println((i + 1) + ". " + disponiveis.get(i).getTitulo());
            }
            System.out.println("0. Voltar");
            System.out.print("Escolha o número do livro: ");

            int escolha = Integer.parseInt(sc.nextLine());

            if (escolha == 0) return;

            if (escolha > 0 && escolha <= disponiveis.size()) {
                //pega o isbn do livro
                String isbnSelecionado = disponiveis.get(escolha - 1).getIsbn();
                //valida a situação do usuário para o emprestimo
                fachada.emprestar(isbnSelecionado, u);

                System.out.println("\nEmpréstimo realizado com sucesso!");
                System.out.println("O livro '" + disponiveis.get(escolha - 1).getTitulo() + "' agora está com você.");
            } else {
                System.out.println("Opção inválida.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas o número da opção.");
        } catch (Exception e) {
            System.out.println("\n[!] Não foi possível realizar o empréstimo: " + e.getMessage());
        }
    }

    private void devolverLivro(Biblioteca fachada, Usuario u, Scanner sc) {
        System.out.println("\n--- DEVOLUÇÃO DE LIVRO ---");

        //checa se o usuário tem livros para devolução
        List<Livro> meusLivros = u.getLivrosEmprestados(); //

        if (meusLivros.isEmpty()) {
            System.out.println("Você não possui livros pendentes de devolução.");
            return;
        }

        //faz uma lista de livros com índices
        System.out.println("Selecione o livro que deseja devolver:");
        for (int i = 0; i < meusLivros.size(); i++) {
            Livro l = meusLivros.get(i); //
            System.out.println((i + 1) + ". " + l.getTitulo() + " (ISBN: " + l.getIsbn() + ")");
        }
        System.out.println("0. Cancelar");
        System.out.print("Escolha: ");

        try {
            int escolha = Integer.parseInt(sc.nextLine());

            if (escolha == 0) return;

            if (escolha > 0 && escolha <= meusLivros.size()) {
                //pega o ISBN automaticamente com base na escolha
                String isbnSelecionado = meusLivros.get(escolha - 1).getIsbn(); 

                //faz a devolução
                fachada.devolverLivro(isbnSelecionado, u); //
                System.out.println("Livro devolvido com sucesso!");

                // Oferece o pagamento de multa se houver (lógica que discutimos antes)
                if (u.getMultaAcumulada() > 0) {
                     System.out.printf("\nATENÇÃO: Você possui R$ %.2f em multas.\n", u.getMultaAcumulada());
                    System.out.print("Deseja quitar esse valor agora? (S/N): ");
                    
                    if (sc.nextLine().equalsIgnoreCase("S")) {
                        fachada.pagarMulta(u);
                        System.out.println("Multa paga! Seu saldo foi zerado.");
                    }
                }
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas o número da opção.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}