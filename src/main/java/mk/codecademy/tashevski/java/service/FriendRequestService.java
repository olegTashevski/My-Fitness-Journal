package mk.codecademy.tashevski.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mk.codecademy.tashevski.java.dto.FriendRequestGet;
import mk.codecademy.tashevski.java.dto.ResolvementOfFriendRequest;
import mk.codecademy.tashevski.java.model.FriendRequest;
import mk.codecademy.tashevski.java.repository.FriendRequestRepo;

@Service
public class FriendRequestService {

	@Autowired
	private FriendRequestRepo friendRequestRepo;
	
	@Autowired
	private WeightlifterPutService weightlifterPutService;

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
		// TODO Auto-generated method stub
		return friendRequestRepo.getAllRequestForUser(username);
	}

	public void resolveRequest(ResolvementOfFriendRequest resolvement) {
		System.out.println(resolvement);
		if(resolvement.isAccepted()) {
			System.out.println("ITs true");
			weightlifterPutService.addFriend(resolvement.getFrom(), resolvement.getTo());
		}
		friendRequestRepo.deleteByFromUserAndToUser(resolvement.getFrom(),resolvement.getTo());
		
	}
	
}
