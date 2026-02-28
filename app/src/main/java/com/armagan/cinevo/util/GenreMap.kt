package com.armagan.cinevo.util

import java.util.Locale

object GenreUtil {

    private val genreMapEn = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War",
        37 to "Western"
    )

    private val genreMapTr = mapOf(
        28 to "Aksiyon",
        12 to "Macera",
        16 to "Animasyon",
        35 to "Komedi",
        80 to "Suç",
        99 to "Belgesel",
        18 to "Dram",
        10751 to "Aile",
        14 to "Fantastik",
        36 to "Tarih",
        27 to "Korku",
        10402 to "Müzik",
        9648 to "Gizem",
        10749 to "Romantik",
        878 to "Bilim Kurgu",
        10770 to "TV Filmi",
        53 to "Gerilim",
        10752 to "Savaş",
        37 to "Western"
    )

    fun getGenreNames(ids: List<Int>, locale: Locale = Locale.getDefault()): String {
        val map = if (locale.language == "tr") genreMapTr else genreMapEn
        return ids.mapNotNull { map[it] }.joinToString(", ")
    }

    fun getGenreIds(names: List<String>, locale: Locale = Locale.getDefault()): List<Int> {
        val map = if (locale.language == "tr") genreMapTr else genreMapEn
        val lowerCaseNames = names.map { it.lowercase(locale) }

        return map.filter { (id, name) ->
            name.lowercase(locale) in lowerCaseNames
        }.keys.toList()
    }
    fun getAllGenres(locale: Locale = Locale.getDefault()): List<String> {
        val map = if (locale.language == "tr") genreMapTr else genreMapEn
        return map.values.toList()
    }
    fun getAllGenreIds(): List<Int> {
        return genreMapEn.keys.toList()
    }

}
