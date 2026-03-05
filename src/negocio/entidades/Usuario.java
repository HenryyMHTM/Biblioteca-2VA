package negocio.entidades;

// usei classe abstrata pra conseguir incluir aluno e prof aq
public abstract class Usuario {
    // mudei cpf pra string pra nao sumir o zero do começo
    private String cpf;
    private String nome;
    private String dataNascimento;

    public Usuario(String cpf, String nome, String dataNascimento) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    // abstrato pra forçar o limite nas filhas dps
    public abstract int getLimiteEmprestimo();

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getDataNascimento() { return dataNascimento; }
}