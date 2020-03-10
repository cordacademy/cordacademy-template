import net.corda.core.concurrent.CordaFuture
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.core.TestIdentity
import net.corda.testing.core.singleIdentity
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class FlowTest : AutoCloseable {

    protected companion object {

        val cordapps = listOf<String>(
            TODO("Add cordapps...")
        )

        val IDENTITY_A = TestIdentity(CordaX500Name("PartyA", "London", "GB"))
        val IDENTITY_B = TestIdentity(CordaX500Name("PartyB", "New York", "US"))
        val IDENTITY_C = TestIdentity(CordaX500Name("PartyC", "Paris", "FR"))

        fun partiesOf(vararg identities: TestIdentity) = identities.map { it.party }
        fun keysOf(vararg identities: TestIdentity) = identities.map { it.publicKey }
    }

    protected val network: MockNetwork get() = _network
    protected val notaryNode: StartedMockNode get() = _notaryNode
    protected val nodeA: StartedMockNode get() = _nodeA
    protected val nodeB: StartedMockNode get() = _nodeB
    protected val nodeC: StartedMockNode get() = _nodeC
    protected val notaryParty: Party get() = _notaryParty
    protected val partyA: Party get() = _partyA
    protected val partyB: Party get() = _partyB
    protected val partyC: Party get() = _partyC

    private lateinit var _network: MockNetwork
    private lateinit var _notaryNode: StartedMockNode
    private lateinit var _notaryParty: Party
    private lateinit var _nodeA: StartedMockNode
    private lateinit var _nodeB: StartedMockNode
    private lateinit var _nodeC: StartedMockNode
    private lateinit var _partyA: Party
    private lateinit var _partyB: Party
    private lateinit var _partyC: Party

    @BeforeAll
    private fun setup() {
        _network = MockNetwork(
            MockNetworkParameters(
                cordappsForAllNodes = cordapps.map { TestCordapp.findCordapp(it) },
                networkParameters = testNetworkParameters(
                    minimumPlatformVersion = 5
                )
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

    @AfterAll
    private fun tearDown() {
        network.stopNodes()
        finalize()
    }

    fun <T> runNetwork(function: () -> CordaFuture<T>): CordaFuture<T> {
        val result = function()
        network.runNetwork()
        return result
    }

    override fun close() = finalize()
    protected open fun initialize() = Unit
    protected open fun finalize() = Unit
}