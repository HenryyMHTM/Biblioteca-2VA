package negocio.entidades;

import java.util.ArrayList;

public class Professor extends Usuario {
    public Professor(String cpf, String nome, String dataNascimento,ArrayList<Livro> livrosEmprestados, double multaAcumulada) {
        super(cpf, nome, dataNascimento, livrosEmprestados, multaAcumulada);
    }

    @Override
    public int getLimiteEmprestimo() {
        return 5; // prof pega mais
    }

    

    
}