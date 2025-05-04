package com.example.gamedealshunter.data.network

import org.koin.core.module.Module
import org.koin.dsl.module
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType

@OptIn(ExperimentalSerializationApi::class)
val networkModule: Module = module {

    single {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl("https://www.cheapshark.com/api/1.0/")
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .client(get())
            .build()
            .create(CheapSharkApi::class.java)
    }
}
