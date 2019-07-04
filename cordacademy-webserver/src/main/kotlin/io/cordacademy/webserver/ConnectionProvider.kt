package io.cordacademy.webserver

import net.corda.client.rpc.CordaRPCClient
import net.corda.client.rpc.CordaRPCConnection
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort

class ConnectionProvider(private val configuration: Configuration = Configuration()) : AutoCloseable {

    val proxy: CordaRPCOps by lazy { connection.proxy }

    private val connection: CordaRPCConnection by lazy {
        val address = NetworkHostAndPort(configuration.host, configuration.port)
        val client = CordaRPCClient(address)
        client.start(configuration.username, configuration.password)
    }

    override fun close() = connection.notifyServerAndClose()
}