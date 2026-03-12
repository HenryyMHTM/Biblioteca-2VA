package negocio.excecao;

public class FormatoDataNascimentoException extends UsuarioException{

    public FormatoDataNascimentoException(String msg) {
        super("\nErro: Formato incorreto. Use dd/mm/aaaa.");
    }

}
