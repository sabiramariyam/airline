package com.hackathon.airlineprojectpss.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.airlineprojectpss.entity.BookingRecord;




public interface BookingRepository extends JpaRepository<BookingRecord, Long> {
	
}
