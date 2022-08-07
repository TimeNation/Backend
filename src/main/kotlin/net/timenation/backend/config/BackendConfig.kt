package net.timenation.backend.config

import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets


/**
 * Created by EinsLuca
 * Class create at 17.07.2022 18:30 @Backend
 **/
class BackendConfig {

    var mysqlPassword = ""

    fun loadConfig(file: File): BackendConfig {
        if (!file.exists()) {
            val timeConfig = BackendConfig()
            timeConfig.mysqlPassword = "change pls"
            try {
                FileOutputStream(file).use { fileOutputStream ->
                    fileOutputStream.write(
                        Gson().toJson(timeConfig).toByteArray(StandardCharsets.UTF_8)
                    )
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            return timeConfig
        }
        try {
            FileInputStream(file).use { fileInputStream ->
                return Gson().fromJson(
                    String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    BackendConfig::class.java
                )
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

}