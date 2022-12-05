package mk.codecademy.tashevski.java.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mk.codecademy.tashevski.java.dto.PostGet;
import mk.codecademy.tashevski.java.model.Weightlifter;
@Repository
public interface WeightlifterRepo extends JpaRepository<Weightlifter, String> {
	@Transactional(readOnly = true)
	@Query("SELECT we.username from Weightlifter we where we.username like :pattern")
	Slice<String> selectUsernamesFromWeightlifters(@Param("pattern") String pattern,Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT friend FROM Weightlifter we JOIN we.myFriends friend WHERE we.username = :myU AND friend.username = :otherU")
	Optional<Weightlifter> findFriend(@Param("myU") String myUsername,@Param("otherU") String otherUsername);

	@Transactional(readOnly = true)
	@Query("SELECT we FROM Weightlifter we JOIN FETCH we.myFriends friends WHERE we.username = :username")
	Weightlifter getWeightlifterWithFriends(@Param("username") String  username);

	@Transactional(readOnly = true)
	@Query("SELECT we FROM Weightlifter we")
	Slice<Weightlifter> getAll(Pageable pageable);

	
}
