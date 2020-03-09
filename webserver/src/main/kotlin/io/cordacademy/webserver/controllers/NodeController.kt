package io.cordacademy.webserver.controllers

import io.cordacademy.webserver.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
import java.time.ZonedDateTime

@RestController
@RequestMapping("/nodes")
class NodeController : Controller() {

    @GetMapping(produces = ["application/json"])
    fun getNodeInformation() = response {
        mapOf(
            "currentNodeTime" to ZonedDateTime.ofInstant(rpc.currentNodeTime(), ZoneOffset.UTC),
            "localNode" to rpc.nodeInfo().legalIdentities.first().toString(),
            "networkNodes" to rpc.networkMapSnapshot().map { it.legalIdentities.first().toString() },
            "notaryNodes" to rpc.notaryIdentities().map { it.toString() }
        )
    }

    @GetMapping("/time", produces = ["application/json"])
    fun getLocalNodeTime() = response {
        mapOf("currentNodeTime" to ZonedDateTime.ofInstant(rpc.currentNodeTime(), ZoneOffset.UTC))
    }

    @GetMapping("/local", produces = ["application/json"])
    fun getLocalNodeName() = response {
        mapOf("localNode" to rpc.nodeInfo().legalIdentities.first().toString())
    }

    @GetMapping("/network", produces = ["application/json"])
    fun getNetworkNodes() = response {
        mapOf("networkNodes" to rpc.networkMapSnapshot().map { it.legalIdentities.first().toString() })
    }

    @GetMapping("/notaries", produces = ["application/json"])
    fun getNotaryNodes() = response {
        mapOf("notaryNodes" to rpc.notaryIdentities().map { it.toString() })
    }

    @GetMapping("/shutdown", produces = ["application/json"])
    fun getAwaitingShutdown() = response {
        mapOf("awaitingShutdown" to rpc.isWaitingForShutdown())
    }

    @PostMapping("/shutdown", produces = ["application/json"])
    fun shutdown() = response {
        rpc.shutdown()
        mapOf("message" to "Node is shutting down.")
    }
}