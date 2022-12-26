package mk.codecademy.tashevski.java.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mk.codecademy.tashevski.java.model.Day;
@Repository
public interface DayRepo extends JpaRepository<Day,String>{

	@Transactional(readOnly = true)
	@Query("SELECT day FROM Day day LEFT JOIN FETCH day.supplements sup WHERE day.id=:id")
	Optional<Day> getDayForSupplementDeletion(@Param("id") String dayId);

	@Transactional(readOnly = true)
	@Query("SELECT day FROM Day day LEFT JOIN FETCH day.meals meals WHERE day.id=:id")
	Day getDayWithMeals(@Param("id") String idofDay);

	@Transactional(readOnly = true)
	@Query("SELECT day FROM Day day LEFT JOIN FETCH day.workouts workouts WHERE day.id=:id")
	Day getDayWithWorkouts(@Param("id") String idofDay);
	
	
	
}
