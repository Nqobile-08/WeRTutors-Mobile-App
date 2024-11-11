package com.ratjatji.eskhathinitutors.Admin

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("registerToken")
    fun sendToken(@Body token: TokenRequest): Call<Void>
}

data class TokenRequest(val token: String)
