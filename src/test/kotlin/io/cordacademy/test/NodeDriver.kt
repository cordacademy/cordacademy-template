package io.cordacademy.test

import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.core.utilities.loggerFor
import net.corda.testing.driver.DriverParameters
import net.corda.testing.driver.NodeHandle
import net.corda.testing.driver.driver
import net.corda.testing.node.TestCordapp
import net.corda.testing.node.User

class NodeDriver {

    private companion object {

        @JvmStatic
        fun main(vararg args: String) {
            driver(parameters) {
                identities
                    .map { startNode(providedName = it, rpcUsers = rpcUsers).getOrThrow() }
                    .forEach { logStartedNode(it) }
            }
        }

        val logger = loggerFor<NodeDriver>()

        val rpcUsers: List<User> = listOf(User("guest", "letmein", permissions = setOf("ALL")))

        val cordapps: List<TestCordapp> = listOf(
            // TODO : Uncomment and modify with the package names for your cordapps.
            // TestCordapp.findCordapp("my.first.cordapp.contract"),
            // TestCordapp.findCordapp("my.first.cordapp.workflow")
        )

        val identities: List<CordaX500Name> = listOf(
            CordaX500Name("PartyA", "London", "GB"),
            CordaX500Name("PartyB", "New York", "US"),
            CordaX500Name("PartyC", "Paris", "FR")
        )

        val parameters = DriverParameters(
            isDebug = true,
            startNodesInProcess = true,
            waitForAllNodesToFinish = true,
            cordappsForAllNodes = cordapps
        )

        private fun logStartedNode(node: NodeHandle) = logger.info(
            "Node registered with RPC address '${node.rpcAddress}' for '${node.nodeInfo.legalIdentities.first()}'"
        )
    }
}