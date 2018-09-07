package demo.spring.book.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import demo.spring.book.entity.BookingRecord;

public interface BookingRepository extends JpaRepository<BookingRecord, Long> {
	
}
