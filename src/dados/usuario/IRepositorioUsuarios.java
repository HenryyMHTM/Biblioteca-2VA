package dados.usuario;
import negocio.entidades.Usuario;

// interface padrao pra organizar os metodos
public interface IRepositorioUsuarios {
    void cadastrarUsuario(Usuario u);
}