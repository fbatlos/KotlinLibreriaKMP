package org.example.projects.ViewModel

import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.Libro
import javax.management.Query

expect class SharedViewModel() {

    val token: StateFlow<String?>

    fun setToken(token: String)

}