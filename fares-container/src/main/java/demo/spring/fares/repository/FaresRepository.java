package demo.spring.fares.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.spring.fares.entity.Fare;

public interface FaresRepository extends JpaRepository<Fare, Long> {
	Fare getFareByFlightNumberAndFlightDate(String flightNumber, String flightDate);
}
