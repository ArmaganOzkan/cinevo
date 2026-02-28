package com.armagan.cinevo.data.di

import com.armagan.cinevo.BuildConfig // Projenin BuildConfig dosyasını import et
import com.armagan.cinevo.data.remote.ApiService
import com.armagan.cinevo.data.remote.AuthInterceptor // Senin yazdığın Interceptor
import com.armagan.cinevo.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient // OkHttp import'u
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepository()


    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {

        return AuthInterceptor(BuildConfig.TMDB_API_KEY)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}