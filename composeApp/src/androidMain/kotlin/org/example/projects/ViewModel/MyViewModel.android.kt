// androidMain/kotlin/com/example/android/AndroidViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

actual abstract class SharedViewModel actual constructor() : ViewModel() {
    private val _textError = MutableStateFlow<String?>(null)
    actual val textError: StateFlow<String?> = _textError

    private val _username = MutableStateFlow<String?>(null)
    actual val username: StateFlow<String?> = _username

    private val _email = MutableStateFlow<String?>(null)
    actual val email: StateFlow<String?> = _email

    private val _contrasenia = MutableStateFlow<String?>(null)
    actual val contrasenia: StateFlow<String?> = _contrasenia

    private val _municipio = MutableStateFlow<String?>(null)
    actual val municipio: StateFlow<String?> = _municipio

    private val _provincia = MutableStateFlow<String?>(null)
    actual val provincia: StateFlow<String?> = _provincia

    private val _isLoginEnabled = MutableStateFlow(false)
    actual val isLoginEnable: StateFlow<Boolean> = _isLoginEnabled

    private val _isLoading = MutableStateFlow(false)
    actual val isLoading: StateFlow<Boolean> = _isLoading

    private val _isOpen = MutableStateFlow(false)
    actual val isOpen: StateFlow<Boolean> = _isOpen

    
    actual val showDialog: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    override fun onLogChange(username: String, contrasenia: String) {
        viewModelScope.launch {
            _username.value = username
            _contrasenia.value = contrasenia
            _isLoginEnabled.value = loginEnable(username, contrasenia)
        }
    }

    override fun loginEnable(username: String, contrasenia: String) =
        username.isNotEmpty() && contrasenia.length >= 3

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
}