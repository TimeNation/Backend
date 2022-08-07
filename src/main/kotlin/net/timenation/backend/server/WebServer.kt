package net.timenation.backend.server

import com.sun.net.httpserver.HttpServer
import net.timenation.backend.handler.PlayerHandler
import java.net.InetSocketAddress

class WebServer {
    private var httpServer: HttpServer? = null

    init {
        httpServer = HttpServer.create(InetSocketAddress("0.0.0.0", 8080), 0)
        httpServer?.createContext("/player", PlayerHandler())
    }

    fun startWebServer() {
        httpServer?.start()
    }

    fun stopWebServer() {
        httpServer?.stop(0)
    }
}