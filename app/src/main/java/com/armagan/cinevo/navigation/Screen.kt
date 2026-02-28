package com.armagan.cinevo.navigation

sealed class Screen (val route:String){

    object LoginScreen :Screen("login-page")
    object RegisterScreen : Screen("register-page")
    object MainScreen: Screen("main-page")
    object HomeScreen: Screen("home-page")
    object SearchScreen: Screen("search-page")
    object ProfileScreen: Screen("profile-page")
    object SupportScreen: Screen("support-page")
    object FavoriteScreen: Screen("favorite-page")
    object SettingsScreen: Screen("settings-page")
    object MyReviewsScreen: Screen("myreviews-page")

    object DetailsScreen : Screen("detail-page/{movieId}") {
        fun passMovieId(movieId: Int): String {
            return "detail-page/$movieId"
        }
    }
    object AllReviewsScreen : Screen("allreviews-page/{movieId}/{title}") {
        fun passArguments(movieId: String, title: String): String {

            val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
            return "allreviews-page/$movieId/$encodedTitle"
        }
    }
}