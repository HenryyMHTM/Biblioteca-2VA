package dados.usuario;
import java.util.List;
import negocio.entidades.Usuario;

// interface padrao pra organizar os metodos
public interface IRepositorioUsuarios {


    Usuario buscarPorCpf(String cpf);
    List<Usuario> listarTodos();
    void adicionar(Usuario u);
    void atualizar(Usuario u);
}

