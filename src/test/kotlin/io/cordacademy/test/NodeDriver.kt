package io.cordacademy.test

fun main(vararg args: String) = NodeDriver().setup()

/**
 * Represents an in-memory Corda network.
 */
class NodeDriver : IntegrationTestNetwork(
    // TODO : Uncomment the lines below and modify accordingly.
    // "my.first.cordapp.contract",
    // "my.first.cordapp.workflow"
)