package org.example.projects.BaseDeDatos.ErrorAPI

import io.ktor.http.*

class ApiException(message: String) : Exception(message)