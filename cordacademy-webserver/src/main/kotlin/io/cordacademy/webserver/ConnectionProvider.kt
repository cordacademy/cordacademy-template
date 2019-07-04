package io.cordacademy.webserver

import net.corda.client.rpc.CordaRPCClient
import net.corda.client.rpc.CordaRPCConnection
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort

/**
 * Provides an RPC connection to a node.
 *
 * @param configuration The configuration which will be used to obtain an RPC connection.
 */
class ConnectionProvider(private val configuration: Configuration = Configuration()) : AutoCloseable {

    /**
     * Gets the Corda RPC proxy interface.
     */
    val proxy: CordaRPCOps by lazy { connection.proxy }

    /**
     * Creates the RPC connection.
     */
    private val connection: CordaRPCConnection by lazy {
        val address = NetworkHostAndPort(configuration.host, configuration.port)
        val client = CordaRPCClient(address)
        client.start(configuration.username, configuration.password)
    }

    /**
     * Closes the RPC connection.
     */
    override fun close() = connection.notifyServerAndClose()
}