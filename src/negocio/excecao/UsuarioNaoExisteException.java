package negocio.excecao;

@SuppressWarnings("serial")
public class UsuarioNaoExisteException extends UsuarioException{

    public UsuarioNaoExisteException(String msg) {
        super("\nUsuário não existe!");
    }

}
