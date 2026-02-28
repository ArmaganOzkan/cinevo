package com.armagan.cinevo.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.repository.AuthRepository
import com.armagan.cinevo.data.local.RememberMeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val rememberRepo: RememberMeRepository
) : ViewModel() {

    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: State<Boolean> = _loginSuccess

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String?> = _errorMessage

    fun loginUser(email: String, password: String, remember: Boolean) {
        repository.loginUser(email, password) { success, error ->
            if (success) {
                viewModelScope.launch {
                    rememberRepo.saveRememberMe(remember, email)
                    _loginSuccess.value = true
                }
            } else {
                _errorMessage.value = error ?: "Bilinmeyen hata"
            }
        }
    }
}
