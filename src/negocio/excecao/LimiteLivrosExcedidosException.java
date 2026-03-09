package negocio.excecao;

public class LimiteLivrosExcedidosException extends Exception {
    public LimiteLivrosExcedidosException(int atual, int limite) {
        super("\nLimite de empréstimos atingido para esse tipo de usuário! Você já tem "+atual+
            " livros. Limite atual: "+limite+" livros.");
    }
}
