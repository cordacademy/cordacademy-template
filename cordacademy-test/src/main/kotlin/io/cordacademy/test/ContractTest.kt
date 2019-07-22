package io.cordacademy.test

import net.corda.core.contracts.ContractClassName
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Provides utility for implementing Corda mock service based tests.
 *
 * @param cordapps A list of cordapps which should be loaded by the mock services.
 */
abstract class ContractTest(
    private val cordapps: List<String>,
    private val contracts: List<ContractClassName>
) : AutoCloseable {

    protected companion object {

        /**
         * Gets a well known party for each of the specified test identities.
         *
         * @param identities The test identities from which to obtain well know parties.
         * @return Returns a collection of well known parties.
         */
        fun partiesOf(vararg identities: TestIdentity) = identities.map { it.party }

        /**
         * Gets a signing key for each of the specified test identities.
         *
         * @param identities The test identities from which to obtain signing keys.
         * @return Returns a collection of public signing keys.
         */
        fun keysOf(vararg identities: TestIdentity) = identities.map { it.publicKey }
    }

    private lateinit var _services: MockServices

    /**
     * Gets the mocked Corda services available to this test.
     */
    protected val services: MockServices get() = _services

    /**
     * Closes this resource, relinquishing any underlying resources.
     */
    override fun close() = finalize()

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
        _services = MockServices(cordapps)
        contracts.forEach { _services.addMockCordapp(it) }
        initialize()
    }

    /**
     * Finalizes the test container.
     */
    @AfterEach
    private fun tearDown() = close()
}