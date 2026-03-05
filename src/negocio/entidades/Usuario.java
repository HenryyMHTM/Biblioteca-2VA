package negocio.entidades;

public abstract class Usuario {
    double CPF;
    String nome;

    Usuario (double CPF, String nome) {
        this.CPF = CPF;
        this.nome = nome;
    }

    public abstract int limiteEmprestimo (int limite);
    public double getCPF() {return CPF;}
    public String getNome() {return nome;} 
}
