package negocio.excecao;

public class NomeApenasCaracteresException extends UsuarioException {
    public NomeApenasCaracteresException (){
        super("\nPor favor, digite apenas letras em nome");
    }
}
