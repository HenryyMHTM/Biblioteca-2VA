package negocio.excecao;

public class MultaPendenteException extends Exception {
    public MultaPendenteException(double valor) {
        super("\nVocê possui multas pendentes no valor de R$ " + String.format("%.2f", valor) + ". Regularize para continuar.");
    }
}