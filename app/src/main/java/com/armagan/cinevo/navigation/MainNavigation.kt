package com.armagan.cinevo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.armagan.cinevo.presentation.allreviews.AllReviewsPage
import com.armagan.cinevo.presentation.contact.SupportPage
import com.armagan.cinevo.presentation.favorites.FavoritePage
import com.armagan.cinevo.presentation.home.detailpage.DetailPage
import com.armagan.cinevo.presentation.login.LoginPage
import com.armagan.cinevo.presentation.main.MainPage
import com.armagan.cinevo.presentation.home.homepage.HomePage
import com.armagan.cinevo.presentation.myreviews.MyReviewsPage
import com.armagan.cinevo.presentation.profile.ProfilePage
import com.armagan.cinevo.presentation.register.RegisterPage
import com.armagan.cinevo.presentation.search.SearchPage
import com.armagan.cinevo.presentation.settings.SettingsPage
import com.armagan.cinevo.util.LanguagePreference


@Composable
fun Navigation(startDestination:String,
               languageState: MutableState<String>,
               languagePref: LanguagePreference
){
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination =startDestination) {

        composable(route = Screen.LoginScreen.route){
            LoginPage(navController =navController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterPage(navController =navController)
        }
        composable(route = Screen.MainScreen.route){
            MainPage(navController = navController)
        }
        composable(route = Screen.HomeScreen.route){
            HomePage(navController = navController)
        }
        composable(route = Screen.SearchScreen.route){
            SearchPage(navController = navController)
        }
        composable(route = Screen.ProfileScreen.route){
            ProfilePage(navController = navController)
        }
        composable(route = Screen.SupportScreen.route){
            SupportPage(navController = navController)
        }
        composable(route = Screen.FavoriteScreen.route){
            FavoritePage(navController = navController)
        }
        composable(route = Screen.MyReviewsScreen.route){
            MyReviewsPage(navController = navController)
        }
        composable(route = Screen.SettingsScreen.route){
            SettingsPage(navController = navController,
                languageState = languageState,
                languagePref = languagePref
                )
        }
        composable(
            route = Screen.DetailsScreen.route
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: -1
            DetailPage(navController = navController, movieId = movieId)
        }

        composable(
            route = Screen.AllReviewsScreen.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
            val title = java.net.URLDecoder.decode(encodedTitle, "UTF-8")
            AllReviewsPage(navController = navController, movieId = movieId, title = title)
        }




    }
}
