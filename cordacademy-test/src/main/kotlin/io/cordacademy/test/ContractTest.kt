package io.cordacademy.test

import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices

/**
 * Provides utility for implementing Corda mock service based tests.
 *
 * @param cordapps A list of cordapps which should be loaded by the mock services.
 */
abstract class ContractTest(vararg cordapps: String) {

    /**
     * Gets the mocked Corda services available to this test.
     */
    protected val services = MockServices(cordapps.toList())

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
}