package com.hackathon.airlineprojectpss.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.airlineprojectpss.entity.Inventory;





public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Inventory findByFlightNumberAndFlightDate(String flightNumber, String flightDate);
	
}
