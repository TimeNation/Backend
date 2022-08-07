package net.timenation.backend

import net.timenation.backend.config.BackendConfig
import net.timenation.backend.logger.LogType
import net.timenation.backend.logger.Logger
import net.timenation.backend.manager.RequestManager
import net.timenation.backend.mysql.MySQL
import net.timenation.backend.player.PlayerManager
import net.timenation.backend.server.WebServer
import java.io.File
import java.io.IOException
import java.util.*

fun main() {
    Logger.log("Trying to start TimeNation Backend...", LogType.INFO)
    val webServer = WebServer()
    webServer.startWebServer()
    Backend.instance.mysql = MySQL()
    Runtime.getRuntime().addShutdownHook(Thread { webServer.stopWebServer() })
    Logger.log("Successfully started TimeNation Backend!", LogType.SUCCESSFULLY)
}

class Backend {

    val requestManager: RequestManager = RequestManager()
    lateinit var mysql: MySQL
    val playerManager: PlayerManager = PlayerManager()
    val playerKey: String = "nmOGpnvkCrrIug8Hxdvot9VI2Jrfwz4ASI9O9zNQjIRihQwTqF"
    val timeConfig: BackendConfig = BackendConfig().loadConfig(File("config.json"))


    companion object {
        @JvmStatic
        val instance: Backend = Backend()
    }
}