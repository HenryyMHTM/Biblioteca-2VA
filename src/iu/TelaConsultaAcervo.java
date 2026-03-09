package iu;
import fachada.Biblioteca;
import negocio.entidades.Livro;
import java.util.List;
import java.util.Scanner;

public class TelaConsultaAcervo {

    @SuppressWarnings("resource")
    public void exibir(Biblioteca fachada) {
        Scanner sc = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- CONSULTAR ACERVO ---");
            System.out.println("1. Listar todos os livros");
            System.out.println("2. Buscar por ISBN");
            System.out.println("3. Buscar por Título");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());

                if (opcao == 1) {
                    List<Livro> todos = fachada.listarTodosLivros();
                    if (todos.isEmpty()) System.out.println("O acervo está vazio.");
                    else for (Livro l : todos) imprimirLivro(l);
                } 
                else if (opcao == 2) {
                    System.out.print("Digite o ISBN: ");
                    Livro achado = fachada.buscarLivroPorIsbn(sc.nextLine());
                    if (achado != null) imprimirLivro(achado);
                    else System.out.println("Nenhum livro encontrado com esse ISBN.");
                } 
                else if (opcao == 3) {
                    System.out.print("Digite o Título (ou parte dele): ");
                    List<Livro> achados = fachada.buscarLivroPorTitulo(sc.nextLine());
                    if (!achados.isEmpty()) for (Livro l : achados) imprimirLivro(l);
                    else System.out.println("Nenhum livro encontrado com esse Título.");
                } 
                else if (opcao != 0) {
                    System.out.println("Erro: Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas números.");
            }
        }
    }

    private void imprimirLivro(Livro l) {
        System.out.println("\n[ISBN: " + l.getIsbn() + "] " + l.getTitulo() + " - " + l.getAutor() + " (" + l.getAno() + ")");
    }
}