package mk.codecademy.tashevski.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mk.codecademy.tashevski.java.model.WORKOUT;
@Repository
public interface WorkoutRepo extends JpaRepository<WORKOUT,Long > {
	
}
