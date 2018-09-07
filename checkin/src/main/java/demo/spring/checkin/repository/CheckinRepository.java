package demo.spring.checkin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.spring.checkin.entity.CheckInRecord;

public interface CheckinRepository extends JpaRepository<CheckInRecord,Long> {

}
