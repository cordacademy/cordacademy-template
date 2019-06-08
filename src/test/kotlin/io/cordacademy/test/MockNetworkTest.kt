package io.cordacademy.test

import net.corda.core.concurrent.CordaFuture
import net.corda.core.identity.Party
import net.corda.testing.core.singleIdentity
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class MockNetworkTest(private vararg val cordapps: String) {

    //region initialization members

    private lateinit var _network: MockNetwork

    private lateinit var _notaryNode: StartedMockNode
    private lateinit var _nodeA: StartedMockNode
    private lateinit var _nodeB: StartedMockNode
    private lateinit var _nodeC: StartedMockNode

    private lateinit var _notaryParty: Party
    private lateinit var _partyA: Party
    private lateinit var _partyB: Party
    private lateinit var _partyC: Party

    //endregion

    protected val network: MockNetwork get() = _network

    protected val notaryNode: StartedMockNode get() = _notaryNode
    protected val nodeA: StartedMockNode get() = _nodeA
    protected val nodeB: StartedMockNode get() = _nodeB
    protected val nodeC: StartedMockNode get() = _nodeC

    protected val notaryParty: Party get() = _notaryParty
    protected val partyA: Party get() = _partyA
    protected val partyB: Party get() = _partyB
    protected val partyC: Party get() = _partyC

    @BeforeEach
    fun setup() {
        _network = MockNetwork(
            MockNetworkParameters(
                cordappsForAllNodes = cordapps.map { TestCordapp.findCordapp(it) }
            )
        )

        _notaryNode = network.defaultNotaryNode
        _nodeA = network.createPartyNode()
        _nodeB = network.createPartyNode()
        _nodeC = network.createPartyNode()

        _notaryParty = notaryNode.info.singleIdentity()
        _partyA = nodeA.info.singleIdentity()
        _partyB = nodeB.info.singleIdentity()
        _partyC = nodeC.info.singleIdentity()
    }

    @AfterEach
    fun tearDown() {
        network.stopNodes()
    }

    fun <T> run(function: () -> CordaFuture<T>): CordaFuture<T> {
        val result = function()
        network.runNetwork()
        return result
    }
}