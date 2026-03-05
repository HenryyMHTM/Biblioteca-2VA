package negocio.entidades;

public class Professor extends Usuario {
    public Professor(String cpf, String nome, String dataNascimento) {
        super(cpf, nome, dataNascimento);
    }

    @Override
    public int getLimiteEmprestimo() {
        return 5; // prof pega mais
    }
}