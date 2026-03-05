package fachada;
import dados.usuario.*;
import negocio.entidades.Usuario;

// fachada concentrando tudo pro main
public class Biblioteca {
    private IRepositorioUsuarios repUsuarios;

    public Biblioteca() {
        // dps mudar pro CSV quando for salvar msm
        this.repUsuarios = new RepositorioUsuariosArrayList();
    }

    public void cadastrarUsuario(Usuario u) {
        repUsuarios.cadastrarUsuario(u);
    }
}