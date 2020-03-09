package io.cordacademy.webserver

import java.util.*

class Configuration(private val filePath: String) {

    companion object {
        @JvmStatic
        val DEFAULT = Configuration(System.getProperty("config.filepath"))
    }

    val rpcHost: String get() = properties.getProperty("config.rpc.host")
    val rpcPort: Int get() = properties.getProperty("config.rpc.port").toInt()
    val rpcUsername: String get() = properties.getProperty("config.rpc.username")
    val rpcPassword: String get() = properties.getProperty("config.rpc.password")

    val webPort: Int get() = properties.getProperty("config.web.port").toInt()

    val cordaPlatformVersion: Int get() = properties.getProperty("config.corda.platformversion").toInt()

    private val properties: Properties by lazy {
        val properties = Properties()
        val stream = javaClass.classLoader.getResourceAsStream(filePath)
        properties.load(stream)
        stream?.close()
        properties
    }
}