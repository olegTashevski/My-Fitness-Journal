package mk.codecademy.tashevski.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mk.codecademy.tashevski.java.model.FriendRequest;
import mk.codecademy.tashevski.java.repository.FriendRequestRepo;
@ExtendWith(MockitoExtension.class)
class FriendRequestServiceTests {
	@Mock
	private FriendRequestRepo friendRequestRepo;
	
	private FriendRequestService friendRequestService;

	@BeforeEach
	void setUp() throws Exception {
		friendRequestService = new FriendRequestService(friendRequestRepo, null);
	}

	@Test
	void testCheckRequestIfPresent() {
		 String fromUserTest = "fromUserTest";
		 String toUserTest = "toUserTest";
		 String result = "true";
		 FriendRequest friendRequest = new FriendRequest(null, fromUserTest, toUserTest);
		 
		when(friendRequestRepo.findByFromUserAndToUser(fromUserTest, toUserTest))
			.thenReturn(Optional.of(friendRequest));
		assertThat(friendRequestService.checkRequest(fromUserTest, toUserTest))
			.isEqualTo(result);
		
		ArgumentCaptor<String> argumentCaptor =
				ArgumentCaptor.forClass(String.class);
		verify(friendRequestRepo,times(1))
		.findByFromUserAndToUser(argumentCaptor.capture(),argumentCaptor.capture());
		
		assertThat(argumentCaptor.getAllValues())
			.allMatch(username->username.equals(fromUserTest)||username.equals(toUserTest));
		
	}
	
	
	void testCheckRequestIfEmpty() {
		 String fromUserTest = "fromUserTest";
		 String toUserTest = "toUserTest";
		 String result = "false";
		 
		when(friendRequestRepo.findByFromUserAndToUser(fromUserTest, toUserTest))
			.thenReturn(Optional.empty());
		assertThat(friendRequestService.checkRequest(fromUserTest, toUserTest))
			.isEqualTo(result);
		
		ArgumentCaptor<String> argumentCaptor =
				ArgumentCaptor.forClass(String.class);
		verify(friendRequestRepo,times(1))
		.findByFromUserAndToUser(argumentCaptor.capture(),argumentCaptor.capture());
		
		assertThat(argumentCaptor.getAllValues())
			.allMatch(username->username.equals(fromUserTest)||username.equals(toUserTest));
		
	}

}
