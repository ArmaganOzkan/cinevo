package com.armagan.cinevo.presentation.register

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.armagan.cinevo.R
import com.armagan.cinevo.model.UiState
import com.armagan.cinevo.navigation.Screen
import com.armagan.cinevo.util.noRippleClick
import kotlinx.coroutines.launch



@Composable
fun RegisterPage(viewModel: RegisterViewModel = hiltViewModel(), navController: NavController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val context = LocalContext.current

    val state = viewModel.registerState

    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current


    Box(modifier = Modifier.fillMaxSize()
        .noRippleClick {
        focusManager.clearFocus()
    }
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "BackgroundImage",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.cinevo_logo),
                contentDescription = "LogoImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, bottom = 2.dp),
                contentScale = ContentScale.FillWidth
            )

            Text(
                text = stringResource(id = R.string.register),
                fontSize = 36.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(22.dp))

            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(stringResource(id = R.string.username)) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(stringResource(id = R.string.mail)) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(stringResource(id = R.string.password)) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val trimmedemail = email.value.trim()
                    val trimmedpassword = password.value.trim()
                    val trimmedusername = username.value.trim()

                    when{
                        trimmedemail.isBlank() || trimmedusername.isBlank() || trimmedpassword.isBlank() ->{
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(context.getString(R.string.pleasefillallblanks))
                            }
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(trimmedemail).matches() ->{
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(context.getString(R.string.entervalidmail))
                            }
                        }
                        trimmedpassword.length<6 ->{
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(context.getString(R.string.passwordmustbe))
                            }
                        }
                        else ->{
                            viewModel.register(trimmedemail,trimmedpassword,trimmedusername)
                            when (state) {
                                is UiState.Success -> {
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                                is UiState.Error -> {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Kayıt sırasında bir hata oluştu.")//dildestek ekle

                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD3336))
            ) {
                Text(
                    stringResource(id = R.string.register),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 6.dp)
                    .clickable {
                        navController.navigate(Screen.LoginScreen.route)
                    },
                text = stringResource(id = R.string.registerpageslogin),
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
        )
    }
}

