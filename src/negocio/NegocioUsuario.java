package negocio;

import dados.usuario.IRepositorioUsuarios;
import negocio.entidades.Usuario;
import negocio.excecao.LimiteLivrosExcedidosException;
import negocio.excecao.MultaPendenteException;
import negocio.excecao.UsuarioJaExisteException;
import negocio.excecao.UsuarioNaoExisteException;;

public class NegocioUsuario {
    private IRepositorioUsuarios repo;
    
    public NegocioUsuario(IRepositorioUsuarios repo) {
        this.repo = repo;
    }
    //cadastra novo usuário
    public void cadastrar(Usuario u) throws UsuarioJaExisteException {
        if (repo.buscarPorCpf(u.getCpf()) != null) {
            //checa se existe, se existir, lança o erro
            throw new UsuarioJaExisteException("");
        }
        //caso não exista, ele adiciona ao repositorio
        repo.adicionar(u);
    }

    public Usuario efetuarLogin(String cpf, String dataNascimento) throws UsuarioNaoExisteException {
        Usuario u = repo.buscarPorCpf(cpf);
        //cmparação de strings para autenticação simples
        if (u != null && u.getDataNascimento().equals(dataNascimento)) {
            return u;
        }
        throw new UsuarioNaoExisteException("");
    }

    public void validarSituacao(Usuario u) throws MultaPendenteException, LimiteLivrosExcedidosException {
        if (u.getMultaAcumulada() > 0) {
            throw new MultaPendenteException(u.getMultaAcumulada());
        }
        if (u.getQuantidadeLivros() >= u.getLimiteEmprestimo()) {
            throw new LimiteLivrosExcedidosException(u.getQuantidadeLivros(), u.getLimiteEmprestimo());
        }
    }
}
