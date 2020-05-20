# TLS 1.3 on older Android versions

This Project is a showcase of TLS 1.3+ support for Android 5+ (API 21+). 
To support older droids you can downgrade OkHttp version.

TLS implementation provided by [Conscrypt library](https://github.com/google/conscrypt/)

Application uses https://www.howsmyssl.com site to check TLS support.

Top part of the screen responsible for OkHttp (Retrofit) requests

Bottom part of the screen shows same site but in WebView, which uses PlayServices or System defaults, so in older Androids you can see that WebView uses TLS 1.2 and OkHttp TLS 1.3.
