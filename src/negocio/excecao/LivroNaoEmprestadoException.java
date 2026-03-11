package negocio.excecao;

public class LivroNaoEmprestadoException extends Exception {
    public LivroNaoEmprestadoException() {
        super("\nEste livro já consta como disponível na biblioteca.");
    }
}