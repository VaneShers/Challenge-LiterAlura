package com.example.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @JsonAlias("title")
    private String titulo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonAlias("authors")
    @Transient
    private List<Autor> autores;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;
    @Transient
    @JsonAlias("languages")
    private List<String> listaLenguaje;
    private String lenguaje;
    @JsonAlias("download_count")
    private int descargas;

    public void prepararAutor(){
        if (autores != null){
            this.autor = autores.get(0);
        }
    }
    public void prepararLenguaje(){
        if (listaLenguaje != null && !listaLenguaje.isEmpty()) {
            this.lenguaje = listaLenguaje.get(0);
        } else {
            this.lenguaje = "desconocido";
        }
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public List<String> getListaLenguaje() {
        return listaLenguaje;
    }

    public void setListaLenguaje(List<String> listaLenguaje) {
        this.listaLenguaje = listaLenguaje;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }
    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", lenguaje='" + lenguaje + '\'' +
                ", descargas=" + descargas +
                '}';
    }
}
