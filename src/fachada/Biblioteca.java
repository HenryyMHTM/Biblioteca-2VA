package fachada;

import java.util.ArrayList;
import java.util.Scanner;

import entidades.Livro;
import entidades.Usuario;


public class Biblioteca {
    private ArrayList<Livro> listaLivros;
    private ArrayList<Usuario> listaUsuarios;
    private Scanner ler;
    
    public Biblioteca() {
        this.listaLivros = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
        this.ler = new Scanner(System.in);
    }
    
}
