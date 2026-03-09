package negocio.excecao;

public class LivroNaoExisteException extends Exception{
    public LivroNaoExisteException (){
        super("\nEste livro não existe na biblioteca.");
    }
}
