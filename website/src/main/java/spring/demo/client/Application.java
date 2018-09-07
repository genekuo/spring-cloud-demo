package spring.demo.client;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class Application implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	/*RestTemplate searchClient = new RestTemplate();
	RestTemplate bookingClient = new RestTemplate();
	RestTemplate checkInClient = new RestTemplate();*/

	@Autowired
	RestTemplate restClient;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		// Search for a flight
		SearchQuery searchQuery = new SearchQuery("NYC", "SFO", "22-JAN-18");
		//Flight[] flights = searchClient.postForObject("http://192.168.60.32:8090/search/get", searchQuery, Flight[].class);

		//Flight[] flights = restClient.postForObject("http://search-service/search/get", searchQuery, Flight[].class);
		Flight[] flights = restClient.postForObject("http://search-apigateway/api/search/get", searchQuery, Flight[].class);

		Arrays.asList(flights).forEach(flight -> logger.info(" flight >" + flight));

		// create a booking only if there are flights.
		if (flights == null || flights.length == 0) {
			return;
		}
		Flight flight = flights[0];
		BookingRecord booking = new BookingRecord(flight.getFlightNumber(), flight.getOrigin(), flight.getDestination(),
				flight.getFlightDate(), null, flight.getFares().getFare());
		Set<Passenger> passengers = new HashSet<Passenger>();
		passengers.add(new Passenger("Gavin", "Franc", "Male", booking));
		booking.setPassengers(passengers);
		long bookingId = 0;
		try {
			//bookingId = bookingClient.postForObject("http://192.168.60.32:8060/booking/create", booking, long.class);
			// Use service endpoint
			//bookingId = restClient.postForObject("http://book-service/booking/create", booking, long.class);
			// Use api gateway
			bookingId = restClient.postForObject("http://book-apigateway/api/booking/create", booking, long.class);
			
			logger.info("Booking created " + bookingId);
		} catch (Exception e) {
			logger.error("BOOKING SERVICE NOT AVAILABLE...!!!");
		}

		// check in passenger
		if (bookingId == 0)
			return;
		try {
			CheckInRecord checkIn = new CheckInRecord("Franc", "Gavin", "28C", null, "BF101", "22-JAN-18", bookingId);
			//long checkinId = checkInClient.postForObject("http://192.168.60.32:8070/checkin/create", checkIn, long.class);
			// Use service endpoint
			//long checkinId = restClient.postForObject("http://checkin-service/checkin/create", checkIn, long.class);
			// Use api gateway
			long checkinId = restClient.postForObject("http://checkin-apigateway/api/checkin/create", checkIn, long.class);
			
			logger.info("Checked IN " + checkinId);
		} catch (Exception e) {
			logger.error("CHECK IN SERVICE NOT AVAILABLE...!!!");
		}
	}

}

@Configuration
class MyConfiguration {

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}