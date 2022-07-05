package com.csm.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csm.demo.entities.State;

public interface StateRepository extends JpaRepository<State, Integer>{
	
	@Query(value = "From State ss where ss.country.countryId=:countryId")
	List<State> getAllStateByCountryId(@Param("countryId") int countryId);

}
