package negocio.entidades;

public class Professor extends Usuario {

    Professor(double CPF, String nome) {
        super(CPF, nome);
    }

    @Override
    public int limiteEmprestimo(int limite) {
        return 5;
    }

}
