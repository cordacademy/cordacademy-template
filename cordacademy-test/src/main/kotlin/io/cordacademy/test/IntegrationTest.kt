package io.cordacademy.test

import net.corda.core.utilities.getOrThrow
import net.corda.core.utilities.loggerFor
import net.corda.testing.driver.DriverParameters
import net.corda.testing.driver.NodeHandle
import net.corda.testing.driver.driver
import net.corda.testing.node.TestCordapp
import net.corda.testing.node.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Provides utility for implementing Corda node driver based tests.
 *
 * @param cordapps A list of cordapps which should be loaded by the node driver network.
 */
abstract class IntegrationTest(private vararg val cordapps: String) {

    private companion object {
        val log = loggerFor<IntegrationTest>()
    }

    private lateinit var _nodeA: NodeHandle
    private lateinit var _nodeB: NodeHandle
    private lateinit var _nodeC: NodeHandle

    /**
     * Gets handle to party A's node.
     */
    protected val nodeA: NodeHandle get() = _nodeA

    /**
     * Gets handle to party B's node.
     */
    protected val nodeB: NodeHandle get() = _nodeB

    /**
     * Gets handle to party C's node.
     */
    protected val nodeC: NodeHandle get() = _nodeC

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
            _nodeA = startNode(providedName = PARTY_A.name, rpcUsers = rpcUsers).getOrThrow()
            _nodeB = startNode(providedName = PARTY_B.name, rpcUsers = rpcUsers).getOrThrow()
            _nodeC = startNode(providedName = PARTY_C.name, rpcUsers = rpcUsers).getOrThrow()

            listOf(_nodeA, _nodeB, _nodeC).forEach { logStartedNode(it) }
        }
    }

    @AfterEach
    fun tearDown() {
        log.info("Closing down integration test network.")
    }

    private fun logStartedNode(node: NodeHandle) = log.info(
        "Node registered with RPC address '${node.rpcAddress}' for '${node.nodeInfo.legalIdentities.first()}'"
    )
}