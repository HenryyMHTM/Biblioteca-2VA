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

    public Biblioteca() {
        this.nUsuario = new NegocioUsuario(new RepositorioUsuariosCSV());
        this.nLivro = new NegocioLivro(new RepositorioLivroCSV());
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

    
}