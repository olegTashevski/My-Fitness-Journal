package mk.codecademy.tashevski.java.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mk.codecademy.tashevski.java.model.Post;
@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

	
	@Query("SELECT post FROM Post post LEFT JOIN FETCH post.photos images WHERE post.id=:id")
	Optional<Post> getPostWithPhotos(@Param("id") Long id);

	@Query("SELECT posts FROM Post posts JOIN FETCH posts.weightlifter we WHERE we.username=:us")
	Slice<Post> getPosts(@Param("us")String username, Pageable pageable);

}
