package negocio.excecao;

public class PerfilUsuarioInexistenteException extends UsuarioException {

    public PerfilUsuarioInexistenteException(String msg) {
        super("\nTipo de usuário inválido! Escolha a opção 1 ou 2.");
    }

}
