package com.amartyasingh.linkmanager.data.remote

import okhttp3.Interceptor
import okhttp3.Response


class Interceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }

}