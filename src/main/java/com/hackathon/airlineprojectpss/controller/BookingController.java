package com.hackathon.airlineprojectpss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.airlineprojectpss.entity.BookingRecord;
import com.hackathon.airlineprojectpss.service.BookingService;


@RestController
@CrossOrigin

public class BookingController {

	@Autowired
    BookingService bookingService;
	
	
	BookingController(BookingService bookingService){
		this.bookingService = bookingService;
	}

	@RequestMapping(value="/create" , method = RequestMethod.POST)
	//@PostMapping("/booking/create")
	long book(@RequestBody BookingRecord record){
		System.out.println("Booking Request" + record); 
		return bookingService.book(record);
	}
	
	@GetMapping("/booking/get/{id}")
	BookingRecord getBooking(@PathVariable long id){
		return bookingService.getBooking(id);

}
}
