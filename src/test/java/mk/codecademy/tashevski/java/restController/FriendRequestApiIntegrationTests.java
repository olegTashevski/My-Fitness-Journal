package mk.codecademy.tashevski.java.restController;

import static mk.codecademy.tashevski.java.Constants.MAIN_USER_ATT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import mk.codecademy.tashevski.java.dto.ResolvementOfFriendRequest;
import mk.codecademy.tashevski.java.model.FriendRequest;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.FriendRequestRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
import mk.codecademy.tashevski.java.service.FriendRequestService;
import mk.codecademy.tashevski.java.service.WeightlifterPutService;
@DataJpaTest
@ActiveProfiles("test")
class FriendRequestApiIntegrationTests {
	

	
	
	@TestConfiguration
	static class FriendRequestApiTestContextConfiguration {
		
		
		@Autowired
		private  FriendRequestRepo friendRequestRepo;
		
		@Autowired
		private WeightlifterRepo weightlifterRepo;
		
		@Bean
		FriendRequestApi friendRequestApi() {
			WeightlifterPutService putService = new WeightlifterPutService(weightlifterRepo, null);
			FriendRequestService friendRequestService =  new FriendRequestService(friendRequestRepo,putService);
			return new FriendRequestApi(friendRequestService);
		}
		
	}
	
	private    FriendRequestRepo friendRequestRepo;
	
	private   WeightlifterRepo weightlifterRepo;
	
	@Autowired
	private FriendRequestApi friendRequestApi;

	
	@BeforeEach
	void setUp() throws Exception {
		friendRequestRepo = friendRequestApi
				.getFriendRequestService()
				.getFriendRequestRepo();
		
		weightlifterRepo = friendRequestApi
				.getFriendRequestService()
				.getWeightlifterPutService()
				.getWeightlifterRepo();
		
		friendRequestRepo.deleteAll();
		
		weightlifterRepo.deleteAll();
		
		
	}

	@Test
	void testWhenFriendRequestExistUsernameIsValidAndIsAccepted() {
		String mainUser="mainTest";
		String otherUser = "otherTest";
		String resultExpected = "request resolved";
		long anyNumber = (long)2;
		ResolvementOfFriendRequest resolvementOfFriendRequest = new ResolvementOfFriendRequest(otherUser, mainUser, true);
		HttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(MAIN_USER_ATT_NAME, mainUser);
		
		Weightlifter weightlifterMain = Weightlifter.builder()
				.username(mainUser)
				.myFriends(new HashSet<>())
				.build();
		Weightlifter weightlifterOther = Weightlifter.builder()
				.username(otherUser)
				.myFriends(new HashSet<>())
				.build();
		
		FriendRequest friendRequest = new FriendRequest(anyNumber, otherUser, mainUser);
		friendRequestRepo.save(friendRequest);
		weightlifterRepo.save(weightlifterMain);
		weightlifterRepo.save(weightlifterOther);
		
		String result  = friendRequestApi.resolveRequest(resolvementOfFriendRequest, request);
		
		assertThat(result)
			.isEqualTo(resultExpected);
		
		weightlifterMain = weightlifterRepo.findById(mainUser).orElse(null); 
		
		assertThat(weightlifterMain)
			.isNotNull();
		assertTrue(weightlifterMain
				.getMyFriends()
				.stream()
				.anyMatch(we->we.getUsername().equals(otherUser)));
		
		weightlifterOther = weightlifterRepo.findById(otherUser).orElse(null); 
		
		assertThat(weightlifterOther)
			.isNotNull();
		assertTrue(weightlifterOther
				.getMyFriends()
				.stream()
				.anyMatch(we->we.getUsername().equals(mainUser)));
		
		friendRequest = friendRequestRepo.findByFromUserAndToUser(otherUser, mainUser).orElse(null);
		
		assertThat(friendRequest)
			.isNull();
		
	}

}
