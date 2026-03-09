package negocio.excecao;

public class MultaPendenteException extends Exception {
    private double valorPendente;
    public MultaPendenteException(double valor) {
        super("\nEmpréstimo de livro(s) negado. Você possui R$ " + valor + " em multas pendentes.");
        this.valorPendente = valor;
    }
    public double getValorPendente () {
        return valorPendente;
    }
}
