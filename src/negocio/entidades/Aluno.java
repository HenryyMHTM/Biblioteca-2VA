package negocio.entidades;

import java.util.ArrayList;

// herda de usuario
public class Aluno extends Usuario {
    public Aluno(String cpf, String nome, String dataNascimento,ArrayList<Livro> livrosEmprestados, double multaAcumulada) {
        super(cpf, nome, dataNascimento, livrosEmprestados, multaAcumulada);
    }

    @Override
    public int getLimiteEmprestimo() {
        return 3; // limite do pdf
    }
    
}