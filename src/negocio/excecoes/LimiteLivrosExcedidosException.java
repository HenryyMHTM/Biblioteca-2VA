package negocio.excecoes;

public class LimiteLivrosExcedidosException extends Exception {
    public LimiteLivrosExcedidosException() {
        super("\nLimite de empréstimos atingido para esse tipo de usuário!");
    }
}
