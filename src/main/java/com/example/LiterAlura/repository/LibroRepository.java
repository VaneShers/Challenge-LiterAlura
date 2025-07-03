package com.example.LiterAlura.repository;

import com.example.LiterAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long>{
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    List<Libro> findAll();
    List<Libro> findByLenguaje(String lenguaje);
}
