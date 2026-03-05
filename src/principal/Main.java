package principal;
import fachada.Biblioteca;
import iu.TelaPrincipal;

public class Main {
    public static void main(String[] args) {
        // instanciando a fachada e a tela aq pra rodar tudo
        Biblioteca fachada = new Biblioteca();
        TelaPrincipal tela = new TelaPrincipal();
        
        // puxa o menu
        tela.iniciar(fachada);
    }
}