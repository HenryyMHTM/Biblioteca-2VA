package negocio.excecao;

public class UsuarioNaoLocatarioException extends Exception {
    public UsuarioNaoLocatarioException() {
        super("\nEste livro foi emprestado para outro usuário.");
    }
}