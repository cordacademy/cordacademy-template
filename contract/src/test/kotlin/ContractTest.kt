import net.corda.core.contracts.ContractClassName
import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class ContractTest : AutoCloseable {

    protected companion object {

        val cordapps = listOf<String>(
            // TODO : Add cordapps...
        )

        val contracts = listOf<ContractClassName>(
            // TODO : Add contracts...
        )

        val IDENTITY_A = TestIdentity(CordaX500Name("PartyA", "London", "GB"))
        val IDENTITY_B = TestIdentity(CordaX500Name("PartyB", "New York", "US"))
        val IDENTITY_C = TestIdentity(CordaX500Name("PartyC", "Paris", "FR"))

        fun partiesOf(vararg identities: TestIdentity) = identities.map { it.party }
        fun keysOf(vararg identities: TestIdentity) = identities.map { it.publicKey }
    }

    private lateinit var _services: MockServices
    protected val services: MockServices get() = _services

    @BeforeEach
    private fun setup() {
        _services = MockServices(cordapps)
        contracts.forEach { _services.addMockCordapp(it) }
        initialize()
    }

    @AfterEach
    private fun tearDown() = close()

    override fun close() = finalize()
    protected open fun initialize() = Unit
    protected open fun finalize() = Unit
}