package negocio.entidades;

// herda de usuario
public class Aluno extends Usuario {
    public Aluno(String cpf, String nome, String dataNascimento) {
        super(cpf, nome, dataNascimento);
    }

    @Override
    public int getLimiteEmprestimo() {
        return 3; // limite do pdf
    }
}