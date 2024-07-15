package com.example.challenge.literalura.repository;

import com.example.challenge.literalura.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {
}

