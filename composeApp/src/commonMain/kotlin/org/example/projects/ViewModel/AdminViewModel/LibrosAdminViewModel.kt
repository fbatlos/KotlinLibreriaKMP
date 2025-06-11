package org.example.projects.ViewModel.AdminViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.APIService
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

class LibrosAdminViewModel  (
    private val uiViewModel: UiStateViewModel,
    private val sharedViewModel: SharedViewModel,
    private val librosViewModel: LibrosViewModel
) : ViewModel() {


    fun addLibro(libro: Libro) {
        viewModelScope.launch {
            uiViewModel.setLoading(true)
            try {
                val add = API.apiService.addLibro(libro,sharedViewModel.token.value!!)
                println("estoy en add libro $add")
                librosViewModel.fetchLibros()
            } catch (e: Exception) {
                uiViewModel.setTextError("Error al añadir: ${e.message}")
                uiViewModel.setShowDialog(true)
            }
        }
        uiViewModel.setLoading(false)
    }

    fun updateLibro(libroid:String,libro: Libro) {
        viewModelScope.launch {
            uiViewModel.setLoading(true)
            try {
                API.apiService.putLibro(libroid,libro,sharedViewModel.token.value!!)
                librosViewModel.fetchLibros()
            } catch (e: Exception) {
                uiViewModel.setTextError("Error al añadir: ${e.message}")
                uiViewModel.setShowDialog(true)
            }
        }
        uiViewModel.setLoading(false)
    }

    fun deleteLibro(id: String) {
        viewModelScope.launch {
            uiViewModel.setLoading(true)
            try {
                //TODO AÑADIR ENDPOINT
                API.apiService.deleteLibro(id,sharedViewModel.token.value!!)
                librosViewModel.fetchLibros()
            } catch (e: Exception) {
                uiViewModel.setTextError("Error al añadir: ${e.message}")
                uiViewModel.setShowDialog(true)
            }
        }
        uiViewModel.setLoading(false)
    }

}