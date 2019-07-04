package io.cordacademy.webserver

import java.util.*

/**
 * Provides a utility wrapper over a loaded property file.
 *
 * @param filePath The path to the configuration file to load.
 */
class Configuration(private val filePath: String = System.getProperty("config.filepath")) {

    /**
     * Gets the web server port.
     */
    val serverPort: Int get() = properties.getProperty("config.server.port").toInt()

    /**
     * Gets the RPC host name.
     */
    val host: String get() = properties.getProperty("config.rpc.host")

    /**
     * Gets the RPC port number.
     */
    val port: Int get() = properties.getProperty("config.rpc.port").toInt()

    /**
     * Gets the RPC username.
     */
    val username: String get() = properties.getProperty("config.rpc.username")

    /**
     * Gets the RPC password.
     */
    val password: String get() = properties.getProperty("config.rpc.password")

    private val properties: Properties by lazy {
        val properties = Properties()
        val stream = javaClass.classLoader.getResourceAsStream(filePath)
        properties.load(stream)
        stream.close()
        properties
    }
}