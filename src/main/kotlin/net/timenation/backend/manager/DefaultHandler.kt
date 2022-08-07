package net.timenation.backend.manager

import com.google.gson.JsonObject
import com.sun.net.httpserver.HttpExchange
import java.io.*
import java.nio.charset.Charset

open class DefaultHandler {

    protected fun getRequestParameters(exchange: HttpExchange): Map<String, String>? {
        val query = exchange.requestURI.query ?: return null
        val result: MutableMap<String, String> = HashMap()
        for (param in query.split("&").toTypedArray()) {
            val entry = param.split("=").toTypedArray()
            result[entry[0]] = if (entry.size > 1) entry[1] else ""
        }
        return result
    }

    protected fun getRequestAsJsonObject(exchange: HttpExchange): JsonObject? {
        val query = exchange.requestURI.query ?: return null
        val jsonObject = JsonObject()
        for (param in query.split("&").toTypedArray()) {
            val entry = param.split("=").toTypedArray()
            jsonObject.addProperty(entry[0], if (entry.size > 1) entry[1] else "")
        }

        return jsonObject
    }

    @Throws(IOException::class)
    protected fun getRequestBody(exchange: HttpExchange): String {
        val builder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(exchange.requestBody))
        reader.lines().forEach { str: String? -> builder.append(str) }
        reader.close()

        return builder.toString()
    }

    protected fun sendResponseToWebServer(exchange: HttpExchange, code: Int, response: String) {
        try {
            exchange.responseBody.use { stream ->
                val byteArray: ByteArray = response.toByteArray(Charset.forName("UTF-8"));
                exchange.sendResponseHeaders(code, byteArray.size.toLong())
                stream.write(byteArray)
                stream.flush()
                exchange.close()
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
}