package com.andela.data.network

import com.andela.data.network.Secrets.API_KEY
import com.andela.data.network.Secrets.BASE_URL
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

private const val NETWORK_LAYER_TAG = "NetworkLayer"
private const val APPLICATION_LAYER_TAG = "ApplicationLayer"

class RetrofitBuilder {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(getHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        val networkLogging = HttpLoggingInterceptor { message ->
            Timber.tag(NETWORK_LAYER_TAG).d(message)
        }
        val appLogging = HttpLoggingInterceptor { message ->
            Timber.tag(APPLICATION_LAYER_TAG).d(message)
        }

        networkLogging.level = HttpLoggingInterceptor.Level.BODY
        appLogging.level = HttpLoggingInterceptor.Level.BODY

        val headerAuthorizationInterceptor = Interceptor { chain ->
            var request = chain.request()
            val originalHttpUrl: HttpUrl = request.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build()
            val requestBuilder = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .url(url)
            request = requestBuilder.build()
            chain.proceed(request)
        }

        client.interceptors().add(headerAuthorizationInterceptor)
        client.interceptors().add(appLogging)
        client.addNetworkInterceptor(networkLogging)
        return client.build()
    }
}
