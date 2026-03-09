package negocio.excecao;
@SuppressWarnings("serial")
public class UsuarioJaExisteException extends UsuarioException{

    public UsuarioJaExisteException(String msg) {
        super("\nEste usuário já está cadastrado.");
    }

}
