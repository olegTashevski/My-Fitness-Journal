package mk.codecademy.tashevski.java.service;

import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.exceptions.IlegalPersonalInformationChange;
import mk.codecademy.tashevski.java.model.BothUsernames;
import mk.codecademy.tashevski.java.model.Rating;
import mk.codecademy.tashevski.java.model.SingleRating;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.SingleRatingRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;

@Service
@RequiredArgsConstructor
public class WeightlifterPutService {

	
	private final WeightlifterRepo weightlifterRepo;
	private final SingleRatingRepo singleRatingRepo;

	public void changePassword(String username, String newPassword) {
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		String oldPassword = weightlifter.getPassword();
		if(newPassword.equals(newPassword)) {
			throw new IlegalPersonalInformationChange("New password cannot be the same as the old password");	
		}
		weightlifter.setPassword(newPassword);
		weightlifterRepo.save(weightlifter);
	}

	public void changeFirstName(String username, String newFirstName) {
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		String oldFirstName = weightlifter.getFirstName();
		if(newFirstName.equals(oldFirstName)) {
			throw new IlegalPersonalInformationChange("New first name cannot be the same as the old one");	
		}
		weightlifter.setFirstName(newFirstName);
		weightlifterRepo.save(weightlifter);
	}

	public void changeLastName(String username, String newLastName) {
		// TODO Auto-generated method stub
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		String oldLastName = weightlifter.getLastName();
		if(newLastName.equals(oldLastName)) {
			throw new IlegalPersonalInformationChange("New Last name cannot be the same as the old one");	
		}
		weightlifter.setLastName(newLastName);
		weightlifterRepo.save(weightlifter);
	}

	public void changeGender(String username, String newGender) {
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		String oldGender = weightlifter.getGender();
		if(newGender.equals(oldGender)) {
			throw new IlegalPersonalInformationChange("New Gender cannot be the same as the old one");	
		}
		weightlifter.setGender(newGender);
		weightlifterRepo.save(weightlifter);
		
	}

	public void changeDateOfBirth(String username, Date newDateOfBirth) {
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		Date oldDate = weightlifter.getDateOfBirth();
		if(newDateOfBirth.equals(oldDate)) {
			throw new IlegalPersonalInformationChange("New DateOfBirth cannot be the same as the old one");	
		}
		weightlifter.setDateOfBirth(newDateOfBirth);
		weightlifterRepo.save(weightlifter);
		
	}

	public void upadteBio(String username, String newBio) {
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		weightlifter.setBio(newBio);
		weightlifterRepo.save(weightlifter);
	}

	public void addFriend(String username1, String username2) {
		Weightlifter weightlifter1 = weightlifterRepo.findById(username1).orElseThrow();
		Weightlifter weightlifter2 = weightlifterRepo.findById(username2).orElseThrow();
		Set<Weightlifter> friends1 = weightlifter1.getMyFriends();
		friends1.add(weightlifter2);
		Set<Weightlifter> friends2 = weightlifter2.getMyFriends();
		friends2.add(weightlifter1);
		
		weightlifterRepo.save(weightlifter1);
		weightlifterRepo.save(weightlifter2);
	}

	public float giveRating(String mainUsername, String friendUsername, int newRating) {
		Weightlifter weightlifter = weightlifterRepo.findById(friendUsername).orElseThrow();
		Rating rating = weightlifter.getRating();
		if(rating==null) {
			rating= new Rating();
		}
		
		if(rating.getNumberOfRatins()==null) {
			rating.setNumberOfRatins((long)0);
		}
		
		long totalNumberOfRatings = rating.getNumberOfRatins();
		
		Optional<SingleRating> singleRatingOptional = 
				singleRatingRepo.getSingleRatingFromMainUserToFriend(mainUsername,friendUsername);
		SingleRating singleRating = singleRatingOptional.orElse(null);
		if(singleRating==null) {
			singleRating = SingleRating.builder()
					.bothUsernames(new BothUsernames(mainUsername, friendUsername))
				.weightlifterRating(rating).build();
			totalNumberOfRatings++;
			}
		singleRating.setRating(newRating);
		if(rating.getSingleRatings()==null) {
			rating.setSingleRatings(new HashSet<>());
			}
		rating.getSingleRatings().add(singleRating);
		rating.setNumberOfRatins(totalNumberOfRatings);
		float raitingFinal  = rating.getSingleRatings().stream()
				.map(rt->rt.getRating())
				.reduce(0, (a,b)->(a+b));
		raitingFinal = raitingFinal/totalNumberOfRatings;
		rating.setRating(raitingFinal);
		rating.setWeightlifter(weightlifter);
		weightlifter.setRating(rating);
		weightlifterRepo.save(weightlifter);
		
		return raitingFinal;
		
	}
	
	
	
}
