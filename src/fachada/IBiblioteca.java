package fachada;

import java.util.List;
import negocio.entidades.Livro;
import negocio.entidades.Usuario;
import negocio.excecao.*;

public interface IBiblioteca {
    // Cadastro e Autenticação
    void cadastrarUsuario(Usuario u) throws UsuarioJaExisteException;
    Usuario login(String cpf, String dataNasc) throws UsuarioNaoExisteException;

    // Operações de Acervo
    List<Livro> listarLivros(); //
    void emprestar(String isbn, Usuario u) throws Exception;
    
}