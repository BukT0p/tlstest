package com.da.tlstest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    private val retrofit: IApi by lazy {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS))
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        this.level = (HttpLoggingInterceptor.Level.BODY)
                    }).build()
            )
            .baseUrl("https://www.howsmyssl.com")
            .build()
            .create(IApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) performRequest()
    }

    private fun performRequest() {
        lifecycleScope.launch {
            val result = retrofit.check()
            val str = result.string()
            val messageStart = str.indexOf("Your client is using")
            text.text = str.substring(messageStart, messageStart + 300)
        }
    }

    interface IApi {
        @GET("/")
        suspend fun check(): ResponseBody
    }
}
