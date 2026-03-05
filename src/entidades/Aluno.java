package entidades;

public class Aluno extends Usuario{

    Aluno(double CPF, String nome) {
        super(CPF, nome);
    }

    @Override
    public int limiteEmprestimo(int limite) {
        return 3;
    }

}
