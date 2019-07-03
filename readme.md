![Cordacademy logo](https://raw.githubusercontent.com/cordacademy/cordacademy.github.io/master/content/images/logo-combined.png)

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

## Gradle Configuration

Since this template does not contain any CorDapp modules, there is no reason to declare CorDapp configurations. The following code snippets should be added to your CorDapp module `build.gradle` files  and modified accordingly:

#### Contract Modules

```groovy
cordapp {
    targetPlatformVersion cordapp_platform_version
    minimumPlatformVersion cordapp_platform_version
    contract {
        name "My First CorDapp Contract"
        vendor "NewCo"
        licence "Apache License, Version 2.0"
        versionId 1
    }
}
```

#### Workflow Modules

```groovy
cordapp {
    targetPlatformVersion cordapp_platform_version
    minimumPlatformVersion cordapp_platform_version
    workflow {
        name "My First CorDapp Workflow"
        vendor "NewCo"
        licence "Apache License, Version 2.0"
        versionId 1
    }
}
```

You will also need to define `cordapp_platform_version` in your `buildscript` configuration:

```groovy
buildscript {
    ext {
        cordapp_platform_version = 4
    }
}
```

_Keep an eye out for **TODO** comments in `build.gradle` files. They serve as indicators to things you will need to modify!_

## Known Issues

### Incorrect Project Name

When you first import a forked or cloned copy of the template repository into IntelliJ you might notice that the project reports an incorrect name; for example, if your repository is called `my-first-cordapp`, then you might see the following at the top of your project view:

my-first-cordapp **[cordacademy-template]**

To fix this, open `settings.gradle` in your project and rename `rootProject.name`, for example:

```groovy
rootProject.name = 'my-first-cordapp'
```

### Quasar Doesn't Load

For some unknown reason, when forking or cloning the template repository, `lib/quasar.jar` does not get copied correctly. To check that you have the correct version, execute the following command from your lib folder:

#### Linux/Mac

```shell
sha1sum quasar.jar
```

#### Windows (PowerShell)

```
Get-FileHash quasar.jar -Algorithm SHA1
```

The correct SHA-1 checksum for `quasar.jar` should be `3916162ad638c8a6cb8f3588a9f080dc792bc052`. If your repository is reporting a different SHA-1 checksum then you can download the correct version [here](https://github.com/cordacademy/cordacademy-template/raw/master/lib/quasar.jar).

### Unit Tests Don't Execute

You may encounter an issue where unit tests don't execute because of a lack of permission on `gradlew`. In order to correct this, type the following command into the terminal from the root of the repository:

```shell
sudo chmod +x ./gradlew
```

