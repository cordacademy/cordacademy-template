package io.cordacademy.webserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Application {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val app = SpringApplication(Application::class.java)
            app.setDefaultProperties(mapOf("server.port" to Configuration.DEFAULT.webPort.toString()))
            app.run(*args)
        }
    }
}