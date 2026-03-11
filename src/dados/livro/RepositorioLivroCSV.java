package dados.livro;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import negocio.entidades.Livro;

public class RepositorioLivroCSV implements IRepositorioLivros {
    private List<Livro> livros;
    private final String CAMINHO_ARQUIVO = "livros.csv";
    private final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RepositorioLivroCSV() {
        this.livros = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Livro l) {
        this.livros.add(l);
        salvarEmArquivo();
    }

    @Override
    public Livro buscarPorIsbn(String isbn) {
        // limpa o que o cara digitou (tira traço e espaço)
        String busca = isbn.replace("-", "").trim();
        
        for (Livro l : livros) {
            // limpa o isbn que veio do csv pra comparar igual
            String isbnArquivo = l.getIsbn().replace("-", "").trim();
            if (isbnArquivo.equalsIgnoreCase(busca)) {
                return l;
            }
        }
        return null; 
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        List<Livro> encontrados = new ArrayList<>();
        for (Livro l : livros) {
            if (l.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                encontrados.add(l);
            }
        }
        return encontrados;
    }

    @Override
    public void atualizar(Livro l) {
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

    private void carregarDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            br.readLine(); // pula o cabecalho

            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",", -1);

                // lendo as colunas na ordem certa do teu csv
                String titulo = dados[0];
                String autor = dados[1];
                int ano = Integer.parseInt(dados[2]);
                String isbn = dados[3];
                boolean status = Boolean.parseBoolean(dados[4]);
                String cpf = (dados[5].equalsIgnoreCase("null") || dados[5].isEmpty()) ? null : dados[5];
                
                LocalDate data = null;
                if (!dados[6].equalsIgnoreCase("null") && !dados[6].isEmpty()) {
                    data = LocalDate.parse(dados[6], FORMATO_DATA);
                }

  this.livros.add(new Livro(titulo, autor, ano, isbn, status, cpf, data));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar os livros: " + e.getMessage());
        }
    }

    private void salvarEmArquivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, false))) {
            // tirei o 'genero' pra bater com teus dados
            bw.write("titulo,autor,ano,isbn,status,cpfLocatario,dataDevolucao");
            bw.newLine();

            for (Livro l : livros) {
                String cpf = (l.getCpfLocatario() != null) ? l.getCpfLocatario() : "null";
                String data = (l.getDataDevolucao() != null) ? l.getDataDevolucao().format(FORMATO_DATA) : "null";

                String linha = l.getTitulo() + "," + l.getAutor() + "," + l.getAno() + "," + 
                               l.getIsbn() + "," + l.isStatus() + "," + cpf + "," + data;

                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }
}