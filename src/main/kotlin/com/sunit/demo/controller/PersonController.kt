package com.sunit.demo.controller

import com.sunit.demo.dto.PersonDTO
import com.sunit.demo.exception.ResourceNotFoundException
import com.sunit.demo.models.Person
import com.sunit.demo.service.PersonService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/persons")
class PersonController(private val personService: PersonService) {

    @GetMapping
    fun getAllPersons(): List<PersonDTO> {
        val persons = personService.getAllPersons()
        return persons.map { mapPersonToDTO(it) }
    }

    @GetMapping("/{id}")
    fun getPersonById(@PathVariable id: Long): PersonDTO? {
        val person = personService.getPersonById(id)

        if (person != null)
            return mapPersonToDTO(person)
        else
            throw ResourceNotFoundException("Person with ID $id not found")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun savePerson(@RequestBody @Valid personDTO: PersonDTO): PersonDTO {
        val savedPerson = personService.savePerson(mapDTOToPerson(personDTO))
        return mapPersonToDTO(savedPerson)
    }

    @PutMapping("/{id}")
    fun updatePerson(@PathVariable id: Long, @RequestBody @Valid updatedPersonDTO: PersonDTO): PersonDTO? {
        val updatedPerson = personService.updatePerson(id, mapDTOToPerson(updatedPersonDTO))

        if (updatedPerson != null)
            return mapPersonToDTO(updatedPerson)
        else
            throw ResourceNotFoundException("Person with ID $id not found")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePerson(@PathVariable id: Long) {
        val existingPerson = personService.getPersonById(id)

        if (existingPerson != null)
            personService.deletePerson(id)
        else
            throw ResourceNotFoundException("Person with ID $id not found")
    }

    private fun mapPersonToDTO(person: Person): PersonDTO {
        return PersonDTO(person.id, person.firstName, person.lastName)
    }

    private fun mapDTOToPerson(personDTO: PersonDTO): Person {
        return Person(personDTO.id, personDTO.firstName, personDTO.lastName)
    }
}
