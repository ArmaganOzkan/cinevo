package com.armagan.cinevo.presentation.settings

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.armagan.cinevo.R
import com.armagan.cinevo.ui.customcomposables.CustomDivider
import com.armagan.cinevo.util.LanguagePreference
import com.armagan.cinevo.util.ThemePreference
import com.armagan.cinevo.util.noRippleClick


@Composable
fun SettingsPage(navController: NavController,
                 languageState: MutableState<String>,
                 languagePref: LanguagePreference
){
    val context = LocalContext.current
    val themePref = remember { ThemePreference(context) }
    var isDarkTheme by remember { mutableStateOf(themePref.isDarkTheme()) }
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf(
        "Türkçe" to "tr",
        "English" to "en"
    )
    fun codeToName(code: String) = languages.find { it.second == code }?.first ?: "Türkçe"

    var selectedLanguage by remember { mutableStateOf(codeToName(languageState.value)) }

    val focusManager = LocalFocusManager.current



    Column(modifier = Modifier.fillMaxSize().padding(start = 6.dp)) {

        Row {
            IconButton(onClick = {
                navController.popBackStack()
            },
                modifier = Modifier.padding(top = 52.dp)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
            }
            Text(
                stringResource(id = R.string.settings),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 62.dp, start = 6.dp)
            )
        }

        CustomDivider(PaddingValues(bottom = 12.dp, top = 12.dp, start = 6.dp, end = 6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.darktheme), fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    isDarkTheme = it
                    themePref.setDarkTheme(it)
                    (context as? Activity)?.recreate()

                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClick { focusManager.clearFocus() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.language), fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = selectedLanguage,
                    fontSize = 16.sp
                )
            }


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 400.dp, y = 0.dp)
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(text = language.first) },
                        onClick = {
                            selectedLanguage = language.first
                            expanded = false

                            languagePref.setLanguage(language.second)
                            languageState.value = language.second

                            (context as? Activity)?.recreate()
                        }
                    )
                }
            }
        }
    }
}