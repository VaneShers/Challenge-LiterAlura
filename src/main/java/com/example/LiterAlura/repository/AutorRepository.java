package com.example.LiterAlura.repository;

import com.example.LiterAlura.model.Autor;
import com.example.LiterAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    List<Autor> findAll();
    @Query("SELECT a FROM Autor a WHERE a.nacimiento < :anio AND a.fallecimiento > :anio")
    List<Autor> findAutorVivo(int anio);
}
