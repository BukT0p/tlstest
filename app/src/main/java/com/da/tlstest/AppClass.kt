package com.da.tlstest

import android.app.Application
import org.conscrypt.Conscrypt
import java.security.Security

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
    }
}