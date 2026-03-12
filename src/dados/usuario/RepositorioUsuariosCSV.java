package dados.usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import negocio.entidades.Aluno;
import negocio.entidades.Professor;
import negocio.entidades.Usuario;

public class RepositorioUsuariosCSV implements IRepositorioUsuarios {
    private List<Usuario> usuarios;
    private final String localArquivo = "usuarios.csv";

    public RepositorioUsuariosCSV() {
        this.usuarios = new ArrayList<>();
        //inicializa o repositória e cria ele
        carregarDoArquivo(); 
    }

    @Override
    public void adicionar(Usuario u) {
        this.usuarios.add(u);
        salvarEmArquivo(); //salva
    }

    @Override
    public Usuario buscarPorCpf(String cpf) {
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Usuario u) {
        for (int i = 0; i < this.usuarios.size(); i++) {
            if (this.usuarios.get(i).getCpf().equals(u.getCpf())) {
                this.usuarios.set(i, u);
                break;
            }
        }
        salvarEmArquivo(); 
    }

    @Override
    public List<Usuario> listarTodos() {
        return this.usuarios;
    }

    //Métodos sem ser da interface, métodos próprios
    private void carregarDoArquivo() {
        File arquivo = new File(localArquivo);
        
        // Se for a primeira execução e o arquivo não existir, sai do método
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine(); // Lê e descarta a primeira linha (cabeçalho)
            
            // Loop para ler todos os usuários
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                
                // Extrai os dados das colunas
                String cpf = dados[0];
                String nome = dados[1];
                String dataNascimento = dados[2];
                double multa = Double.parseDouble(dados[3]);
                String tipo = dados[4];

                Usuario novoUsuario;

                //instancia o objeto correto dependendo do tipo de usuario
                if (tipo.equalsIgnoreCase("Professor")) {
                    novoUsuario = new Professor(cpf, nome, dataNascimento, multa);
                } else {
                    novoUsuario = new Aluno(cpf, nome, dataNascimento, multa);
                }

                this.usuarios.add(novoUsuario);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de usuários: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro de formatação numérica no CSV: " + e.getMessage());
        }
    }
    
    private void salvarEmArquivo() {
        // O false indica que o arquivo será totalmente reescrito
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(localArquivo, false))) {
            
            // 1. Escreve o cabeçalho
            bw.write("cpf,nome,dataNascimento,multaAcumulada,tipo");
            bw.newLine();

            // 2. Percorre a lista e escreve linha por linha
            for (Usuario u : usuarios) {
                //checa se o usuário é aluno ou professor
                String tipo = (u instanceof Professor) ? "Professor" : "Aluno";
                
                // Monta a linha separada por vírgulas
                String linha = u.getCpf() + "," + 
                               u.getNome() + "," + 
                               u.getDataNascimento() + "," + 
                               u.getMultaAcumulada() + "," + 
                               tipo;
                               
                bw.write(linha);
                bw.newLine();
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de usuários: " + e.getMessage());
        }
    }


}