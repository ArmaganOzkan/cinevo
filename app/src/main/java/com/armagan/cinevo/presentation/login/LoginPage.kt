package com.armagan.cinevo.presentation.login
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.armagan.cinevo.R
import com.armagan.cinevo.data.local.RememberMeRepository
import com.armagan.cinevo.navigation.Screen
import com.armagan.cinevo.ui.theme.DiziboxTheme
import com.armagan.cinevo.util.noRippleClick

@Composable
fun LoginPage(viewModel: LoginViewModel = hiltViewModel(),navController:NavController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val rememberMe = remember { mutableStateOf(false) }

    val loginSuccess by viewModel.loginSuccess
    val errorMessage by viewModel.errorMessage

    val context = LocalContext.current
    val rememberRepo = remember { RememberMeRepository(context) }

    val focusManager = LocalFocusManager.current


    LaunchedEffect(Unit) {
        rememberRepo.rememberMeFlow.collect { (remember, savedEmail) ->
            println("LoginPageremember: $remember, savedEmail: $savedEmail")

            if (remember && !savedEmail.isNullOrBlank()) {
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
            }
        }
    }
    LaunchedEffect (loginSuccess){
        if(loginSuccess){
            navController.navigate(Screen.MainScreen.route){
                popUpTo(Screen.LoginScreen.route){inclusive = true}
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .noRippleClick {
        focusManager.clearFocus()
    }) {
        Image(painter = painterResource(id = R.drawable.background),
            contentDescription = "BackgroundImage",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)

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
            Image(painter = painterResource(id = R.drawable.cinevo_logo),
                contentDescription = "LogoImage",
                modifier = Modifier.fillMaxWidth().padding(top=56.dp,bottom=2.dp),
                contentScale = ContentScale.FillWidth)
            Text(
                text = stringResource(id = R.string.greeting),
                fontSize = 36.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(22.dp))

            // E-posta TextField (Outline yerine standart TextField)
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(stringResource(id = R.string.mail)) },
                singleLine = true,
                leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
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
                leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe.value,
                    onCheckedChange = { rememberMe.value = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFDD3336),
                        uncheckedColor = Color.LightGray
                    )
                )
                Text(text = stringResource(id = R.string.rememberme), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {

                   viewModel.loginUser(email.value.trim(),password.value.trim(),rememberMe.value)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD3336))
            ) {
                Text(stringResource(id = R.string.login), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Text(modifier = Modifier.align(Alignment.End).padding(top = 6.dp).clickable {
                navController.navigate(Screen.RegisterScreen.route)
            }, text = stringResource(id = R.string.register), color = Color.White, fontWeight = FontWeight.SemiBold)

            if (!errorMessage.isNullOrBlank()) {
                Text(text = stringResource(id = R.string.wrongloginmessage), color = Color.LightGray, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}


