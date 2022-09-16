package io.micronaut.example

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("io.micronaut.example")
                .mainClass(Application.javaClass)
                .start()
    }
}