package me.tim.org.familycar_kotlin.controller

import okhttp3.*

/**
 * Created by Nekkyou on 5-12-2017.
 */
object HttpController {
    val BASE_PATH = "http://rest.timdaniels.nl:8080/api"
    val jsonType = MediaType.parse("application/json; charset=utf-8")
    val client = OkHttpClient()

    fun run(url: String) : String {
        val request = Request.Builder()
                .url(BASE_PATH + url)
                .build()

        val response = client.newCall(request).execute()
        return response.body()?.string()!!
    }

    fun post(url: String) : String{
        val request = Request.Builder()
                .url(BASE_PATH + url)
                .post(RequestBody.create(jsonType, ""))
                .build()

        val response = client.newCall(request).execute()
        return response.body()?.string()!!
    }

    fun post(url: String, body: String) : String {
        val request = Request.Builder()
                .url(BASE_PATH + url)
                .post(RequestBody.create(jsonType, body))
                .build()

        val response = client.newCall(request).execute()
        return response.body()?.string()!!
    }
}