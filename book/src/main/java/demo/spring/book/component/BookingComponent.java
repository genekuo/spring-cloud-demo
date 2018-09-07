package demo.spring.book.component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
 
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import demo.spring.book.entity.BookingRecord;
import demo.spring.book.entity.Inventory;
import demo.spring.book.entity.Passenger;
import demo.spring.book.repository.BookingRepository;
import demo.spring.book.repository.InventoryRepository;

@EnableFeignClients
@RefreshScope
@Component
public class BookingComponent {
	private static final Logger logger = LoggerFactory.getLogger(BookingComponent.class);
   
	BookingRepository bookingRepository;
	InventoryRepository inventoryRepository;
	FareServiceProxy fareServiceProxy;
	
	// For conatiner
	//private static final String FareURL = "http://192.168.60.32:8080/fares";
	// For container
	//@Autowired
	//private RestTemplate restTemplate;
	
	Sender sender;

	public BookingComponent(){
		
	}
	
	
	@Autowired
	public BookingComponent (BookingRepository bookingRepository,
					  Sender sender,InventoryRepository inventoryRepository,FareServiceProxy fareServiceProxy){
		this.bookingRepository = bookingRepository;
 		this.sender = sender;
 		//this.restTemplate = new RestTemplate();
		this.inventoryRepository = inventoryRepository;
		this.fareServiceProxy = fareServiceProxy;
	}
	public long book(BookingRecord record) {
		logger.info("calling fares to get fare");
		// for Container
		//Fare fare = restTemplate.getForObject(FareURL +"/get?flightNumber="+record.getFlightNumber()+"&flightDate="+record.getFlightDate(),Fare.class);
		//call fares to get fare
		//Fare fare = restTemplate.getForObject(fareServiceUrl+FareURL +"/get?flightNumber="+record.getFlightNumber()+"&flightDate="+record.getFlightDate(),Fare.class);
		// Will not work with container
		Fare fare = fareServiceProxy.getFare(record.getFlightNumber(), record.getFlightDate());
		
		logger.info("calling fares to get fare "+ fare);
		//check fare
		if (!record.getFare().equals(fare.getFare()))
			throw new BookingException("fare is tampered");
		logger.info("calling inventory to get inventory");
		//check inventory
		Inventory inventory = inventoryRepository.findByFlightNumberAndFlightDate(record.getFlightNumber(),record.getFlightDate());
		if(!inventory.isAvailable(record.getPassengers().size())){
			throw new BookingException("No more seats avaialble");
		}
		logger.info("successfully checked inventory" + inventory);
		logger.info("calling inventory to update inventory");
		//update inventory
		inventory.setAvailable(inventory.getAvailable() - record.getPassengers().size());
		inventoryRepository.saveAndFlush(inventory);
		logger.info("sucessfully updated inventory");
		//save booking
		record.setStatus(BookingStatus.BOOKING_CONFIRMED); 
		Set<Passenger> passengers = record.getPassengers();
		passengers.forEach(passenger -> passenger.setBookingRecord(record));
		record.setBookingDate(new Date());
		long id=  bookingRepository.save(record).getId();
		logger.info("Successfully saved booking");
		//send a message to search to update inventory
		logger.info("sending a booking event");
		Map<String, Object> bookingDetails = new HashMap<String, Object>();
		bookingDetails.put("FLIGHT_NUMBER", record.getFlightNumber());
		bookingDetails.put("FLIGHT_DATE", record.getFlightDate());
		bookingDetails.put("NEW_INVENTORY", inventory.getBookableInventory());
		sender.send(bookingDetails);
		logger.info("booking event successfully delivered "+ bookingDetails);
		return id;
	}

	public BookingRecord getBooking(long id) {
		return bookingRepository.findOne(id);
	}
	
	
	public void updateStatus(String status, long bookingId) {
		BookingRecord record = bookingRepository.findOne(bookingId);
		if(record == null) {
			logger.info("NO BOOKING FOUND, ignoring FOR BOOKING ID.." + bookingId);
		}else { 
			record.setStatus(status);
		}
	}
	
}

