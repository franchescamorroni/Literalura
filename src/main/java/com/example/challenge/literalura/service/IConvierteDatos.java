package com.example.challenge.literalura.service;

import java.util.List;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}

