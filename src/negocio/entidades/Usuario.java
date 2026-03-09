package negocio.entidades;

import java.util.ArrayList;

// usei classe abstrata pra conseguir incluir aluno e prof aq
public abstract class Usuario {
    // mudei cpf pra string pra nao sumir o zero do começo
    private String cpf;
    private String nome;
    private String dataNascimento;
    private double multaAcumulada;
    private ArrayList<Livro> livrosEmprestados;

    public Usuario(String cpf, String nome, String dataNascimento, double multaAcumulada) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.livrosEmprestados = new ArrayList<>();
        this.multaAcumulada = multaAcumulada;
    }

    // abstrato pra forçar o limite nas filhas dps
    public abstract int getLimiteEmprestimo();

    //getters herdados
    public String getCpf(){return cpf;}
    public String getNome(){return nome;}
    public String getDataNascimento(){return dataNascimento;}
    public double getMultaAcumulada(){return multaAcumulada;}
    public ArrayList<Livro> getLivrosEmprestados(){return livrosEmprestados;}
    //retorna apenas o número de livros emprestados.
    public int getQuantidadeLivros(){return livrosEmprestados.size();}

    // métodos de atualizacação (também herdados)
    public void adicionarLivro(Livro l) {this.livrosEmprestados.add(l);}
    public void removerLivro(Livro l) {this.livrosEmprestados.remove(l);}
    public void setMultaAcumulada(double multa) {this.multaAcumulada = multa;}
    
}