package io.cordacademy.webserver

import net.corda.client.rpc.CordaRPCClient
import net.corda.client.rpc.CordaRPCClientConfiguration
import net.corda.client.rpc.CordaRPCConnection
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin

@CrossOrigin
abstract class Controller(private val configuration: Configuration = Configuration.DEFAULT) : AutoCloseable {

    protected val rpc: CordaRPCOps by lazy { connection.proxy }

    private val connection: CordaRPCConnection by lazy {
        val client = CordaRPCClient(
            hostAndPort = NetworkHostAndPort(configuration.rpcHost, configuration.rpcPort),
            configuration = CordaRPCClientConfiguration(
                minimumServerProtocolVersion = configuration.cordaPlatformVersion
            )
        )

        client.start(configuration.rpcUsername, configuration.rpcPassword)
    }

    override fun close() = connection.notifyServerAndClose()

    fun response(httpStatus: HttpStatus = HttpStatus.OK, action: () -> Any?): ResponseEntity<*> {
        return try {
            ResponseEntity(action(), httpStatus)
        } catch (e: Exception) {
            ResponseEntity(mapOf("error" to (e.message ?: "Bad request")), HttpStatus.BAD_REQUEST)
        }
    }
}