package mk.codecademy.tashevski.java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mk.codecademy.tashevski.java.model.SingleRating;
@Transactional(readOnly = true)
public interface SingleRatingRepo extends JpaRepository<SingleRating, String> {

	
	@Query("SELECT singleR FROM SingleRating singleR JOIN FETCH singleR.weightlifterRating"
			+ " fullRating WHERE singleR.bothUsernames.fromUsername = :main AND fullRating.username = :friend")
	Optional<SingleRating> getSingleRatingFromMainUserToFriend(@Param("main") String mainUsername,@Param("friend") String friendUsername);
	
	
	

}
