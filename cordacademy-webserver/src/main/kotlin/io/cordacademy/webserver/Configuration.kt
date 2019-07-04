package io.cordacademy.webserver

import java.util.*

class Configuration(private val filePath: String = System.getProperty("config.filepath")) {

    val serverPort: Int get() = properties.getProperty("config.server.port").toInt()

    val host: String get() = properties.getProperty("config.rpc.host")

    val port: Int get() = properties.getProperty("config.rpc.port").toInt()

    val username: String get() = properties.getProperty("config.rpc.username")

    val password: String get() = properties.getProperty("config.rpc.password")

    private val properties: Properties by lazy {
        val properties = Properties()
        val stream = javaClass.classLoader.getResourceAsStream(filePath)
        properties.load(stream)
        stream.close()
        properties
    }
}