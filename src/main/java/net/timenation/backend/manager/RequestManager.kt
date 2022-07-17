package net.timenation.backend.manager

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.timenation.backend.Backend
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class RequestManager {

    fun sendHttpRequest(storage: String, method: String, jsonObject: JsonObject) {
        val httpURLConnection = URL("http://localhost:8080/$storage").openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = method;
        httpURLConnection.useCaches = false;
        httpURLConnection.doOutput = true;

        val dataOutputStream = DataOutputStream(httpURLConnection.outputStream)
        dataOutputStream.writeBytes(jsonObject.toString())
        dataOutputStream.close()
    }
}