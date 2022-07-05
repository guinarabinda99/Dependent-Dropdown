package com.csm.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csm.demo.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>{

}
