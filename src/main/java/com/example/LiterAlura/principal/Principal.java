package com.example.LiterAlura.principal;

import com.example.LiterAlura.model.Autor;
import com.example.LiterAlura.model.Libro;
import com.example.LiterAlura.model.RespuestaLibro;
import com.example.LiterAlura.repository.AutorRepository;
import com.example.LiterAlura.repository.LibroRepository;
import com.example.LiterAlura.service.ConsumoAPI;
import com.example.LiterAlura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI api = new ConsumoAPI();
    private final String URL_BASE = "http://gutendex.com/books/";
    private ConvierteDatos converter = new ConvierteDatos();
    public void mostrarMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    ------------------
                    Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma        
                    0 - Salir
                    ------------------
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            
            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    autoresVivos();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                   break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibro() {
        System.out.println("Ingrese el titulo del libro que desea buscar: ");
        String libroBuscado = teclado.nextLine();
        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(libroBuscado);
        if(libroExistente.isEmpty()){
        var json = api.obtenerDatos(URL_BASE + "?search=" + libroBuscado.replace(" ", "%20"));
        RespuestaLibro respuesta = converter.obtenerDatos(json, RespuestaLibro.class);
        if (respuesta != null && !respuesta.getResults().isEmpty()){
            //Guardar en la base de datos
            Libro libro = respuesta.getResults().get(0);
            libro.prepararAutor();
            libro.prepararLenguaje();
            System.out.println(libro);
            libroRepository.save(libro);

            imprimirInfoLibro(libro);


        }else{
            System.out.println("No se encontraron libros con ese título");
        }
        }else {System.out.println("El libro buscado ya ha sido registrado en la base de datos");}
            }

    private void listarLibros(){
        List<Libro> librosList = libroRepository.findAll();
        librosList.forEach(libro -> imprimirInfoLibro(libro));
    }

    private void listarAutores(){
        List<Autor> autorList = autorRepository.findAll();
        autorList.forEach(autor -> imprimirInfoAutor(autor));
    }
    public void autoresVivos(){
        System.out.println("Indica el año que desea buscar");
        int anioBuscado = teclado.nextInt();
        List<Autor> autorList = autorRepository.findAutorVivo(anioBuscado);
        if (autorList.isEmpty()){
            System.out.println("Ningún autor vivo registrado en esa fecha");
        }else {
            autorList.forEach(autor -> imprimirInfoAutor(autor));
        }
    }

    private void librosPorIdioma(){
        boolean valido = false;
        while(!valido){
            System.out.println("""
            Elija un idioma válido (teclee el código):
            es - Español
            en - Inglés
            fr - Francés
            pt - Portugués
            """);
            String idioma = teclado.nextLine().trim().toLowerCase();
            if (idioma.equals("es") || idioma.equals("en") || idioma.equals("fr") || idioma.equals("pt")){
                valido = true;
                List<Libro> listLibro = libroRepository.findByLenguaje(idioma);
                if (listLibro.isEmpty()){
                    System.out.println("No se encontró ningún libro en ese idioma");
                }else{
                    listLibro.forEach(libro -> imprimirInfoLibro(libro));
                }
            }else {
                System.out.println("Por favor, ingrese una opción válida");
            }
        }
    }
        public void imprimirInfoLibro(Libro libro){
            //Imprimir el libro
            String info = String.format("""
            -------- LIBRO -------- 
            Título: %s
            Autor: %s
            Idioma: %s
            Número de descargas: %d  
            -----------------------          
            """, libro.getTitulo(), libro.getAutor().getNombre(), libro.getLenguaje(), libro.getDescargas());
            System.out.println(info);
        }



    public void imprimirInfoAutor(Autor autor){
        String info = String.format("""
            
            
            Autor: %s
            Año de nacimiento: %d
            Año de fallecimiento: %d
            Libros: """, autor.getNombre(), autor.getNacimiento(), autor.getFallecimiento());

        // Imprimir los títulos de los libros
        System.out.println(info);
        List<Libro> libros = autor.getLibros();
        if (libros != null && !libros.isEmpty()) {
            libros.forEach(libro -> System.out.println(" - " + libro.getTitulo()));
        } else {
            System.out.println(" (Sin libros registrados)");
        }
    }
}
