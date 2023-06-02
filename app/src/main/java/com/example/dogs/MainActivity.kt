package com.example.dogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.dogs.databinding.ActivityMainBinding
import com.example.dogs.repository.DogApi
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_AUTHORIZATION_HEADER = "X-RapidAPI-Key"

class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().addHeader(
            API_AUTHORIZATION_HEADER,
            "ce46d04675msh025655b20284807p19bd2ejsn0d21d102e9bf"
        )
            .build()

        return chain.proceed(newRequest)
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val logging = HttpLoggingInterceptor()

    private val authorizationInterceptor = AuthorizationInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authorizationInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://dog-api.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val dogApi = retrofit.create(DogApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logging.level = HttpLoggingInterceptor.Level.BODY

        binding.randomDog.setOnClickListener{
            getPhoto()
        }
    }

    private fun getPhoto() {
        lifecycleScope.launch {
            try {
                val randomImage = dogApi.getImage()
                Picasso.get().load(randomImage.message).into(binding.randomDog)

            } catch (e: Exception) {
                Snackbar.make(
                    binding.mainView,
                    "Ups click again",
                    Snackbar.LENGTH_SHORT
                ).setAction("Retry") { getPhoto() }.show()
            }
        }
    }
}




