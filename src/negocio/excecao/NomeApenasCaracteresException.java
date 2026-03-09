package negocio.excecao;

public class NomeApenasCaracteresException extends UsuarioException {
    public NomeApenasCaracteresException (){
        super("Por favor, digite apenas letras em nome");
    }
}
