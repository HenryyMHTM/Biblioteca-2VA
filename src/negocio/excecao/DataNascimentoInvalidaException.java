package negocio.excecao;

public class DataNascimentoInvalidaException extends UsuarioException {

    public DataNascimentoInvalidaException(String msg) {
        super("\nData de nascimento inválida");
    }

}
