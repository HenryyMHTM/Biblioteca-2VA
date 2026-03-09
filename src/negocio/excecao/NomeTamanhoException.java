package negocio.excecao;

public class NomeTamanhoException extends UsuarioException{
    public NomeTamanhoException (String nome) {
        super(nome.length() < 3 ? "\nO nome "+nome+" tem menos que 3 caracteres" : "O nome "+nome+" tem mais que 30 caracteres\n");
    }
}
