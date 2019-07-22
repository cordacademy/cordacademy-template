package io.cordacademy.test

import net.corda.core.concurrent.CordaFuture
import net.corda.core.flows.FlowLogic
import net.corda.core.identity.Party
import net.corda.testing.core.singleIdentity
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Provides utility for implementing Corda mock network based tests.
 *
 * @param cordapps A list of cordapps which should be loaded by the mock network.
 */
abstract class FlowTest(private vararg val cordapps: String) : AutoCloseable {

    private lateinit var _network: MockNetwork

    private lateinit var _notaryNode: StartedMockNode
    private lateinit var _notaryParty: Party

    private lateinit var _nodeA: StartedMockNode
    private lateinit var _partyA: Party

    private lateinit var _nodeB: StartedMockNode
    private lateinit var _partyB: Party

    private lateinit var _nodeC: StartedMockNode
    private lateinit var _partyC: Party

    /**
     * Gets the mock network.
     */
    protected val network: MockNetwork get() = _network

    /**
     * Gets the default notary's node.
     */
    protected val notaryNode: StartedMockNode get() = _notaryNode

    /**
     * Gets participant A's node.
     */
    protected val nodeA: StartedMockNode get() = _nodeA

    /**
     * Gets participant B's node.
     */
    protected val nodeB: StartedMockNode get() = _nodeB

    /**
     * Gets participant C's node.
     */
    protected val nodeC: StartedMockNode get() = _nodeC

    /**
     * Gets the default notary's identity.
     */
    protected val notaryParty: Party get() = _notaryParty

    /**
     * Gets participant A's identity.
     */
    protected val partyA: Party get() = _partyA

    /**
     * Gets participant B's identity.
     */
    protected val partyB: Party get() = _partyB

    /**
     * Gets participant C's identity.
     */
    protected val partyC: Party get() = _partyC

    /**
     * Closes this resource, relinquishing any underlying resources.
     */
    override fun close() = finalize()

    /**
     * Runs the mock network and returns a {@code CordaFuture<T>) from the specified function.
     *
     * @param function The function which will be run by the network.
     * @return Returns a {@code CordaFuture<T>) from the specified function.
     */
    fun <T> run(function: () -> CordaFuture<T>): CordaFuture<T> {
        val result = function()
        network.runNetwork()
        return result
    }

    /**
     * Provides a generic mechanism for registering initiated flows.
     *
     * @param T The initiated flow to register.
     */
    protected inline fun <reified T : FlowLogic<*>> StartedMockNode.registerInitiatedFlow() {
        registerInitiatedFlow(T::class.java)
    }

    /**
     * Provides post startup test initialization.
     */
    protected open fun initialize() = Unit

    /**
     *Provides pre tear-down test finalization.
     */
    protected open fun finalize() = Unit

    /**
     * Initializes the test container.
     */
    @BeforeEach
    private fun setup() {
        _network = MockNetwork(
            MockNetworkParameters(
                cordappsForAllNodes = cordapps.map { TestCordapp.findCordapp(it) }
            )
        )

        _notaryNode = network.defaultNotaryNode
        _nodeA = network.createPartyNode(IDENTITY_A.name)
        _nodeB = network.createPartyNode(IDENTITY_B.name)
        _nodeC = network.createPartyNode(IDENTITY_C.name)

        _notaryParty = notaryNode.info.singleIdentity()
        _partyA = nodeA.info.singleIdentity()
        _partyB = nodeB.info.singleIdentity()
        _partyC = nodeC.info.singleIdentity()

        initialize()
    }

    /**
     * Finalizes the test container.
     */
    @AfterEach
    private fun tearDown() {
        close()
        network.stopNodes()
    }
}