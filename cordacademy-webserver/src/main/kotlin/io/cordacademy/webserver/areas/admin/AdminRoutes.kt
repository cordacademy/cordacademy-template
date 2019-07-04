package io.cordacademy.webserver.areas.admin

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import net.corda.core.messaging.CordaRPCOps

/**
 * Defines the admin routes for the web server.
 */
fun Route.adminRoutes(rpc: CordaRPCOps) = route("/admin") {

    get("/nmc/clear") {
        rpc.clearNetworkMapCache()
        call.respond(HttpStatusCode.OK, "Network map cache cleared.")
    }

    get("/nmc/refresh") {
        rpc.refreshNetworkMapCache()
        call.respond(HttpStatusCode.OK, "Network map cache refreshed.")
    }

    get("/flows/registered") {
        call.respond(HttpStatusCode.OK, mapOf("registeredFlows" to rpc.registeredFlows()))
    }

    get("/flows/draining") {
        call.respond(HttpStatusCode.OK, mapOf("flowDraining" to rpc.isFlowsDrainingModeEnabled()))
    }

    post("/flows/draining/enable") {
        rpc.setFlowsDrainingModeEnabled(true)
        call.respond(HttpStatusCode.OK, "Flow draining is enabled.")
    }

    post("/flows/draining/disable") {
        rpc.setFlowsDrainingModeEnabled(false)
        call.respond(HttpStatusCode.OK, "Flow draining is disabled.")
    }
}