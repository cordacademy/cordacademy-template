package io.cordacademy.webserver.controllers

import io.cordacademy.webserver.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController : Controller() {

    @PostMapping("/nmc/clear", produces = ["application/json"])
    fun clearNetworkMapCache() = response {
        rpc.clearNetworkMapCache()
        mapOf("message" to "Network map cache cleared.")
    }

    @PostMapping("/nmc/refresh", produces = ["application/json"])
    fun refreshNetworkMapCache() = response {
        rpc.refreshNetworkMapCache()
        mapOf("message" to "Network map cache refreshed.")
    }

    @PostMapping("/flows/draining/enable", produces = ["application/json"])
    fun enableFlowDraining() = response {
        rpc.setFlowsDrainingModeEnabled(true)
        mapOf("message" to "Flow draining enabled.")
    }

    @PostMapping("/flows/draining/disable", produces = ["application/json"])
    fun disableFlowDraining() = response {
        rpc.setFlowsDrainingModeEnabled(false)
        mapOf("message" to "Flow draining disabled.")
    }

    @GetMapping("/flows/registered", produces = ["application/json"])
    fun getRegisteredFlows() = response {
        mapOf("registeredFlows" to rpc.registeredFlows())
    }

    @GetMapping("/flows/draining", produces = ["application/json"])
    fun getFlowDrainingStatus() = response {
        mapOf("flowDraining" to rpc.isFlowsDrainingModeEnabled())
    }
}