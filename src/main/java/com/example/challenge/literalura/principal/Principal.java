package com.example.challenge.literalura.principal;

import com.example.challenge.literalura.entities.Autor;
import com.example.challenge.literalura.entities.Libro;
import com.example.challenge.literalura.model.DatosAutor;
import com.example.challenge.literalura.model.DatosLibro;
import com.example.challenge.literalura.repository.AutorRepository;
import com.example.challenge.literalura.repository.LibroRepository;
import com.example.challenge.literalura.service.ConsumoAPI;
import com.example.challenge.literalura.service.ConvierteDatos;
import com.example.challenge.literalura.service.ConvierteDatosAutor;

import java.util.*;

public class Principal {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConvierteDatosAutor conversorAutor = new ConvierteDatosAutor();
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository repositorioLibro;
    private AutorRepository repositorioAutor;

    public Principal(LibroRepository repositorioLibro, AutorRepository repositorioAutor) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioAutor = repositorioAutor;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Elija la tarea a través de su número:
                    1- buscar libro por título
                    2- listar libros registrados
                    3- listar autores registrados
                    4- listar autores vivos en un determinado año
                    5- listar libros por idioma
                    
                    0 - Salir.
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAno();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    opcion = 0;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    private void buscarLibroPorTitulo() {
        String nombreLibro = pregunta();
        Optional<Libro> libroOptional = repositorioLibro.findAll().stream()
                .filter(libro -> libro.getTitulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();

        if (libroOptional.isPresent()) {
            Libro libroEncontrado = libroOptional.get();
            System.out.println(libroEncontrado);
            System.out.println("El libro ya está registrado.");
        } else {
            try {
                DatosLibro datosLibro = getDatosLibro(nombreLibro);
                System.out.println(datosLibro);

                if (datosLibro != null) {
                    DatosAutor datosAutor = getDatosAutor(nombreLibro);
                    Autor autor = obtenerORegistrarAutor(datosAutor);

                    Libro libro = new Libro(
                            datosLibro.titulo(),
                            autor,
                            datosLibro.idiomas(),
                            datosLibro.descargas()
                    );

                    repositorioLibro.save(libro);

                    System.out.println("Libro guardado exitosamente:");
                    System.out.println(libro);
                } else {
                    System.out.println("No se encontró el libro.");
                }
            } catch (Exception e) {
                System.out.println("Excepción: " + e.getMessage());
            }
        }
    }

    private DatosLibro getDatosLibro(String nombreLibro) {
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, DatosLibro.class);
    }

    private DatosAutor getDatosAutor(String nombreLibro) {
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        return conversorAutor.obtenerDatos(json, DatosAutor.class);
    }

    private Autor obtenerORegistrarAutor(DatosAutor datosAutor) {
        List<Autor> autores = repositorioAutor.findAll();
        Optional<Autor> autorOptional = autores.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(datosAutor.nombre()))
                .findFirst();

        Autor autor;
        if (autorOptional.isPresent()) {
            autor = autorOptional.get();
        } else {
            autor = new Autor(
                    datosAutor.nombre(),
                    datosAutor.nacimiento(),
                    datosAutor.fallecimiento()
            );
            repositorioAutor.save(autor);
        }
        return autor;
    }

    private String pregunta() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        return teclado.nextLine();
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> libros = repositorioLibro.findAll();
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Idiomas: " + libro.getIdioma());
            System.out.println("Número de descargas: " + libro.getDescargas());
            System.out.println("-------------------");
        });
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> autores = repositorioAutor.findAll();
        autores.forEach(autor -> {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Nacimiento: " + autor.getNacimiento());
            System.out.println("Fallecimiento: " + autor.getFallecimiento());
            System.out.println("Libros:");
            autor.getLibros().forEach(libro -> {
                System.out.println(" - " + libro.getTitulo());
            });
            System.out.println("-------------------");
        });
    }

    private void listarAutoresVivosEnAno() {
        System.out.println("Ingrese un año:");
        int anio = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autores = repositorioLibro.findAutoresVivxsEnAno(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
        } else {
            autores.forEach(autor -> {
                System.out.println("Autor: " + autor.getNombre());
                System.out.println("Nacimiento: " + autor.getNacimiento());
                System.out.println("Fallecimiento: " + autor.getFallecimiento());
                System.out.println("Libros:");
                autor.getLibros().forEach(libro -> {
                    System.out.println(" - " + libro.getTitulo());
                });
                System.out.println("-------------------");
            });
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma del que desea buscar los libros:");
        String idioma = teclado.nextLine();

        List<Libro> libros = repositorioLibro.findByIdioma(idioma);
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Número de descargas: " + libro.getDescargas());
            System.out.println("-------------------");
        });
    }
}

