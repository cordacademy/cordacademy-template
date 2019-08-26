package io.cordacademy.webserver.areas.attachments

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import net.corda.core.crypto.SecureHash
import net.corda.core.messaging.CordaRPCOps
import java.io.ByteArrayInputStream

fun Route.attachmentRoutes(rpc: CordaRPCOps) = route("/attachments") {

    get {
        try {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("id parameter not provided.")
            call.respond(HttpStatusCode.OK, mapOf("exists" to rpc.attachmentExists(SecureHash.parse(id))))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
        }
    }

    post {
        try {
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        part.streamProvider().use {
                            val id = rpc.uploadAttachment(ByteArrayInputStream(it.readBytes()))
                            call.respond(HttpStatusCode.OK, mapOf("attachmentId" to id.toString()))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
        }
    }
}