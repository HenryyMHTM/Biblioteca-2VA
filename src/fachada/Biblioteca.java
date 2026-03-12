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
        RepositorioLivroCSV repLivros = new RepositorioLivroCSV();
        this.nUsuario = new NegocioUsuario(new RepositorioUsuariosCSV());
        this.nLivro = new NegocioLivro(repLivros);
        //aqui atualiza o arquvo de usuario já com as multas atuais.
        
    }

    @Override
    public void cadastrarUsuario(Usuario u) throws UsuarioJaExisteException {
        nUsuario.cadastrar(u);
    }

    @Override
    public Usuario login(String cpf, String dataNasc) throws UsuarioNaoExisteException, DataNascimentoInvalidaException, CPFTamanhoException, CPFApenasNumerosException {
        Usuario u = nUsuario.efetuarLogin(cpf, dataNasc);
        if (u != null) {
        //aqui resolve o bug do usuário não ter livros em posse.
        sincronizarLivrosUsuario(u);
        }
    return u;
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
    @Override
    public void atualizarUsuario(Usuario u) {
        nUsuario.atualizar(u); //
    }
    
    private void sincronizarLivrosUsuario(Usuario u) {
        //limpa a lista atual para evitar duplicações
        u.getLivrosEmprestados().clear(); 
        
        for (Livro l : nLivro.listarTodos()) {
            //caso o CPF do locatário no livro for igual ao CPF do usuário logado
            if (u.getCpf().equals(l.getCpfLocatario())) {
                //adiciona o livro a lista interna do usuário.
                u.adicionarLivro(l); 
            }
        }
    }
// repasse dos metodos de buscar acervo
    public List<Livro> listarTodosLivros() {
        return nLivro.listarTodos();
    }

    @Override
    public Livro buscarLivroPorIsbn(String isbn) {
        return nLivro.buscarPorIsbn(isbn); // Delegando para a camada de negócio
    }

    @Override
    public List<Livro> buscarLivroPorTitulo(String titulo) {
        return nLivro.buscarPorTitulo(titulo); // Delegando para a camada de negócio
    }
    @Override
    public void pagarMulta(Usuario u) {
        nUsuario.pagarMulta(u); //
    }
        
    public void devolverLivro(String isbn, Usuario u) throws Exception {
        nLivro.devolverLivro(isbn, u);
        nUsuario.atualizar(u);
    }

    public double consultarTotalDívida(Usuario u) {
        double acumulada = u.getMultaAcumulada(); //o que já deve
        double projetada = nLivro.calcularMultaProjetada(u); //o que está atrasado agora
        return acumulada + projetada;
    }
}