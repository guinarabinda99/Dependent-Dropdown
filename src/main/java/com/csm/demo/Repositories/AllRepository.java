package com.csm.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csm.demo.entities.AllCountryStateCity;

public interface AllRepository extends JpaRepository<AllCountryStateCity, Integer>{

}
