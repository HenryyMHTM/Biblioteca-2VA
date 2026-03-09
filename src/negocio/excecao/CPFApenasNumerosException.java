package negocio.excecao;

public class CPFApenasNumerosException extends UsuarioException {
    public CPFApenasNumerosException (String cpf){
        super("\nCPF "+cpf+" inválido, por favor, digite apenas números!\n");
    }
}
