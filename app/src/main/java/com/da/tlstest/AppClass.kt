package com.da.tlstest

import android.app.Application
import android.os.Build
import org.conscrypt.Conscrypt
import java.security.Security


class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Security.insertProviderAt(Conscrypt.newProvider(), 1)
        } else {
            TODO("Add support for older versions")
        }
    }
}