package com.armagan.cinevo.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.repository.AuthRepository
import com.armagan.cinevo.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository:AuthRepository
): ViewModel(){

    var registerState by mutableStateOf<UiState>(UiState.Idle)
        private set
    fun register(email:String,password:String,name:String){
        viewModelScope.launch {
            registerState = UiState.Loading
            val result = repository.registerUser(email,password,name)
            registerState = if (result.isSuccess){
                UiState.Success("Kullanıcı başarıyla oluşturuldu")
            }else{
                UiState.Error("Kullanıcı oluşturulurken bir hata oldu")
            }
        }
    }


}