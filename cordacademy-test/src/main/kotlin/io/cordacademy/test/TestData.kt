package io.cordacademy.test

import net.corda.core.contracts.*
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.CordaX500Name
import net.corda.core.transactions.LedgerTransaction
import net.corda.testing.core.TestIdentity

val IDENTITY_A = TestIdentity(CordaX500Name("PartyA", "London", "GB"))
val IDENTITY_B = TestIdentity(CordaX500Name("PartyB", "New York", "US"))
val IDENTITY_C = TestIdentity(CordaX500Name("PartyC", "Paris", "FR"))

@BelongsToContract(DummyContract::class)
data class DummyState(override val participants: List<AbstractParty>) : ContractState

class DummyContract : Contract {

    companion object {
        val ID: ContractClassName = DummyContract::class.qualifiedName!!
    }

    override fun verify(tx: LedgerTransaction) = Unit

    class DummyCommand : CommandData
}