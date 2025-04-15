package org.example.projects.BaseDeDatos.ErrorAPI

import io.ktor.http.*

class ApiException(message: String, val statusCode: HttpStatusCode) : Exception(message)