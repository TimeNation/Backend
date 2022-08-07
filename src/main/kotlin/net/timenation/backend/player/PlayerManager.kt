package net.timenation.backend.player

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.timenation.backend.Backend
import net.timenation.backend.logger.LogType
import net.timenation.backend.logger.Logger
import java.sql.ResultSet
import java.util.*

class PlayerManager {

    fun addPlayersInformationInDatabase(username: String, uuid: String, ip: String) : String {
        val jsonObject = JsonObject()
        val jsonObjectPlayerInfos = JsonObject()
        val jsonObjectGadgets = JsonObject()

        jsonObject.addProperty("username", username)
        jsonObject.addProperty("uuid", uuid)
        jsonObject.addProperty("language", "en")
        jsonObject.addProperty("crystals", 100)
        jsonObject.addProperty("lootboxes", 0)

        jsonObjectPlayerInfos.addProperty("ip", ip)
        jsonObjectPlayerInfos.addProperty("onlineTime", 0)
        jsonObject.add("playerInformation", jsonObjectPlayerInfos)

        jsonObjectGadgets.addProperty("helmet", "null")
        jsonObjectGadgets.addProperty("gadget", "null")
        jsonObject.add("gadgetData", jsonObjectGadgets)

        Backend.instance.mysql.updateDatabase("INSERT INTO playerData (playerUuid, playerName, playerData) VALUES ('$uuid', '$username', '$jsonObject')")
        return jsonObject.toString();
    }

    fun getPlayersInformationAsJsonObject(uuid: String, plugin: String) : String {
        val resultSet: ResultSet? = Backend.instance.mysql.getDatabaseResult("SELECT * FROM playerData WHERE playerUuid='${UUID.fromString(uuid)}'")
        val jsonObject = JsonObject()

        if (resultSet?.next() == true) with(jsonObject) {
            addProperty("username", resultSet.getString("playerName"))
            addProperty("uuid", resultSet.getString("playerUuid"))
            add("playerData", JsonParser().parse(resultSet.getString("playerData")) as JsonObject)

            Logger.log("$plugin getted playerData from " + jsonObject.get("username").asString, LogType.INFO)
            return jsonObject.toString()
        }

        Logger.log("$plugin can't get playerData from $uuid", LogType.ERROR)
        return "User not registed in database"
    }

    fun updatePlayerInformations(uuid: String, username: String, crystals: Int, lootboxes: Int, helmet: String, gadget: String, language: String) : String {
        val resultSet: ResultSet? = Backend.instance.mysql.getDatabaseResult("SELECT * FROM playerData WHERE playerUuid='$uuid'")

        if(resultSet?.next() == true) {
            val jsonObject: JsonObject = JsonParser().parse(resultSet.getString("playerData")).asJsonObject
            val jsonPlayerGadgetObject: JsonObject = jsonObject.getAsJsonObject("gadgetData")

            jsonObject.addProperty("username", username)
            jsonObject.addProperty("uuid", uuid)
            jsonObject.addProperty("language", language);
            jsonObject.addProperty("crystals", crystals);
            jsonObject.addProperty("lootboxes", lootboxes);

            jsonPlayerGadgetObject.addProperty("helmet", helmet)
            jsonPlayerGadgetObject.addProperty("gadget", gadget)

            jsonObject.add("gadgetData", jsonPlayerGadgetObject)
            Backend.instance.mysql.updateDatabase("UPDATE playerData SET playerData='$jsonObject' WHERE playerUuid='$uuid'")
            Logger.log("Update playerData for $username", LogType.INFO)
            return jsonObject.toString()
        }

        println("ERROR")

        Logger.log("UUID is not registed in the database", LogType.ERROR)
        return "UUID is not registed in the database"
    }

    fun updatePlayersCrystals(uuid: String, crystals: Int, crystalUpdateType: CrystalUpdateType) : String {
        val resultSet: ResultSet? = Backend.instance.mysql.getDatabaseResult("SELECT * FROM playerData WHERE playerUuid='$uuid'")

        if(resultSet?.next() == true) {
            val jsonObject: JsonObject = JsonParser().parse(resultSet.getString("playerData")).asJsonObject
            val jsonPlayerInformationObject: JsonObject = jsonObject.getAsJsonObject("playerInformation")

            when (crystalUpdateType) {
                CrystalUpdateType.ADD_CRYSTALS -> jsonPlayerInformationObject.addProperty("crystals", jsonPlayerInformationObject.get("crystals").asInt + crystals)
                CrystalUpdateType.REMOVE_CRYSTALS -> jsonPlayerInformationObject.addProperty("crystals", jsonPlayerInformationObject.get("crystals").asInt - crystals)
                CrystalUpdateType.SET_CRYSTALS -> jsonPlayerInformationObject.addProperty("crystals", crystals)
            }
            jsonObject.add("playerInformation", jsonPlayerInformationObject)
            Backend.instance.mysql.updateDatabase("UPDATE playerData SET playerData='$jsonObject' WHERE playerUuid='$uuid'")
            Logger.log("Update crystals for ${jsonObject.get("username").asString} to ${jsonPlayerInformationObject.get("crystals").asInt}", LogType.INFO)
            return jsonObject.toString()
        }

        Logger.log("UUID is not registed in the database", LogType.ERROR)
        return "UUID is not registed in the database"
    }

    fun updatePlayerInformation(uuid: String, username: String, ip: String) : String {
        val resultSet: ResultSet? = Backend.instance.mysql.getDatabaseResult("SELECT * FROM playerData WHERE playerUuid='$uuid'")

        if(resultSet?.next() == true) {
            val jsonObject: JsonObject = JsonParser().parse(resultSet.getString("playerData")).asJsonObject
            val jsonPlayerInformationObject: JsonObject = jsonObject.getAsJsonObject("playerInformation")

            jsonPlayerInformationObject.addProperty("ip", ip)
            jsonObject.addProperty("username", username)
            jsonObject.add("playerInformation", jsonPlayerInformationObject)
            Backend.instance.mysql.updateDatabase("UPDATE playerData SET playerName='$username', playerData='$jsonObject' WHERE playerUuid='$uuid'")
            Logger.log("Upated playerData from $username($uuid)", LogType.INFO)
            return jsonObject.toString()
        }

        Logger.log("UUID is not registed in the database", LogType.ERROR)
        return "UUID is not registed in the database"
    }

    fun updatePlayerGadgetHelmet(uuid: String, helmet: String) : String {
        val resultSet: ResultSet? = Backend.instance.mysql.getDatabaseResult("SELECT * FROM playerData WHERE playerUuid='$uuid'")

        if(resultSet?.next() == true) {
            val jsonObject: JsonObject = JsonParser().parse(resultSet.getString("playerData")).asJsonObject
            val jsonPlayerGadgetDataObject: JsonObject = jsonObject.getAsJsonObject("gadgetData")

            jsonPlayerGadgetDataObject.addProperty("helmet", helmet)
            jsonObject.add("gadgetData", jsonPlayerGadgetDataObject)
            Backend.instance.mysql.updateDatabase("UPDATE playerData SET playerData='$jsonObject' WHERE playerUuid='$uuid'")
            Logger.log("Upated playerGadgetData(Helmet) from ${jsonObject.get("username").asString}($uuid)", LogType.INFO)
            return jsonObject.toString()
        }

        Logger.log("UUID is not registed in the database", LogType.ERROR)
        return "UUID is not registed in the database"
    }

    fun updatePlayerGadgetItem(uuid: String, item: String) : String {
        val resultSet: ResultSet? = Backend.instance.mysql.getDatabaseResult("SELECT * FROM playerData WHERE playerUuid='$uuid'")

        if(resultSet?.next() == true) {
            val jsonObject: JsonObject = JsonParser().parse(resultSet.getString("playerData")).asJsonObject
            val jsonPlayerGadgetDataObject: JsonObject = jsonObject.getAsJsonObject("gadgetData")

            jsonPlayerGadgetDataObject.addProperty("gadget", item)
            jsonObject.add("gadgetData", jsonPlayerGadgetDataObject)
            Backend.instance.mysql.updateDatabase("UPDATE playerData SET playerData='$jsonObject' WHERE playerUuid='$uuid'")
            Logger.log("Upated playerGadgetData(Item) from ${jsonObject.get("username").asString}($uuid)", LogType.INFO)
            return jsonObject.toString()
        }

        Logger.log("UUID is not registed in the database", LogType.ERROR)
        return "UUID is not registed in the database"
    }
}

enum class CrystalUpdateType {
    ADD_CRYSTALS,
    REMOVE_CRYSTALS,
    SET_CRYSTALS
}