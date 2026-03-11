package negocio;

import dados.livro.IRepositorioLivros;
import java.time.LocalDate;
import java.util.List;
import negocio.entidades.*;
import negocio.excecao.*;

public class NegocioLivro {
    private IRepositorioLivros repo;

    public NegocioLivro(IRepositorioLivros repo) {
        this.repo = repo;
    }

    public List<Livro> listarDisponiveis() {
        return repo.listarTodos();
    }

    public void emprestarLivro(String isbn, Usuario u) throws LivroNaoExisteException, LivroJaEmprestadoException {
        Livro l = repo.buscarPorIsbn(isbn);
        
        // se nao achar nada no csv
        if (l == null) {
            throw new LivroNaoExisteException();
        }
        
        // se o livro ja tiver com alguem (status false)
        if (!l.isStatus()) {
            throw new LivroJaEmprestadoException();
        }
        
        // bota o livro na conta do cara e marca como ocupado
        l.setStatus(false); 
        l.setCpfLocatario(u.getCpf());
        l.setDataDevolucao(LocalDate.now().plusDays(7));
        u.adicionarLivro(l);
        
        // joga as mudancas pro arquivo
        repo.atualizar(l);
    }

    public void devolverLivro(String isbn, Usuario u) throws LivroNaoExisteException, LivroNaoEmprestadoException, UsuarioNaoLocatarioException {
        Livro l = repo.buscarPorIsbn(isbn);

        // checa se o livro existe mesmo
        if (l == null) {
            throw new LivroNaoExisteException(); 
        }
        // ve se o livro ja nao tava livre
        if (l.isStatus()) {
       throw new LivroNaoEmprestadoException();
        }
        // trava se o cara tentar devolver o livro de outro
        if (l.getCpfLocatario() != null && !l.getCpfLocatario().equals(u.getCpf())) {
            throw new UsuarioNaoLocatarioException();
        }

        // limpa os dados da locacao
        l.setStatus(true); 
        l.setCpfLocatario(null); 
        l.setDataDevolucao(null); 

        // atualiza o csv
        repo.atualizar(l);
    }
}