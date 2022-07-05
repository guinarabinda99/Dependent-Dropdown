package com.csm.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "City")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cityId;
	private String cityName;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country countryId;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State stateId;

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Country getCountryId() {
		return countryId;
	}

	public void setCountryId(Country countryId) {
		this.countryId = countryId;
	}

	public State getStateId() {
		return stateId;
	}

	public void setStateId(State stateId) {
		this.stateId = stateId;
	}
	
	
	
}
