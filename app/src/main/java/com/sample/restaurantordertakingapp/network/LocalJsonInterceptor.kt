package com.sample.restaurantordertakingapp.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody


class LocalJsonInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        // only intercept menu.json calls
        if (request.url.encodedPath.endsWith("menu.json")) {
            val json = context.assets.open("menu.json")
                .bufferedReader().use { it.readText() }
            return okhttp3.Response.Builder()
                .code(200)
                .message(json)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(json.toResponseBody("application/json".toMediaType()))
                .addHeader("content-type", "application/json")
                .build()
        }
        return chain.proceed(request)
    }
}

