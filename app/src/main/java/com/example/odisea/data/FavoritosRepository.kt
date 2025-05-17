package com.example.odisea.data

import com.example.odisea.data.Lugar

object FavoritosRepository {
    private val favoritos = mutableSetOf<Int>()

    fun esFavorito(id: Int): Boolean = favoritos.contains(id)

    fun agregar(id: Int) {
        favoritos.add(id)
    }

    fun quitar(id: Int) {
        favoritos.remove(id)
    }

    fun obtenerTodos(): Set<Int> = favoritos

    fun actualizar(lista: List<Lugar>) {
        favoritos.clear()
        favoritos.addAll(lista.map { it.id })
    }
}
