package negocio.excecao;


public class CPFTamanhoException extends UsuarioException{
    public CPFTamanhoException (String cpf) {
        super(cpf.length() > 11 ? "\nO CPF "+cpf+" tem mais que 11 números!": "\nO CPF "+cpf+" tem menos de 11 números!");
    }
}
