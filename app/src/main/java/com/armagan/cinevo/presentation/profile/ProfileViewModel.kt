package com.armagan.cinevo.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.local.RememberMeRepository
import com.armagan.cinevo.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository:AuthRepository,
    private val rememberMeRepository: RememberMeRepository
):ViewModel(){

    fun logout(onLogoutComplete: () ->Unit){
        viewModelScope.launch {
            authRepository.logoutUser()
            rememberMeRepository.clearRememberMe()
            onLogoutComplete()
        }
    }

}