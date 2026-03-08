package negocio.excecoes;

public class MultaPendenteException extends Exception {
    public MultaPendenteException(double valor) {
        super("\nEmpréstimo negado. Você possui R$ " + valor + " em multas pendentes. Pague a multa antes de pegar um livro!");
    }
}
