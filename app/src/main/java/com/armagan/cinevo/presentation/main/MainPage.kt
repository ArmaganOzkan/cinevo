package com.armagan.cinevo.presentation.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.armagan.cinevo.R
import com.armagan.cinevo.navigation.Screen
import com.armagan.cinevo.ui.theme.DiziboxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController){
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )
    var selectedindex by remember { mutableStateOf(0) }

    Scaffold(

        bottomBar = {
            NavigationBar(
                containerColor = Color(0xF4121212),
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
                            indicatorColor = Color.Gray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.White,
                            unselectedIconColor = Color.White

                        )

                    )
                }
            }
        },
        content = { padding ->
            when(selectedindex) {
                0 -> navController.navigate(Screen.HomeScreen.route)
                1 -> navController.navigate(Screen.SearchScreen.route)
                2 -> navController.navigate(Screen.ProfileScreen.route)


            }


        }
    )


}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiziboxTheme {
        val navController = rememberNavController()
        MainPage(navController =navController)
    }
}



sealed class BottomNavItem(val route: String, val icon: ImageVector, @StringRes val labelResId: Int){
    object Home: BottomNavItem("home-page",Icons.Default.Home, R.string.home)
    object Search : BottomNavItem("search-page", Icons.Default.Search, R.string.search)
    object Profile : BottomNavItem("profile-page", Icons.Default.Person, R.string.profile)
}