package iu;
import fachada.Biblioteca;
import negocio.entidades.*;
import java.util.Scanner;

public class TelaCadastroUsuario {
    
    public void exibir(Biblioteca fachada) {
        Scanner sc = new Scanner(System.in);
        String nome = "";
        String cpf = "";
        String dataNascimento = "";
        int tipo = 0;

        System.out.println("\n--- CRIAR CONTA ---");

        // barrar nome vazio
        while (nome.trim().isEmpty()) {
            System.out.print("Nome completo: ");
            nome = sc.nextLine();
            if (nome.trim().isEmpty()) {
                System.out.println("Erro: O nome não pode ser vazio.");
            }
        }

        // prender num loop ate vir 11 digitos
        while (true) {
            System.out.print("CPF (apenas números): ");
            cpf = sc.nextLine();
            if (cpf.matches("[0-9]+") && cpf.length() == 11) {
                break;
            }
            System.out.println("Erro: CPF inválido. Digite exatamente 11 números.");
        }

        // validar basico do dd/mm/aaaa
        while (true) {
            System.out.print("Data de Nascimento (dd/mm/aaaa): ");
            dataNascimento = sc.nextLine();
            if (dataNascimento.length() == 10 && dataNascimento.contains("/")) {
                break;
            }
            System.out.println("Erro: Formato incorreto. Use dd/mm/aaaa.");
        }

        // try catch pra nao estourar se botarem letra no menu
        while (tipo != 1 && tipo != 2) {
            System.out.print("Perfil (1 - Estudante, 2 - Professor): ");
            String entrada = sc.nextLine(); 
            try {
                tipo = Integer.parseInt(entrada);
                if (tipo != 1 && tipo != 2) {
                    System.out.println("Erro: Escolha a opção 1 ou 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas o número.");
            }
        }

        // criar o obj certo de acordo com a opçao
        Usuario novoUsuario;
        if (tipo == 1) {
            novoUsuario = new Aluno(cpf, nome, dataNascimento);
        } else {
            novoUsuario = new Professor(cpf, nome, dataNascimento);
        }

        fachada.cadastrarUsuario(novoUsuario);
        System.out.println("Conta criada com sucesso!");
    }
}