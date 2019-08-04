import io.cordacademy.test.IntegrationTest

class NodeDriver : IntegrationTest(
    // TODO : Add cordapp packages here; for example...
    // "my.first.cordapp.contract",
    // "my.first.cordapp.workflow"
) {
    companion object {
        @JvmStatic
        fun main(vararg args: String) = NodeDriver().start {}
    }
}