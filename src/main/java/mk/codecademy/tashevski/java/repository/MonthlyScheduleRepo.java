package mk.codecademy.tashevski.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mk.codecademy.tashevski.java.model.MonthlySchedule;
@Repository
public interface MonthlyScheduleRepo extends JpaRepository<MonthlySchedule, String> {

}
