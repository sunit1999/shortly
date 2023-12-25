package com.sunit.demo.repository

import com.sunit.demo.models.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository: JpaRepository<Person, Long>
