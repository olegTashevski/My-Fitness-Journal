package mk.codecademy.tashevski.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.dto.FriendRequestGet;
import mk.codecademy.tashevski.java.dto.ResolvementOfFriendRequest;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.FriendRequest;
import mk.codecademy.tashevski.java.repository.FriendRequestRepo;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
	

	@Getter
	private final FriendRequestRepo friendRequestRepo;
	
	@Getter
	private final WeightlifterPutService weightlifterPutService;
	
	

	public void sendRequest(String fromUser, String toUser) {
		friendRequestRepo.save(new FriendRequest(null, fromUser, toUser));
	}

	public String checkRequest(String fromUser, String toUser) {
		String response = "false";
		Optional<FriendRequest> request = friendRequestRepo.findByFromUserAndToUser(fromUser,toUser);
		if(request.isPresent()) {
			response="true";
		}
		return response;
	}

	public List<FriendRequestGet> getUserRequests(String username) {
		
		return friendRequestRepo.getAllRequestForUser(username);
	}

	public void resolveRequest(ResolvementOfFriendRequest resolvement) {
		
		
		
		final String fromUsername = resolvement.getFrom();
		final String toUsername = resolvement.getTo();
		
		if(friendRequestRepo.findByFromUserAndToUser(fromUsername, toUsername).isEmpty()) {
			throw new IlegalAccessApiException();
		}
		if(resolvement.isAccepted()) {
			
			weightlifterPutService.addFriend(fromUsername, toUsername);
		}
		
		friendRequestRepo.deleteByFromUserAndToUser(fromUsername,toUsername);
		
	}
	
}
