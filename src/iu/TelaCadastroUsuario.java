package iu;
import dados.usuario.RepositorioUsuariosCSV;
import fachada.Biblioteca;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import negocio.entidades.Aluno;
import negocio.entidades.Professor;
import negocio.entidades.Usuario;
import negocio.excecao.CPFApenasNumerosException;
import negocio.excecao.CPFTamanhoException;
import negocio.excecao.DataNascimentoInvalidaException;
import negocio.excecao.FormatoDataNascimentoException;
import negocio.excecao.NomeApenasCaracteresException;
import negocio.excecao.NomeTamanhoException;
import negocio.excecao.PerfilUsuarioInexistenteException;
import negocio.excecao.UsuarioJaExisteException;

public class TelaCadastroUsuario {
    private RepositorioUsuariosCSV repositorio;
    
    public void exibir(Biblioteca fachada) {
        Scanner sc = new Scanner(System.in);
        String nome;
        String cpf;
        String dataNascimento;
        int tipo = 0;

        System.out.println("\n--- CRIAR CONTA ---");

        // Validação do Nome com Exceções
        while (true) {
            try {
                System.out.print("Nome completo: ");
                nome = sc.nextLine();
                
                if (nome.trim().length() < 3) {
                    throw new NomeTamanhoException(nome);
                }
                if (!nome.matches("[a-zA-ZáéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕçÇ ]+")) {
                    throw new NomeApenasCaracteresException();
                }
                break; // Sai do loop se estiver tudo ok
            } catch (NomeTamanhoException | NomeApenasCaracteresException e) {
                System.out.println(e.getMessage());
            }
        }

        //checa CPF usando exceções
        while (true) {
            try {
                System.out.print("CPF (apenas 11 números): ");
                cpf = sc.nextLine();

                if (cpf.length() != 11 && cpf.matches("[0-9]+")) {
                    throw new CPFTamanhoException(cpf);
                }
                if (!cpf.matches("[0-9]+")) {
                    throw new CPFApenasNumerosException(cpf);
                }
                break;
            } catch (CPFTamanhoException | CPFApenasNumerosException e) {
                System.out.println(e.getMessage());
            }
        }
        //validação de data de nascimento
        while (true) {
            System.out.print("Data de Nascimento (dd/mm/aaaa): ");
            
            dataNascimento = sc.nextLine();
            

            try {
                if (dataNascimento.length() != 10 && !dataNascimento.contains("/")){
                    throw new FormatoDataNascimentoException(dataNascimento);
                }
                LocalDate dataNascC = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate hoje = LocalDate.now();
                if (Period.between(dataNascC, hoje).getYears() < 15) {
                    throw new DataNascimentoInvalidaException(dataNascimento); 
                }
                
                break;

            } catch (DataNascimentoInvalidaException | FormatoDataNascimentoException e) {
                System.out.println(e.getMessage()); 
            }
        }
        //tipo de usuario
        while (tipo != 1 && tipo != 2) {
            System.out.print("Perfil (1 - Estudante, 2 - Professor): ");
            String entrada = sc.nextLine(); 
            try {
                tipo = Integer.parseInt(entrada);
                if (tipo != 1 && tipo != 2) {
                    throw new PerfilUsuarioInexistenteException(entrada);
                }
            } catch ( PerfilUsuarioInexistenteException e) {
                System.out.println(e.getMessage());
            }
        }
        //salvamento via fachada
        try {
            Usuario novoUsuario;
            if (tipo == 1) {
                novoUsuario = new Aluno(cpf, nome, dataNascimento, 0.0);
            } else {
                novoUsuario = new Professor(cpf, nome, dataNascimento, 0.0);
            }

            fachada.cadastrarUsuario(novoUsuario);
            System.out.println("\nConta criada com sucesso!");
        } catch (UsuarioJaExisteException e) {
            System.out.println(e.getMessage());
        }
    }
}