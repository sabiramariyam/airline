package com.hackathon.airlineprojectpss.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackathon.airlineprojectpss.entity.BookingRecord;
import com.hackathon.airlineprojectpss.entity.Inventory;
import com.hackathon.airlineprojectpss.entity.Passengers;
import com.hackathon.airlineprojectpss.exception.BookingException;
import com.hackathon.airlineprojectpss.repository.BookingRepository;
import com.hackathon.airlineprojectpss.repository.InventoryRepository;
import org.slf4j.LoggerFactory;

@Service
public class BookingService {
	
	@Autowired
	BookingRepository bookingRepository;
	InventoryRepository inventoryRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
	
	
	public BookingService (BookingRepository bookingRepository,
			  InventoryRepository inventoryRepository){
			  this.bookingRepository = bookingRepository;
			  this.inventoryRepository = inventoryRepository;
			  

	}
	
	

	public long book(BookingRecord record) {
		
		logger.info("calling fares to get fare");
		
		logger.info("calling inventory to get inventory");
		
		Inventory inventory = inventoryRepository.findByFlightNumberAndFlightDate(record.getFlightNumber(),record.getFlightDate());
		if(!inventory.isAvailable(record.getPassengers().size())){
			throw new BookingException("No more seats avaialble");
		}
		
		logger.info("successfully checked inventory" + inventory);
		logger.info("calling inventory to update inventory");
		
		inventory.setAvailable(inventory.getAvailable() - record.getPassengers().size());
		inventoryRepository.saveAndFlush(inventory);
		logger.info("sucessfully updated inventory");
		
		record.setStatus(BookingStatus.BOOKING_CONFIRMED); 
		Set<Passengers> passengers = record.getPassengers();
		passengers.forEach(passenger -> passenger.setBookingRecord(record));
		record.setBookingDate(new Date());
		long id=  bookingRepository.save(record).getId();
		logger.info("Successfully saved booking");
		
		logger.info("sending a booking event");
		Map<String, Object> bookingDetails = new HashMap<String, Object>();
		bookingDetails.put("FLIGHT_NUMBER", record.getFlightNumber());
		bookingDetails.put("FLIGHT_DATE", record.getFlightDate());
		bookingDetails.put("NEW_INVENTORY", inventory.getBookableInventory());
		logger.info("booking event successfully delivered "+ bookingDetails);
	
		return id;
	}

	public BookingRecord getBooking(long id) {
		return bookingRepository.findById(new Long(id)).get();
	}
	
	public void updateStatus(String status, long bookingId) {
		BookingRecord record = bookingRepository.findById(new Long(bookingId)).get();
		record.setStatus(status);
	}
	
	private void checkFare(String requestedFare, String actualfare){
		logger.info("calling fares to get fare (reactively collected) "+ actualfare);
		if (!requestedFare.equals(actualfare))
			throw new BookingException("fare is tampered");
	}


	
}
