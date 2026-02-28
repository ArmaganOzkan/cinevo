package com.armagan.cinevo.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.armagan.cinevo.R
import com.armagan.cinevo.navigation.Screen
import com.armagan.cinevo.presentation.main.BottomNavItem
import com.armagan.cinevo.ui.customcomposables.BlurredDarkBackground
import com.armagan.cinevo.ui.customcomposables.CustomDivider
import com.armagan.cinevo.ui.theme.ThemeViewModel
import com.armagan.cinevo.util.quotes

@Composable
fun ProfilePage(navController: NavController,
                viewModel: ProfileViewModel = hiltViewModel()

){

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )
    var selectedindex by remember { mutableStateOf(2) }
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val randomQuote by remember { mutableStateOf(quotes.random()) }


    Box(modifier=Modifier.fillMaxSize()) {

        if(isDarkTheme){
            BlurredDarkBackground()
        }


        Scaffold(
            containerColor = if(isDarkTheme) Color.Transparent else MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = if(isDarkTheme) 0.15f else 1.0f),
                    tonalElevation = 0.dp
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = index == selectedindex,
                            onClick = { selectedindex = index },
                            icon = { Icon(imageVector = item.icon, contentDescription = "") },
                            label = { Text(stringResource(id = item.labelResId)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                indicatorColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground

                            )

                        )
                    }
                }
            },
            content = { padding ->
                when (selectedindex) {
                    0 -> navController.navigate(Screen.HomeScreen.route)
                    1 -> navController.navigate(Screen.SearchScreen.route)
                }




                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = if(isDarkTheme) Color.Transparent else MaterialTheme.colorScheme.background)
                        .padding(top = 62.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(id = R.string.profile),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        CustomDivider(
                            paddingval = PaddingValues(
                                horizontal = 0.dp,
                                vertical = 16.dp
                            )
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = randomQuote,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 10.dp, start = 3.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.secondary,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ProfileItem(
                            title = stringResource(id = R.string.favorites),
                            icon = Icons.Default.Favorite
                        ) {
                            navController.navigate(Screen.FavoriteScreen.route)
                        }
                        ProfileItem(
                            title = stringResource(id = R.string.myreviews),
                            icon = Icons.Filled.Create
                        ) {
                            navController.navigate(Screen.MyReviewsScreen.route)
                        }

                        ProfileItem(
                            title = stringResource(id = R.string.settings),
                            icon = Icons.Default.Settings
                        ) {
                            navController.navigate(Screen.SettingsScreen.route)
                        }
                        ProfileItem(
                            title = stringResource(id = R.string.support),
                            icon = Icons.Default.Info
                        ) {
                            navController.navigate(Screen.SupportScreen.route)
                        }
                        ProfileItem(
                            title = stringResource(id = R.string.logout),
                            icon = Icons.AutoMirrored.Default.ExitToApp
                        ) {

                            viewModel.logout {
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(Screen.ProfileScreen.route) { inclusive = true }
                                }
                            }
                        }


                    }
                }

            }
        )
    }

}
@Composable
fun ProfileItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column {
        ListItem(
            headlineContent = { Text(title, color = MaterialTheme.colorScheme.onBackground) },
            leadingContent = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier
                .clickable { onClick() }
            , colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}