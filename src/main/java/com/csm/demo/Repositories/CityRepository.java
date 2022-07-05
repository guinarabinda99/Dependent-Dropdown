package com.csm.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csm.demo.entities.City;

public interface CityRepository extends JpaRepository<City, Integer>{
	
	@Query(value = "From City cc where cc.stateId.stateId=:stateId and cc.countryId.countryId=:countryId")
	List<City> getAllCityByStateId(@Param("stateId") int stateId,@Param("countryId") int countryId);

}
