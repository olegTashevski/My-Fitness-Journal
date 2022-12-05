package mk.codecademy.tashevski.java.service;

import java.sql.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mk.codecademy.tashevski.java.exceptions.IlegalPersonalInformationChange;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;

@Service
public class WeightlifterPutService {

	@Autowired
	private WeightlifterRepo weightlifterRepo;

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
	
	
	
}
