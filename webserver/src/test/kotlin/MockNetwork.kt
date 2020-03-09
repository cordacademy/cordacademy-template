class MockNetwork : IntegrationTest() {
    companion object {
        @JvmStatic
        fun main(vararg args: String) = MockNetwork().start {
            println("Started mock network")
        }
    }
}