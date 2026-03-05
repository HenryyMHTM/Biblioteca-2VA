package dados.usuario;
import negocio.entidades.Usuario;
import java.util.ArrayList;

public class RepositorioUsuariosArrayList implements IRepositorioUsuarios {
    // arraylist pra guardar na memoria por enqnto
    private ArrayList<Usuario> usuarios;

    public RepositorioUsuariosArrayList() {
        this.usuarios = new ArrayList<>();
    }

    @Override
    public void cadastrarUsuario(Usuario u) {
        usuarios.add(u); // add na lista
    }
}