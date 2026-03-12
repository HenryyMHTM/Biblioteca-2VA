package negocio;

import dados.usuario.IRepositorioUsuarios;
import java.util.List;
import negocio.entidades.Usuario;
import negocio.excecao.CPFApenasNumerosException;
import negocio.excecao.CPFTamanhoException;
import negocio.excecao.DataNascimentoInvalidaException;
import negocio.excecao.LimiteLivrosExcedidosException;
import negocio.excecao.MultaPendenteException;
import negocio.excecao.UsuarioJaExisteException;
import negocio.excecao.UsuarioNaoExisteException;
;

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

    public Usuario efetuarLogin(String cpf, String dataNascimento) throws UsuarioNaoExisteException, DataNascimentoInvalidaException,
     CPFTamanhoException, CPFApenasNumerosException {
        
        //cmparação de strings para autenticação simples
        if (cpf.length() != 11 && cpf.matches("[0-9]+")) {
            throw new CPFTamanhoException(cpf);
        }
        if (!cpf.matches("[0-9]+")) {
            throw new CPFApenasNumerosException(cpf);
        }
        
        Usuario u = repo.buscarPorCpf(cpf);
        if (u == null) {
            throw new UsuarioNaoExisteException("");
        }
        if (u.getDataNascimento().equals(dataNascimento)) {
            return u;
        } else {
            throw new DataNascimentoInvalidaException("");
        }
        
    }

    public void validarSituacao(Usuario u) throws MultaPendenteException, LimiteLivrosExcedidosException {
        if (u.getMultaAcumulada() > 0) {
            throw new MultaPendenteException(u.getMultaAcumulada());
        }
        if (u.getQuantidadeLivros() >= u.getLimiteEmprestimo()) {
            throw new LimiteLivrosExcedidosException(u.getQuantidadeLivros(), u.getLimiteEmprestimo());
        }
    }
    public List<Usuario> listarTodos() {
        return repo.listarTodos(); //chama o RepositorioUsuariosCSV
    }
    public void pagarMulta(Usuario u) {
        u.setMultaAcumulada(0.0); // Zera o valor no objeto
        repo.atualizar(u); // Persiste a dívida zerada no usuarios.csv
    }
    public void atualizar(Usuario u) {
        if (u != null) {
            repo.atualizar(u); //
        }
}
}
