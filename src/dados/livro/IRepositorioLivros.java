package dados.livro;

import java.util.List;

import negocio.entidades.Livro;

public interface IRepositorioLivros {

    void adicionar(Livro l);

    Livro buscarPorIsbn(String isbn);

    void atualizar(Livro l);

    void remover(String isbn);

    List<Livro> listarTodos();

}
