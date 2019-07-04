package io.cordacademy.webserver.areas.nodes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import net.corda.core.messaging.CordaRPCOps
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * Defines the node routes for the web server.
 */
fun Route.nodeRoutes(rpc: CordaRPCOps) = route("/nodes") {

    get {
        val nodeTime = ZonedDateTime.ofInstant(rpc.currentNodeTime(), ZoneOffset.UTC)
        val localNode = rpc.nodeInfo().legalIdentities.first().toString()
        val networkNodes = rpc.networkMapSnapshot().map { it.legalIdentities.first().toString() }
        val notaryNodes = rpc.notaryIdentities().map { it.toString() }
        call.respond(
            HttpStatusCode.OK, mapOf(
                "currentNodeTime" to nodeTime,
                "localNode" to localNode,
                "networkNodes" to networkNodes,
                "notaryNodes" to notaryNodes
            )
        )
    }

    get("/time") {
        val nodeTime = ZonedDateTime.ofInstant(rpc.currentNodeTime(), ZoneOffset.UTC)
        call.respond(HttpStatusCode.OK, mapOf("currentNodeTime" to nodeTime))
    }

    get("/local") {
        val localNode = rpc.nodeInfo().legalIdentities.first().toString()
        call.respond(HttpStatusCode.OK, mapOf("localNode" to localNode))
    }

    get("/network") {
        val networkNodes = rpc.networkMapSnapshot().map { it.legalIdentities.first().toString() }
        call.respond(HttpStatusCode.OK, mapOf("networkNodes" to networkNodes))
    }

    get("/notaries") {
        val notaryNodes = rpc.notaryIdentities().map { it.toString() }
        call.respond(HttpStatusCode.OK, mapOf("notaryNodes" to notaryNodes))
    }

    get("/shutdown") {
        call.respond(HttpStatusCode.OK, mapOf("awaitingShutdown" to rpc.isWaitingForShutdown()))
    }

    post("/shutdown") {
        rpc.shutdown()
        call.respond(HttpStatusCode.OK, "Node is shutting down.")
    }
}