package negocio;

import dados.livro.IRepositorioLivros;
import java.time.LocalDate;
import java.util.List;
import negocio.entidades.*;
import negocio.excecao.*;

public class NegocioLivro {
    private IRepositorioLivros repo;

    public NegocioLivro(IRepositorioLivros repo) {
        this.repo = repo;
    }

    public List<Livro> listarDisponiveis() {
        return repo.listarTodos();
    }
    //método usado para sincronizar com a conta
    public List<Livro> listarTodos() {
        return repo.listarTodos(); //carrega a busca para o repositório
    }

    public void validarSituacao(Usuario u) throws MultaPendenteException {
        //bloqueia por multa consolidada (já no CSV)
        if (u.getMultaAcumulada() > 0) {
            throw new MultaPendenteException(u.getMultaAcumulada());
        }
        
        //bloqueia se houver livro atrasado em posse do usuário.
        LocalDate hoje = LocalDate.now();
        for (Livro l : u.getLivrosEmprestados()) {
            if (l.getDataDevolucao() != null && hoje.isAfter(l.getDataDevolucao())) {
                throw new RuntimeException("Você possui livros com prazo de entrega vencido. Devolva-os para realizar novos empréstimos.");
            }
        }
    }
    public void emprestarLivro(String isbn, Usuario u) throws LivroNaoExisteException, LivroJaEmprestadoException {
        Livro l = repo.buscarPorIsbn(isbn);
        
        // se nao achar nada no csv
        if (l == null) {
            throw new LivroNaoExisteException();
        }
        
        // se o livro ja tiver com alguem (status false)
        if (!l.isStatus()) {
            throw new LivroJaEmprestadoException();
        }
        
        // bota o livro na conta do cara e marca como ocupado
        l.setStatus(false); 
        l.setCpfLocatario(u.getCpf());
        l.setDataDevolucao(LocalDate.now().plusDays(7));
        u.adicionarLivro(l);
        
        // joga as mudancas pro arquivo
        repo.atualizar(l);
    }

    public void devolverLivro(String isbn, Usuario u) throws LivroNaoExisteException, LivroNaoEmprestadoException, LivroJaEmprestadoException {
        Livro l = repo.buscarPorIsbn(isbn);
        LocalDate hoje = LocalDate.now();
        // checa se o livro existe mesmo
        if (l == null) throw new LivroNaoExisteException(); 
        // ve se o livro ja nao tava livre
        if (l.isStatus()) {
            throw new LivroNaoEmprestadoException();
        }
        // trava se o cara tentar devolver o livro de outro
        if (l.getCpfLocatario() != null && !l.getCpfLocatario().equals(u.getCpf())) {
            throw new LivroJaEmprestadoException();
        }

        if (l.getDataDevolucao() != null && hoje.isAfter(l.getDataDevolucao())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(l.getDataDevolucao(), hoje);
            double valorDaMulta = diasAtraso * 2.0; // R$ 2,00 por dia
            
            // 2. Acumula o valor no objeto usuário (que será salvo depois)
            u.setMultaAcumulada(u.getMultaAcumulada() + valorDaMulta);
            System.out.println("Devolução com atraso! Multa de R$ " + valorDaMulta + " aplicada ao perfil.");
        }

        // limpa os dados da locacao
        l.setStatus(true); 
        l.setCpfLocatario(null); 
        l.setDataDevolucao(null); 

        // atualiza o csv
        repo.atualizar(l);
    }

    public Livro buscarPorIsbn(String isbn) {
        return repo.buscarPorIsbn(isbn);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return repo.buscarPorTitulo(titulo);
    }

    public double calcularMultaProjetada(Usuario u) {
        double totalAtraso = 0;
        LocalDate hoje = LocalDate.now();

        for (Livro l : u.getLivrosEmprestados()) {
            if (l.getDataDevolucao() != null && hoje.isAfter(l.getDataDevolucao())) {
                long dias = java.time.temporal.ChronoUnit.DAYS.between(l.getDataDevolucao(), hoje);
                totalAtraso += dias * 2.0; // R$ 2,00 por dia
            }
        }
        return totalAtraso;
    }
}