package negocio.entidades;

public class Professor extends Usuario {
    public Professor(String cpf, String nome, String dataNascimento, double multaAcumulada) {
        super(cpf, nome, dataNascimento,  multaAcumulada);
    }

    @Override
    public int getLimiteEmprestimo() {
        return 5; // prof pega mais
    }

    

    
}