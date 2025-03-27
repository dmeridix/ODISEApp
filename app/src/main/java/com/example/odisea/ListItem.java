package com.example.odisea;

public interface ListItem {
    String getId(); // Identificador único del elemento
    String getType(); // Tipo del elemento (por ejemplo, "hotel", "restaurant", "spa", "pista")
    String getTitle(); // Título del elemento
    String getImageUrl(); // URL de la imagen
    String getDescription(); // Descripción breve
    String getLocation(); // Ubicación
    float getRating(); // Calificación
}
