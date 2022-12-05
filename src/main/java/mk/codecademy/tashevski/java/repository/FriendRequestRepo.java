package mk.codecademy.tashevski.java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mk.codecademy.tashevski.java.dto.FriendRequestGet;
import mk.codecademy.tashevski.java.model.FriendRequest;

public interface FriendRequestRepo extends JpaRepository<FriendRequest, Long> {

	Optional<FriendRequest> findByFromUserAndToUser(String fromUser, String toUser);

	@Transactional(readOnly = true)
	@Query("SELECT new mk.codecademy.tashevski.java.dto.FriendRequestGet(req.toUser,req.fromUser) FROM FriendRequest req WHERE req.toUser=:username")
	List<FriendRequestGet> getAllRequestForUser(@Param("username") String username);

	@Transactional
	void deleteByFromUserAndToUser(String from, String to);

}
