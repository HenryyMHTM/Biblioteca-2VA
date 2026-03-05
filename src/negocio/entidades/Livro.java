package negocio.entidades;

public class Livro {
    int id, ano;
    String titulo, autor, isbn;
    boolean status;

    


    public Livro(int id, int ano, String titulo, String autor, String isbn, boolean status) {
        this.id = id;
        this.ano = ano;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.status = status;
    }

    
    public int getId() {return id;}
    public int getAno() {return ano;}
    public String getTitulo() {return titulo;}
    public String getAutor() {return autor;}
    public String getIsbn() {return isbn;}
    public boolean isStatus() {return status;}

    protected void setStatus(boolean status) {this.status = status;}
}
