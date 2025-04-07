import kotlinx.coroutines.flow.StateFlow

actual abstract class SharedViewModel actual constructor() {
    actual val textError: StateFlow<String?>
        get() = TODO("Not yet implemented")
    actual val username: StateFlow<String?>
        get() = TODO("Not yet implemented")
    actual val email: StateFlow<String?>
        get() = TODO("Not yet implemented")
    actual val contrasenia: StateFlow<String?>
        get() = TODO("Not yet implemented")
    actual val municipio: StateFlow<String?>
        get() = TODO("Not yet implemented")
    actual val provincia: StateFlow<String?>
        get() = TODO("Not yet implemented")
    actual val isLoginEnable: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual val isLoading: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual val isOpen: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual val showDialog: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual val errorMensage: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    actual fun onLogChange(username: String, contrasenia: String) {
    }

    actual fun onRegisterChange(
        username: String,
        contrasenia: String,
        municipio: String,
        provincia: String,
        email: String
    ) {
    }

    actual fun onShowDialog(showDialog: Boolean) {
    }

    actual fun textErrorChange(textError: String) {
    }

    actual fun onIsLoading(isLoading: Boolean) {
    }

    actual fun onIsOpen(isOpen: Boolean) {
    }

    actual fun newErrorText(error: String) {
    }

    protected actual fun loginEnable(username: String, contrasenia: String): Boolean {
        TODO("Not yet implemented")
    }

}