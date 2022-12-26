package mk.codecademy.tashevski.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mk.codecademy.tashevski.java.model.BothUsernames;
import mk.codecademy.tashevski.java.model.Rating;
import mk.codecademy.tashevski.java.model.SingleRating;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.SingleRatingRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
@ExtendWith(MockitoExtension.class)
class WeightlifterPutServiceTests {
	
	@Mock
	private WeightlifterRepo weightlifterRepo;
	
	@Mock
	private SingleRatingRepo singleRatingRepo;
	
	private WeightlifterPutService testService;

	@BeforeEach
	void setUp() throws Exception {
		testService = new WeightlifterPutService(weightlifterRepo, singleRatingRepo);
	}

	@Test
	void testGiveRatingWhenFrienUsernameIsValidAndRatingIsNullAndSingleRatingOptinalIsEmpty() {
		final String mainUser = "mainUserTest";
		final String friendUser = "friendUsernameTest";
		final Weightlifter weightlifter = Weightlifter.builder()
				.username(friendUser).build();
		final int newTestRating = 3;
		//expectedReturnValue value is equal to newTestRating because weightlifter does not have a rating yet
		final float expectedReturnValue = newTestRating;
		when(weightlifterRepo.findById(friendUser))
				.thenReturn(Optional.of(weightlifter));
		
		when(singleRatingRepo.getSingleRatingFromMainUserToFriend(mainUser, friendUser))
					.thenReturn(Optional.empty());
		
		assertThat(testService.giveRating(mainUser, friendUser, newTestRating))
				.isEqualTo(expectedReturnValue);
		
		verify(weightlifterRepo,times(1))
			.findById(friendUser);
		
		verify(singleRatingRepo, times(1))
			.getSingleRatingFromMainUserToFriend(mainUser, friendUser); 
		
		verify(weightlifterRepo,times(1))
			.save(weightlifter);
		
		
	}
	
	@Test
	void testGiveRatingWhenFrienUsernameIsValidAndRatingAllreadyExistsAndSingleRatingIsPresent() {
		final String mainUser = "mainUserTest";
		final String friendUser = "friendUsernameTest";
		final int oldRSingleRatingNumber = 4;
		final long numberOfRatins = (long)2;
		final int otherRatingNumber = 5;
		SingleRating singleRating = SingleRating.builder()
				.bothUsernames(new BothUsernames(mainUser, friendUser))
				.rating(oldRSingleRatingNumber)
				.build();
		
		SingleRating otherRating = SingleRating.builder()
				.rating(otherRatingNumber)
				.build();
		
		final Set<SingleRating> singleRatings = new HashSet<>(Arrays.asList(singleRating,otherRating));
		Rating rating = Rating.builder()
				.numberOfRatins(numberOfRatins)
				.singleRatings(singleRatings)
				.build();
		
		final Weightlifter weightlifter = Weightlifter.builder()
				.rating(rating)
				.username(friendUser).build();
		
		final int newTestRating = 3;
		//expectedReturnValue value is equal to newTestRating(because it over writes the oldRSingleRatingNumber rating of mainUsername to friend ) + otherRatingNumber / 2 -the number of total single ratings
		final float expectedReturnValue = (newTestRating + otherRatingNumber) / numberOfRatins;
		when(weightlifterRepo.findById(friendUser))
				.thenReturn(Optional.of(weightlifter));
		
		when(singleRatingRepo.getSingleRatingFromMainUserToFriend(mainUser, friendUser))
					.thenReturn(Optional.of(singleRating));
		
		assertThat(testService.giveRating(mainUser, friendUser, newTestRating))
				.isEqualTo(expectedReturnValue);
		
		verify(weightlifterRepo,times(1))
			.findById(friendUser);
		
		verify(singleRatingRepo, times(1))
			.getSingleRatingFromMainUserToFriend(mainUser, friendUser); 
		
		verify(weightlifterRepo,times(1))
			.save(weightlifter);
		
		
	}
	
	@Test
	void testGiveRatingWhenFrienUsernameInNotValid() {
		String anyMainUsername = "anyUsername";
		String friendUsername = "friendUsername";
		int anyRating = 1;
		when(weightlifterRepo.findById(friendUsername))
		.thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> testService.giveRating(anyMainUsername, friendUsername, anyRating))
			.isInstanceOf(NoSuchElementException.class);
		
		verify(weightlifterRepo,times(1))
		.findById(friendUsername);
	
		verify(singleRatingRepo, never())
		.getSingleRatingFromMainUserToFriend(any(), any()); 
	
		verify(weightlifterRepo,never())
		.save(any());
	
		
	}
	
	
	
	

}
