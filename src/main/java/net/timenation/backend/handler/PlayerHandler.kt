package net.timenation.backend.handler

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import net.timenation.backend.Backend
import net.timenation.backend.logger.LogType
import net.timenation.backend.logger.Logger
import net.timenation.backend.manager.DefaultHandler
import net.timenation.backend.player.CrystalUpdateType

class PlayerHandler : DefaultHandler(), HttpHandler {

    override fun handle(exchange: HttpExchange?) {
        Logger.log(exchange?.requestMethod, LogType.INFO)
        when (exchange?.requestMethod) {
            "GET" -> {
                if (getRequestAsJsonObject(exchange)!!["key"].asString == Backend.instance.playerKey) {
                    sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.getPlayersInformationAsJsonObject(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["plugin"].asString))
                    return
                } else {
                    Logger.log("WARNING: It could be that someone unknown tried to change something!", LogType.WARNING)
                    sendResponseToWebServer(exchange, 404, "Wrong key!")
                }
                return
            }
            "POST" -> {
                if (getRequestAsJsonObject(exchange)!!["key"].asString == Backend.instance.playerKey) {
                    sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.addPlayersInformationInDatabase(getRequestAsJsonObject(exchange)!!["name"].asString, getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["ip"].asString))
                    Logger.log("Created playerData for ${getRequestAsJsonObject(exchange)!!["name"].asString}(${getRequestAsJsonObject(exchange)!!["uuid"].asString} | ${getRequestAsJsonObject(exchange)!!["ip"].asString})", LogType.INFO)
                } else {
                    Logger.log("WARNING: It could be that someone unknown tried to change something!", LogType.WARNING)
                    sendResponseToWebServer(exchange, 404, "Wrong key!")
                }
                return
            }
            "PATCH" -> {
                if (getRequestAsJsonObject(exchange)!!["key"].asString == Backend.instance.playerKey) {
                    when (getRequestAsJsonObject(exchange)!!["type"].asString) {
                        "add_crystals" -> sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayersCrystals(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["crystals"].asInt, CrystalUpdateType.ADD_CRYSTALS))
                        "remove_crystals" -> sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayersCrystals(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["crystals"].asInt, CrystalUpdateType.REMOVE_CRYSTALS))
                        "set_crystals" -> sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayersCrystals(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["crystals"].asInt, CrystalUpdateType.SET_CRYSTALS))
                        "update_player" -> sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayerInformations(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["name"].asString, getRequestAsJsonObject(exchange)!!["crystals"].asInt, getRequestAsJsonObject(exchange)!!["lootboxes"].asInt, getRequestAsJsonObject(exchange)!!["helmet"].asString, getRequestAsJsonObject(exchange)!!["gadget"].asString, getRequestAsJsonObject(exchange)!!["language"].asString))
                        "update_playerinformation" -> sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayerInformation(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["username"].asString, getRequestAsJsonObject(exchange)!!["ip"].asString))
                        "update_playergadgethelmet" -> sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayerGadgetHelmet(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["helmet"].asString))
                        "update_playergadgetitem" -> sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayerGadgetItem(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["item"].asString))
                    }
                } else {
                    Logger.log("WARNING: It could be that someone unknown tried to change something!", LogType.WARNING)
                    sendResponseToWebServer(exchange, 404, "Wrong key!")
                }
                return
            }
            "PUT" -> {
                if (getRequestAsJsonObject(exchange)!!["key"].asString == Backend.instance.playerKey) {
                    if (getRequestAsJsonObject(exchange)!!["type"].asString == "update_player") sendResponseToWebServer(exchange, 200, Backend.instance.playerManager.updatePlayerInformations(getRequestAsJsonObject(exchange)!!["uuid"].asString, getRequestAsJsonObject(exchange)!!["name"].asString, getRequestAsJsonObject(exchange)!!["crystals"].asInt, getRequestAsJsonObject(exchange)!!["lootboxes"].asInt, getRequestAsJsonObject(exchange)!!["helmet"].asString, getRequestAsJsonObject(exchange)!!["gadget"].asString, getRequestAsJsonObject(exchange)!!["language"].asString))
                } else {
                    Logger.log("WARNING: It could be that someone unknown tried to change something!", LogType.WARNING)
                    sendResponseToWebServer(exchange, 404, "Wrong key!")
                }
                return
            }
        }
    }
}