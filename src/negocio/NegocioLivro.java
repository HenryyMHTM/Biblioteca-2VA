package negocio;

import dados.livro.IRepositorioLivros;
import java.time.LocalDate;
import java.util.List;
import negocio.entidades.*;
import negocio.excecao.LivroNaoExisteException;

public class NegocioLivro {
    private IRepositorioLivros repo;

    public NegocioLivro(IRepositorioLivros repo) {
        this.repo = repo;
    }

    public List<Livro> listarDisponiveis() {
        return repo.listarTodos();
    }

    public void emprestarLivro(String isbn, Usuario u) throws LivroNaoExisteException {
        Livro l = repo.buscarPorIsbn(isbn);
        if (l == null || !l.isStatus()) {
            throw new LivroNaoExisteException();
        }
        
        l.setStatus(false); // Indisponível
        l.setCpfLocatario(u.getCpf());
        l.setDataDevolucao(LocalDate.now().plusDays(7));
        u.adicionarLivro(l);
        repo.atualizar(l);
    }

    

}