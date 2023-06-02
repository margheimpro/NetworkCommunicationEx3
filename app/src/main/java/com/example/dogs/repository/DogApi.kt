package com.example.dogs.repository

import retrofit2.http.GET

interface DogApi {
        @GET("image/random")
        suspend fun getImage(): DogImage
}