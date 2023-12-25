package com.sunit.demo.service

import com.sunit.demo.dto.PersonDTO
import com.sunit.demo.models.Person
import com.sunit.demo.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonService(private val personRepository: PersonRepository) {

    fun getAllPersons(): List<Person> = personRepository.findAll()

    fun getPersonById(id: Long): Person? = personRepository.findById(id).orElse(null)

    fun savePerson(person: Person): Person = personRepository.save(person)

    fun updatePerson(id: Long, updatedPerson: Person): Person? {
        val existingPerson = getPersonById(id)
        return if (existingPerson != null) {
            val mergedPerson = existingPerson.copy(
                firstName = updatedPerson.firstName,
                lastName = updatedPerson.lastName
            )
            personRepository.save(mergedPerson)
        } else {
            null
        }
    }

    fun deletePerson(id: Long) {
        personRepository.deleteById(id)
    }

    private fun mapPersonToDTO(person: Person): PersonDTO {
        return PersonDTO(person.id, person.firstName, person.lastName)
    }

    private fun mapDTOToPerson(personDTO: PersonDTO): Person {
        return Person(personDTO.id, personDTO.firstName, personDTO.lastName)
    }
}
