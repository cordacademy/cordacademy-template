![Cordacademy](https://raw.githubusercontent.com/cordacademy/cordacademy.github.io/master/resources/images/logo_text.png)

# Template

**Provides the base template for Cordacademy playground and laboratory samples.**

This repository is intended only as a template for building playground and laboratory samples. 

- It maintains the core Corda, Kotlin and JUnit dependencies. 
- It maintains Gradle run/debug task configurations such as `build` and `deployNodes`.
- It contains only code that is intended to be shared across playground and laboratory samples. 

Remember to pull changes to this repository into forks in order to keep dependencies and tasks up to date.

## Unit Test Helpers

This template provides a handy class for implementing unit tests that utilise the Corda in-memory mock network driver. To implement this class, ensure you have a Gradle dependency in your test module to the base project, for example:

```groovy
dependencies {
    testCompile project(":<project-name>")
}
```

The `MockNetworkTest` abstract class provides:

- An in-memory Corda mock network.
- A default notary and associated party.
- 3 x test nodes and associated parties.
- `@BeforeEach` initialisation of the network, nodes and parties.
- `@AfterEach` finalisation of the network.
- A `run` utility method that returns a `CordaFuture<T>` having run the network.

### Example

```kotlin
package my.unit.tests

class MyUnitTests : MockNetworkTest("my.cordapp.package") {

    @Test
    fun `my first Corda unit test should do something cool`() {
    
        // Arrange
        val flow = MyCordaFlow(partyB)
        val timeout = Duration.ofSeconds(10)
        
        // Act
        val result = run {
            nodeA.startFlow(flow)
        }.getOrThrow(timeout)
        
        // Assert
        assertEquals(result, "cool!")
    }
}
```

## Run/Debug Tasks

### Configurations

This template includes some default Gradle run/debug task configurations.

| Name                     | Action                        | Description                           |
| ------------------------ | ----------------------------- | ------------------------------------- |
| **Build**                | `./gradlew build`             | Runs a build                          |
| **Build (Clean)**        | `./gradlew clean build`       | Runs a clean build                    |
| **Deploy Nodes**         | `./gradlew deployNodes`       | Creates a Corda node deployment       |
| **Deploy Nodes (Clean)** | `./gradlew clean deployNodes` | Creates a clean Corda node deployment |
| **Test**                 | `./gradlew test`              | Runs all tests in the project         |

### Quasar

This template includes quasar which can be found in `lib/quasar.jar`. Corda unit tests and node driver configurations require quasar to be passed as a `javaagent` VM argument, for example:

```
-ea -javaagent:lib/quasar.jar
```

