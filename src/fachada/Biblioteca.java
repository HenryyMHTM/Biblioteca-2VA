package fachada;

import dados.livro.RepositorioLivroCSV;
import dados.usuario.RepositorioUsuariosCSV;
import java.util.List;
import negocio.*;
import negocio.entidades.*;
import negocio.excecao.*;

public class Biblioteca implements IBiblioteca {
    private final NegocioUsuario nUsuario;
    private final NegocioLivro nLivro;
    private final RepositorioLivroCSV repLivros;

    public Biblioteca() {
        this.repLivros = new RepositorioLivroCSV();
        this.nUsuario = new NegocioUsuario(new RepositorioUsuariosCSV());
        this.nLivro = new NegocioLivro(this.repLivros);
    }

    @Override
    public void cadastrarUsuario(Usuario u) throws UsuarioJaExisteException {
        nUsuario.cadastrar(u);
    }

    @Override
    public Usuario login(String cpf, String dataNasc) throws UsuarioNaoExisteException {
        return nUsuario.efetuarLogin(cpf, dataNasc);
    }

    @Override
    public List<Livro> listarLivros() {
        return nLivro.listarDisponiveis();
    }

    @Override
    public void emprestar(String isbn, Usuario u) throws Exception {
        nUsuario.validarSituacao(u);
        nLivro.emprestarLivro(isbn, u);
    }
// repasse dos metodos de buscar acervo
    public List<Livro> listarTodosLivros() {
        return repLivros.listarTodos();
    }

    public Livro buscarLivroPorIsbn(String isbn) {
        return repLivros.buscarPorIsbn(isbn);
    }

    public List<Livro> buscarLivroPorTitulo(String titulo) {
        return repLivros.buscarPorTitulo(titulo);
    }
    
    public void devolverLivro(String isbn, Usuario u) throws Exception {
        nLivro.devolverLivro(isbn, u);
    }
}