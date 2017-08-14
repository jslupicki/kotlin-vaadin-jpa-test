package com.slupicki

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class KotlinVaadinJpaTestApplication {
    @Bean
    fun demo(repository : PersonRepository) = CommandLineRunner {
        repository.deleteAll()
        repository.save(listOf(
                Person("Piotr", "Ćwiąkalski"),
                Person("Jan", "Kowalski"),
                Person("Zdzisław", "Nowak")
        ))
        repository.findAll().forEach {
            println("Saved: $it")
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(KotlinVaadinJpaTestApplication::class.java, *args)
}
