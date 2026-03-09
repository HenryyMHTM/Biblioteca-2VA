package dados.livro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import negocio.entidades.Livro;

public class RepositorioLivroCSV implements IRepositorioLivros {
    private List<Livro> livros;
    private final String CAMINHO_ARQUIVO = "livros.csv";
    // O formatador de data diz ao Java como ler o padrão "dd/MM/yyyy"
    private final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RepositorioLivroCSV() {
        this.livros = new ArrayList<>();
        carregarDoArquivo();
    }

    // métodos CRUD da interface

    @Override
    public void adicionar(Livro l) {
        this.livros.add(l);
        salvarEmArquivo();
    }

    @Override
    public Livro buscarPorIsbn(String isbn) {
        for (Livro l : livros) {
            if (l.getIsbn().equals(isbn)) {
                return l;
            }
        }
        return null; // Retorna nulo se não achar o livro
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        List<Livro> livrosEncontrados = new ArrayList<>();
        for (Livro l : livros) {
            if (l.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                livrosEncontrados.add(l);
            }
        }
        return livrosEncontrados;
    }

    @Override
    public void atualizar(Livro l) {
        // Quando o status de um livro muda (emprestou ou devolveu), 
        // a gente chama esse método para reescrever o arquivo.
        salvarEmArquivo();
    }

    @Override
    public void remover(String isbn) {
        Livro l = buscarPorIsbn(isbn);
        if (l != null) {
            this.livros.remove(l);
            salvarEmArquivo();
        }
    }

    @Override
    public List<Livro> listarTodos() {
        return this.livros;
    }

    //métodos próprios da classe

    private void carregarDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine(); // Pula o cabeçalho

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",", -1);

                String titulo = dados[0];
                String autor = dados[1];
                int ano = Integer.parseInt(dados[2]);
                String isbn = dados[3];
                boolean status = Boolean.parseBoolean(dados[4]);
                
                String cpfLocatario = (dados[5].equalsIgnoreCase("null") || dados[5].isEmpty()) ? null : dados[5];
                LocalDate dataDevolucao = null;

                if (!dados[6].equalsIgnoreCase("null") && !dados[6].isEmpty()) {
                    dataDevolucao = LocalDate.parse(dados[6], FORMATO_DATA);
                }

                Livro novoLivro = new Livro(titulo, autor, ano, isbn, status, cpfLocatario, dataDevolucao);
                this.livros.add(novoLivro);
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de livros: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro na formatação dos dados do livro: " + e.getMessage());
        }
    }

    private void salvarEmArquivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, false))) {
            
            // Cabeçalho
            bw.write("titulo,autor,ano,genero,isbn,status,cpfLocatario,dataDevolucao");
            bw.newLine();

            for (Livro l : livros) {
                //Prepara o CPF para virar texto (se for nulo, vira a palavra "null")
                String cpfParaSalvar = (l.getCpfLocatario() != null) ? l.getCpfLocatario() : "null";
                
                // Prepara a Data para virar texto
                String dataParaSalvar = "null";
                if (l.getDataDevolucao() != null) {
                    dataParaSalvar = l.getDataDevolucao().format(FORMATO_DATA);
                }

                // Monta a linha
                String linha = l.getTitulo() + "," +
                               l.getAutor() + "," +
                               l.getAno() + "," +
                               l.getIsbn() + "," +
                               l.isStatus() + "," + // O boolean no Java já imprime true ou false
                               cpfParaSalvar + "," +
                               dataParaSalvar;

                bw.write(linha);
                bw.newLine();
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo de livros: " + e.getMessage());
        }
    }
}
