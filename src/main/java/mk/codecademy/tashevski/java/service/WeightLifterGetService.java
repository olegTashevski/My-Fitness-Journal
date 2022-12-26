package mk.codecademy.tashevski.java.service;

import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_FRIEND;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_MYPAGE;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_NOTFRIEND;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.dto.PostGet;
import mk.codecademy.tashevski.java.dto.RequestPost;
import mk.codecademy.tashevski.java.dto.WeightlifterUsername;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessException;
import mk.codecademy.tashevski.java.model.Day;
import mk.codecademy.tashevski.java.model.Photo;
import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.model.SingleRating;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.PhotoRepo;
import mk.codecademy.tashevski.java.repository.PostRepo;
import mk.codecademy.tashevski.java.repository.SingleRatingRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
@Service
@RequiredArgsConstructor
public class WeightLifterGetService {
	
	
	
	private final WeightlifterRepo weightlifterRepo;
	private final PostRepo postRepo;
	
	
	private final  ExtrasService extrasService;
	
	
	private final PhotoRepo photoRepo;
	
	private final  SingleRatingRepo singleRatingRepo; 
	


	

	public Slice<Post> getPosts(String username,String mainUser,int pageIndex) {
		if(!mainUser.equals(username) && weightlifterRepo.findFriend(mainUser, username).isEmpty()) {
				throw new IlegalAccessApiException();
		}
		Pageable pageable = PageRequest.of(pageIndex,5,Sort.by(Direction.DESC, "timeStamp"));
		return postRepo.getPosts(username,pageable);
		}

	

	public Slice<String> getWeighliftersUsernamesForPattern(String pattern) {
		return  weightlifterRepo.selectUsernamesFromWeightlifters(pattern+"%",PageRequest.of(0, 4));
		
		 
	}

	public void setModelForHomePage(String weightlifter, Model model,String type) {
		Optional<Weightlifter> weightlifterOpt = weightlifterRepo.findById(weightlifter);
		
		model.addAttribute("weightlifterUsername",new WeightlifterUsername(weightlifter));
		model.addAttribute("weightlifter", weightlifterOpt.get());
		
		
		if(type.equals(AUTHORI_TYPE_MYPAGE)) {
			model.addAttribute(AUTHORI_TYPE_MYPAGE, true);
			model.addAttribute(AUTHORI_TYPE_FRIEND, false);
			model.addAttribute(AUTHORI_TYPE_NOTFRIEND, false);
		model.addAttribute("postRequest",RequestPost.builder().weightlifterUsername(weightlifter)
				.hasImages("true")
				.build());
		}
		else if(type.equals(AUTHORI_TYPE_FRIEND)){
				model.addAttribute(AUTHORI_TYPE_MYPAGE, false);
				model.addAttribute(AUTHORI_TYPE_FRIEND, true);
				model.addAttribute(AUTHORI_TYPE_NOTFRIEND, false);
		}
			
		else  {
				model.addAttribute(AUTHORI_TYPE_MYPAGE, false);
				model.addAttribute(AUTHORI_TYPE_FRIEND, false);
				model.addAttribute(AUTHORI_TYPE_NOTFRIEND, true);
			}
		}
		
	
	
	
	

	public void setMounthlyShedule(String username, Model model,String type) {
		if(type.equals(AUTHORI_TYPE_MYPAGE)) {
			model.addAttribute(AUTHORI_TYPE_MYPAGE, true);
		}
		model.addAttribute("username", username);
		final Set<Day> days = weightlifterRepo.findById(username).orElseThrow().getMonthlySchedule().getDays();
		final List<Day> daysList = new ArrayList<>();
		while (true) {
			Day day = checkLatestDayThenRemoveIt(days);
			daysList.add(day);
			if(days.isEmpty()) {
				break;
			}
		}
		
		model.addAttribute("daysOfShedule", daysList);
		
	}

	

	

	private Day checkLatestDayThenRemoveIt(Set<Day> days) {
		Day dayResult = null;
		for (Day day : days) {
			boolean latest = days.stream().allMatch(
					dayStream->
							day.getDate().isAfter(
							dayStream.getDate()
							)
							||
							day.equals(dayStream)
					);
			if(latest) {
				dayResult = day;
				break;
			}
		}
		days.remove(dayResult);
		return dayResult;
	}



	public PostGet getPost(Long id,String mainUser) {
		Post post = postRepo.getPostWithPhotos(id).orElseThrow();
		if(!mainUser.equals(post.getWeightlifter().getUsername())) {
			throw new IlegalAccessApiException();
		}
		Set<Long> images = post.getPhotos().stream()
				.map(Photo::getId)
				.collect(Collectors.toSet());
		
		return new PostGet(post.getId(), post.getHead(), post.getDescription(), images) ;
	}
	
	

	public void setModelForHomePageAfterPost(RequestPost requestPost,String mainUser) {
		String username = requestPost.getWeightlifterUsername();
		if(!mainUser.equals(username)) {
			throw new IlegalAccessException(username);
		}
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		Post post = new Post(
				null
				, requestPost.getHead()
				, requestPost.getDescription()
				,Timestamp.valueOf(LocalDateTime.now())
				, null
				, weightlifter);
		Set<Post> posts = weightlifter.getPosts();
		posts.add(post);
		Set<Photo> imagesPost = new HashSet<>();
		
		if(requestPost.getHasImages().equalsIgnoreCase("true")) {
		for (MultipartFile imageTemp : requestPost.getImages()) {
			byte[] imageContent = extrasService.compress(imageTemp,username);
			Photo photo = new Photo(null, post, imageContent);
			photo=photoRepo.save(photo);
			imagesPost.add(photo);
		}
		post.setPhotos(imagesPost);
		}
		weightlifterRepo.save(weightlifter);
		
		
	}



	public String getRating(String username, String friend) {
		String response = "noRating";
		Optional<SingleRating> ratingOp = singleRatingRepo.getSingleRatingFromMainUserToFriend(username, friend);
		if(ratingOp.isPresent()) {
			response =""+ ratingOp.get().getRating();
		}
		
		return response;
	}

	

	
	

	

}
