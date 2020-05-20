package com.da.tlstest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
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
            .baseUrl(BASE_URL)
            .build()
            .create(IApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            lifecycleScope.launch {
                text.text = testTLS(retrofit.check())
            }
        }
        webView.loadUrl(BASE_URL)
    }

    private suspend fun testTLS(responseBody: ResponseBody) = withContext(Dispatchers.IO) {
        val dom = Jsoup.parse(responseBody.byteStream(), null, BASE_URL)
        dom.body().getElementsByTag("div").firstOrNull {
            it.children().first().text() == "Version"
        }?.children()?.get(1)?.text()
    }

    interface IApi {
        @GET("/")
        suspend fun check(): ResponseBody
    }

    companion object {
        const val BASE_URL = "https://www.howsmyssl.com"
    }
}
