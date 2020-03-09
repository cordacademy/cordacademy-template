![Cordacademy logo](https://raw.githubusercontent.com/cordacademy/cordacademy.github.io/master/content/images/logo-combined.png)



1. [Introduction](#introduction)
2. [Quick Start Guide](#quick-start-guide)
3. [Running The Web Server](#running-the-web-server)
4. [Configuring Your CorDapp](#configuring-your-cordapp)
5. [Testing Your CorDapp](#testing-your-cordapp)
6. [Getting Template Updates](#getting-template-updates)
7. [Known Problems](#known-problems)
8. [Contribution](#contribution)



# Introduction

**The Cordacademy template is designed for building Corda applications with out-of-the-box functionality that "just works".**

We use this template as the basis for all playground and laboratory samples, but you could also use it as a convenient starting block for building your own CorDapps! We've done most of the heavy lifting, so you can focus on more important parts of your application.

### What's In The Box?

- All configuration and dependencies are structured and organised to make upgrading easier.
- Extensible classes for building consistent contract, workflow and integration tests.
- An in-memory node driver based Corda network for manual testing.
- A web-server with basic node, network and administration functionality.
- IntelliJ run configurations for gradle tasks, in-memory network and web servers.
- Postman configuration for all web-servers.



# Quick Start Guide

Getting started with this template should be really easy; clone, build, run...that should be all it takes!

From the terminal or from your favourite Git tool, clone the repository to your local machine.

```
git clone https://github.com/cordacademy/cordacademy-template.git
```

If you're continuing from the terminal, you can go ahead and build some nodes.

```
cd cordacademy-template
./gradlew deployNodes
```

You can find nodes in the `tasks/build/nodes` directory. They will be called `Notary`, `PartyA`, `PartyB` and `PartyC`. Given that this is only a template, the nodes won't do very much because there are no installed CorDapps, but you should be able to ensure that they start at least.

```
cd tasks/build/nodes/<NODE_NAME>
java -jar corda.jar
```

You can get a better feel for what the template offers if you open the repository with IntelliJ IDEA. Once you've imported the gradle project and built everything, you can use the run configuration drop down execute various tasks:

### Gradle Tasks

| Name                     | Action                        | Description                           |
| ------------------------ | ----------------------------- | ------------------------------------- |
| **Build**                | `./gradlew build`             | Runs a build                          |
| **Build (Clean)**        | `./gradlew clean build`       | Runs a clean build                    |
| **Deploy Nodes**         | `./gradlew deployNodes`       | Creates a Corda node deployment       |
| **Deploy Nodes (Clean)** | `./gradlew clean deployNodes` | Creates a clean Corda node deployment |
| **Test**                 | `./gradlew test`              | Runs all tests in the project         |

### Network & Server Tasks

- **Run In-Memory Network** starts the in-memory network driver with a notary and three party nodes. These nodes have been configured to use the same allocated RPC ports as `deployNodes` so that you don't need to edit your configuration between the two. Ensure that the in-memory network is fully running before you start any web server tasks.

- **Run WebServer (Party \*)** starts a Ktor web server for the specified party. 
- **Run All WebServers** combines individual tasks to run all of the Ktor web servers at once.

Web server ports are conveniently aligned with the node's RPC port number. Starting from port 8080, subtract 10000 from the RPC port number and add the remainder; for example:

| Party       | RPC Port | Web Port |
| ----------- | -------- | -------- |
| **Party A** | 10005    | 8085     |
| **Party B** | 10009    | 8089     |
| **Party C** | 10013    | 8093     |

### Tasks With Quasar

When configuring additional tasks, you might be prompted to include `quasar.jar` which can be found in the `lib` directory. Corda unit tests and node driver configurations require quasar to be passed as a `javaagent` VM argument, for example:

```
-ea -javaagent:lib/quasar.jar
```



# Running The Web Server

The web server provides some basic functionality  to help manage your node. The following endpoints are exposed for all web server instances:

| Verb | Endpoint                      | Description                                             |
| ---- | ----------------------------- | ------------------------------------------------------- |
| GET  | /admin/nmc/clear              | Clears the network map cache                            |
| GET  | /admin/nmc/refresh            | Refreshes the network map cache                         |
| GET  | /admin/flows/registered       | Lists all flows registered by the node                  |
| GET  | /admin/flows/draining         | Determines whether flow draining is enabled             |
| POST | /admin/flows/draining/enable  | Enables flow draining                                   |
| POST | /admin/flows/draining/disable | Disables flow draining                                  |
| GET  | /nodes                        | Details about the local and network nodes               |
| GET  | /nodes/time                   | Gets the local node time                                |
| GET  | /nodes/local                  | Gets the local node X.500 name                          |
| GET  | /nodes/network                | Gets the X.500 names of all nodes on the network        |
| GET  | /nodes/notaries               | Gets the X.500 names of all notary nodes on the network |
| GET  | /nodes/shutdown               | Determines whether a node shutdown is pending           |
| POST | /nodes/shutdown               | Shuts down the local node                               |



# Configuring Your CorDapp

The template contains empty modules for building your contract and workflow.

### CorDapp Configuration Variables

The project root's `build.gradle` file contains variables which allow you to control CorDapp configuration globally; for example:

```groovy
buildscript {
    ext {
        
        ...
        
        cordapp_platform_version = 5
        cordapp_signing_enabled = true
        cordapp_contract = 'TODO_REPLACE_WITH_CONTRACT_NAME'
        cordapp_workflow = 'TODO_REPLACE_WITH_WORKFLOW_NAME'
        cordapp_vendor = 'TODO_REPLACE_WITH_VENDOR_NAME'
        cordapp_license = 'TODO_REPLACE_WITH_LICENSE'
        cordapp_version = 1
    }
}
```



# Testing Your CorDapp

This template provides some extensible utility classes to help you write consistent unit and integration tests. The `ContractTest`, `FlowTest` and `IntegrationTest` abstract classes are designed to wrap all of the basic requirements for writing consistent contract, workflow and integration tests.

Keep an eye out for **TODO** comments in the test classes as you will need to add cordapp and contracts for each test class.

### ContractTest

The `ContractTest` class provides utility for implementing Corda mock service based tests. The following example illustrates how to consume this class:

```kotlin
package my.contract.tests

class MyContractTests : ContractTest() {

    @Test
    fun `My contract should do something awesome`() {
        services.ledger {
            transaction {
                input(DummyContract.ID, DUMMY_STATE)
                output(DummyContract.ID, DUMMY_STATE)
                command(keysOf(PARTY_A, PARTY_B), DummyContract.DummyCommand())
                failsWith(DummyContract.DummyCommand.MY_FIRST_CONTRACT_RULE)
            }
        }
    }
}
```

### FlowTest

The `FlowTest` class provides utility for implementing Corda mock network based tests. By extending this class you get access to:

- An in-memory Corda mock network.
- A default notary and associated party.
- 3 x test nodes and associated parties.
- `@BeforeEach` initialisation of the network, nodes and parties.
- `@AfterEach` finalisation of the network.
- Extensible post-setup initialization and pre-teardown finalization.
- A `run` utility method that returns a `CordaFuture<T>` having run the network.

The following example illustrates how to consume this class:

```kotlin
package my.workflow.tests

class MyWorkflowTests : FlowTest() {

    @Test
    fun `My flow should do something awesome`() {
    
        // Arrange
        val flow = MyCordaFlow(partyB)
        val timeout = Duration.ofSeconds(10)
        
        // Act
        val result = runNetwork {
            nodeA.startFlow(flow)
        }.getOrThrow(timeout)
        
        // Assert
        assertEquals(result, "cool!")
    }
}
```

### IntegrationTest

The `IntegrationTest` class provides utility for implementing Corda node driver based tests.

The following example illustrates how to consume this class:

```kotlin
package my.integration.tests

class MyIntegrationTests : IntegrationTest() {

    @Test
    fun `My node should do something awesome`() = start {
    
        // Arrange
        val timeout = Duration.ofSeconds(10)
        
        // Act
        val result = nodeA.rpc.startTrackedFlow(::MyCordaFlow, partyB).returnValue.getOrThrow(10)
        
        // Assert
        assertEquals(result, "cool!")
    }
}
```

**Note that due to the expensive nature of spinning up a driver based network, integration tests will be slow, as the network will be spun up and spun down for every test. We're working on improving this in future versions of the template.**

### MockNetwork

The `MockNetwork` class simply extends the `IntegrationTest` class, but also has a run configuration associated with it in IntelliJ, allowing you to execute a driver based in-memory Corda network for manual testing.



# Getting Template Updates

This repository will get updated periodically with new dependency versions and features. In order to pull these changes into derived repositories you will need to:

1. Add this template repository as a remote to derived repositories

```
git remote add template https://github.com/cordacademy/cordacademy-template.git
```

2. Fetch the changes to the template

```
git fetch --all
```

3. Merge the template master into the the derived repository

```
git merge template/master --allow-unrelated-histories
```

**BEWARE** This can be a destructive change as the histories are considered unrelated, so review the changes before you commit back to the derived repository.



# Known Problems

We've identified a few places where things don't "just work" quite as well as we'd like. We'll work on that, but to save you trawling the internet for solutions, we've documented as many known problems as possible.

### Incorrect Project Name

When you first import a forked or cloned copy of the template repository into IntelliJ you might notice that the project reports an incorrect name; for example, if your repository is called `my-first-cordapp`, then you might see the following at the top of your project view:

my-first-cordapp **[cordacademy-template]**

To fix this, open `settings.gradle` in your project and rename `rootProject.name`, for example:

```groovy
rootProject.name = 'my-first-cordapp'
```

### Quasar Doesn't Load

For some unknown reason, when forking or cloning the template repository, `lib/quasar.jar` does not get copied correctly. To check that you have the correct version, execute the following command from your `lib` folder:

#### Linux/MacOS

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

### IntelliJ Run Configurations Don't Work

Sometimes you will see a red cross over the top of an intelliJ run configuration indicating that it's not working. This usually happens because IntelliJ either forgets or resets which module the run configuration is intended to run from. You can _usually_ fix this by selecting the appropriate module.

# Contribution

Cordacademy is an open-source initiative and we'd like to encourage contribution to the Corda ecosystem through Cordacademy.