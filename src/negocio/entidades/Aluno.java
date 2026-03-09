package negocio.entidades;


// herda de usuario
public class Aluno extends Usuario {
    public Aluno(String cpf, String nome, String dataNascimento, double multaAcumulada) {
        super(cpf, nome, dataNascimento, multaAcumulada);
    }

    @Override
    public int getLimiteEmprestimo() {
        return 3; // limite do pdf
    }
    
}