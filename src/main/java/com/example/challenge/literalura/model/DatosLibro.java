package com.example.challenge.literalura.model;

import com.example.challenge.literalura.entities.Autor;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("name") Autor autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Double descargas){
}

