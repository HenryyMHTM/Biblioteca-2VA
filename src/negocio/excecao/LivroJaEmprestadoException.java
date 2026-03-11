package negocio.excecao;

public class LivroJaEmprestadoException extends Exception {
    public LivroJaEmprestadoException() {
        // Agora sim, limpo e direto
        super("\nEste livro já está emprestado para outro usuário.");
    }
}