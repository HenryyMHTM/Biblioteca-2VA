package negocio.excecao;

public class LimiteLivrosExcedidosException extends Exception {
    public LimiteLivrosExcedidosException(int atual, int limite) {
        super("\nLimite excedido! Você já tem " + atual + " livros de um máximo de " + limite + ".");
    }
}