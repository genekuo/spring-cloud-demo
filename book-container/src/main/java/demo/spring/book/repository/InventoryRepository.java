package demo.spring.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.spring.book.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Inventory findByFlightNumberAndFlightDate(String flightNumber, String flightDate);

}
