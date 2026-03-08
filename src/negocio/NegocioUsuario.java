package negocio;

import dados.usuario.IRepositorioUsuarios;
import negocio.entidades.Usuario;
import negocio.excecoes.LimiteLivrosExcedidosException;
import negocio.excecoes.MultaPendenteException;;

public class NegocioUsuario {
    private IRepositorioUsuarios repo;
    public NegocioUsuario(IRepositorioUsuarios repo) {
        this.repo = repo;
    }

    public void validarEmprestimo(Usuario u) throws LimiteLivrosExcedidosException, MultaPendenteException {
        //regra para multa
        if (u.getMultaAcumulada() > 0) {
            throw new MultaPendenteException(u.getMultaAcumulada());
        }
        
        //limite de empréstimos
        if (u.getLivrosEmprestados().size() >= u.getLimiteEmprestimo()) {
            throw new LimiteLivrosExcedidosException();
        }
    }
}
