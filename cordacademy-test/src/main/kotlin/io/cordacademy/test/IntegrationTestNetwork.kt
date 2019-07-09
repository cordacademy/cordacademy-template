package io.cordacademy.test

import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.core.utilities.loggerFor
import net.corda.testing.driver.DriverParameters
import net.corda.testing.driver.NodeHandle
import net.corda.testing.driver.driver
import net.corda.testing.node.TestCordapp
import net.corda.testing.node.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class IntegrationTestNetwork(private vararg val cordapps: String) {

    private companion object {
        val logger = loggerFor<NodeDriver>()
    }

    private lateinit var nodeA: NodeHandle
    private lateinit var nodeB: NodeHandle
    private lateinit var nodeC: NodeHandle

    val identityA = CordaX500Name("PartyA", "London", "GB")
    val identityB = CordaX500Name("PartyB", "New York", "US")
    val identityC = CordaX500Name("PartyC", "Paris", "FR")

    @BeforeEach
    fun setup() {
        val rpcUsers: List<User> = listOf(User("guest", "letmein", permissions = setOf("ALL")))

        val parameters = DriverParameters(
            isDebug = true,
            startNodesInProcess = true,
            waitForAllNodesToFinish = true,
            cordappsForAllNodes = cordapps.map { TestCordapp.findCordapp(it) }
        )

        driver(parameters) {
            nodeA = startNode(providedName = identityA, rpcUsers = rpcUsers).getOrThrow()
            nodeB = startNode(providedName = identityB, rpcUsers = rpcUsers).getOrThrow()
            nodeC = startNode(providedName = identityC, rpcUsers = rpcUsers).getOrThrow()

            listOf(nodeA, nodeB, nodeC).forEach { logStartedNode(it) }
        }
    }

    @AfterEach
    fun tearDown() {
        logger.info("Closing down integration test network.")
    }

    private fun logStartedNode(node: NodeHandle) = logger.info(
        "Node registered with RPC address '${node.rpcAddress}' for '${node.nodeInfo.legalIdentities.first()}'"
    )
}