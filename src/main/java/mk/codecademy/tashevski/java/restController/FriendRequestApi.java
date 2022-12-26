package mk.codecademy.tashevski.java.restController;

import static mk.codecademy.tashevski.java.Constants.MAIN_USER_ATT_NAME;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.dto.FriendRequestGet;
import mk.codecademy.tashevski.java.dto.ResolvementOfFriendRequest;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.service.FriendRequestService;
@RestController
@RequestMapping("/friendRequest/")
@RequiredArgsConstructor
public class FriendRequestApi {
	
	@Getter
	private final FriendRequestService friendRequestService;
	
	@PostMapping("/sendRequest")
	public String sendRequest(@RequestParam String fromUser,@RequestParam String toUser) {
		friendRequestService.sendRequest(fromUser,toUser);
		return "Successfully send request";
	}
	@GetMapping("/checkRequest")
	public String checkRequest(@RequestParam  String fromUser,@RequestParam String toUser) {
		
		return friendRequestService.checkRequest(fromUser,toUser);
	}
	
	@GetMapping("/getUserRequests")
	public List<FriendRequestGet> getUserRequests(@RequestParam String username){
		
		return friendRequestService.getUserRequests(username);
	}
	
	@PostMapping("/resolveRequest")
	public String resolveRequest(@RequestBody ResolvementOfFriendRequest resolvement,HttpServletRequest request) {
		String mainUser =(String) request.getAttribute(MAIN_USER_ATT_NAME);
		if(! mainUser.equals(resolvement.getTo())) {
			throw new IlegalAccessApiException();
		}
		friendRequestService.resolveRequest(resolvement);
		return "request resolved";
	}

}
