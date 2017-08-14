package com.slupicki

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version

interface PersonRepository: CrudRepository<Person, UUID> {
    fun findByLastName(lastName: String) : Iterable<Person>
}

@MappedSuperclass
open class Base (
        @Id val id: UUID = UUID.randomUUID(),
        @Version val version: Int = 0
)

@Entity
data class Person(val firstName: String = "", val lastName: String = "") : Base()
