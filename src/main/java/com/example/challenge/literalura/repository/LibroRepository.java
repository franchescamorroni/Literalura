package com.example.challenge.literalura.repository;

import com.example.challenge.literalura.entities.Autor;
import com.example.challenge.literalura.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findAll();

    @Query("SELECT DISTINCT l.autor FROM Libro l")
    List<Autor> findAllAutores();

    @Query("SELECT DISTINCT a FROM Autor a WHERE a.nacimiento <= :anio AND (a.deceso >= :anio OR a.deceso IS NULL)")
    List<Autor> findAutoresVivxsEnAno(int anio);

    List<Libro> findByIdioma(String idioma);
}


