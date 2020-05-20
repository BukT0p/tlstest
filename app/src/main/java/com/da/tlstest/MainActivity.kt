package com.da.tlstest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    private val retrofit: IApi by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl(BASE_URL)
            .build()
            .create(IApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView.loadUrl(BASE_URL)
        lifecycleScope.launch {
            text.append(checkTLS())
        }
    }

    private suspend fun checkTLS(): String = withContext(Dispatchers.IO) {
        val responseBody = retrofit.check()
        val dom = Jsoup.parse(responseBody.byteStream(), null, BASE_URL)
        dom.body().getElementsByTag("div").firstOrNull {
            it.children().first().text() == "Version"
        }?.children()?.get(1)?.text() ?: "DOM changed, full body:\n\n${dom.body().text()}"
    }

    interface IApi {
        @GET("/")
        suspend fun check(): ResponseBody
    }

    companion object {
        const val BASE_URL = "https://www.howsmyssl.com"
    }
}