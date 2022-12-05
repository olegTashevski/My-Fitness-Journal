package mk.codecademy.tashevski.java.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;
import mk.codecademy.tashevski.java.exceptions.FileNotImageException;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.Photo;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.PhotoRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;



@Service
public class ExtrasService {
	@Autowired
	private WeightlifterRepo weightlifterRepo;
	
	@Autowired
	private PhotoRepo photoRepo;
	
	byte[] compress(MultipartFile photo,String username) {
		byte[] fileContent=null;
		try (InputStream input = photo.getInputStream();ByteArrayOutputStream stream = new ByteArrayOutputStream();
				DeflaterOutputStream compressor = new DeflaterOutputStream(stream)) {
		    
		        ImageIO.read(input).toString();
				compressor.write(photo.getBytes());
				compressor.finish();
				fileContent =  stream.toByteArray();
			}
		catch (Exception e) {
			throw new FileNotImageException("The specified file IS NOT AN IMAGE",username);
		}
			return fileContent;
		
		
	}
	
	@SneakyThrows
	public byte[] decompress(byte[] image) {
		ByteArrayInputStream stream = new ByteArrayInputStream(image);
		InflaterInputStream decompressor = new InflaterInputStream(stream);
		byte[] decompressedData;
		
			 decompressedData = decompressor.readAllBytes();
	return 	decompressedData;	
	}

	public byte[] getProfileImage(String weightlifterId) {
		byte[] image = weightlifterRepo.findById(weightlifterId).get().getProfilePic();
		return decompress(image);
	}

	public byte[] getPostImage(Long photoId , String mainUser) {
		Photo photo = photoRepo.findById(photoId).get();
		String username = photo.getPost().getWeightlifter().getUsername();
		if(!mainUser.equals(username)) {
			Optional<Weightlifter> weigtlifter = weightlifterRepo.findFriend(mainUser, username);
			if(weigtlifter.isEmpty()) {
				throw new IlegalAccessApiException();
			}
		}

		return decompress(photoRepo.findById(photoId).get().getContent());
	}

	
	

}
