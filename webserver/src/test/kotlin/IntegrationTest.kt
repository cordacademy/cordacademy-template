import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.core.utilities.loggerFor
import net.corda.testing.core.TestIdentity
import net.corda.testing.driver.DriverParameters
import net.corda.testing.driver.NodeHandle
import net.corda.testing.driver.PortAllocation
import net.corda.testing.driver.driver
import net.corda.testing.node.TestCordapp
import net.corda.testing.node.User

abstract class IntegrationTest : AutoCloseable {

    private companion object {

        val cordapps = listOf<String>(
            // TODO : Add cordapps...
        )

        val IDENTITY_A = TestIdentity(CordaX500Name("PartyA", "London", "GB"))
        val IDENTITY_B = TestIdentity(CordaX500Name("PartyB", "New York", "US"))
        val IDENTITY_C = TestIdentity(CordaX500Name("PartyC", "Paris", "FR"))

        val RPC_USERS = listOf(User("guest", "letmein", permissions = setOf("ALL")))

        val log = loggerFor<IntegrationTest>()
    }

    private lateinit var _nodeA: NodeHandle
    private lateinit var _nodeB: NodeHandle
    private lateinit var _nodeC: NodeHandle

    protected val nodeA: NodeHandle get() = _nodeA
    protected val nodeB: NodeHandle get() = _nodeB
    protected val nodeC: NodeHandle get() = _nodeC

    fun start(action: () -> Unit) {
        val parameters = DriverParameters(
            isDebug = true,
            startNodesInProcess = true,
            waitForAllNodesToFinish = true,
            cordappsForAllNodes = cordapps.map { TestCordapp.findCordapp(it) },
            portAllocation = object : PortAllocation() {
                private var start = 10000
                override fun nextPort(): Int = start++
            }
        )

        driver(parameters) {
            _nodeA = startNode(providedName = IDENTITY_A.name, rpcUsers = RPC_USERS).getOrThrow()
            _nodeB = startNode(providedName = IDENTITY_B.name, rpcUsers = RPC_USERS).getOrThrow()
            _nodeC = startNode(providedName = IDENTITY_C.name, rpcUsers = RPC_USERS).getOrThrow()

            listOf(_nodeA, _nodeB, _nodeC).forEach {
                val identity = it.nodeInfo.legalIdentities.first()
                val rpcAddress = it.rpcAddress
                log.info("Node registered with RPC address '$rpcAddress' for node '$identity'.")
            }

            initialize()
            action()
            finalize()
        }
    }

    override fun close() = finalize()
    protected open fun initialize() = Unit
    protected open fun finalize() = Unit
}