package negocio.entidades;

import java.time.LocalDate;

public class Livro {
    private final int ano;
    private final String titulo;
    private final String autor;
    private final String isbn;
    private boolean status;
    private LocalDate dataDevolucao;
    private String cpfLocatario;



    public Livro( String titulo, String autor,int ano, String isbn,
          boolean status, String cpfLocatario, LocalDate dataDevolucao) {
        this.ano = ano;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.status = status;
        this.cpfLocatario = cpfLocatario;
        this.dataDevolucao = dataDevolucao;
    }

    public int getAno() {return ano;}
    public String getTitulo() {return titulo;}
    public String getAutor() {return autor;}
    public String getIsbn() {return isbn;}
    public boolean isStatus() {return status;}

    public void setStatus(boolean status) {this.status = status;}

    public String getCpfLocatario() {
        return cpfLocatario;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setCpfLocatario(String cpf) {
        this.cpfLocatario = cpf;
    }

    public void setDataDevolucao(LocalDate data) {
        this.dataDevolucao = data;
    }
}
