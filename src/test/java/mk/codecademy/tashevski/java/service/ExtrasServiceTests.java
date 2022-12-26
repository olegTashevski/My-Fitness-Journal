package mk.codecademy.tashevski.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.Photo;
import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.PhotoRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
@ExtendWith(MockitoExtension.class)
class ExtrasServiceTests {
	
	private ExtrasService extrasServiceTest;
	@Mock
	private PhotoRepo photoRepo;
	
	@Mock
	private WeightlifterRepo weightlifterRepo;

	@BeforeEach
	void setUp() {
		extrasServiceTest = new ExtrasService(weightlifterRepo,photoRepo);
	}
	
	@Test
	void testGetPostImageWhenPhotoIsPresentAndUsernameIsEqualToMainUser() throws IOException {
		final String username = "testUsername";
		final Long photoId = (long)1;
		Weightlifter weightlifter = Weightlifter.builder()
				.username(username).build();
		
		Post post = Post.builder()
				.weightlifter(weightlifter).build();
		
		//change the path to a file in your pc
		File file = new File("C:\\Users\\User\\OneDrive - Institut za Akreditacija na RM\\Pictures\\35.jpg");
		
		FileInputStream fileInputStream = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile("photo", fileInputStream);
		byte[] compressedImageContent = extrasServiceTest.compress(multipartFile, username);
		Photo photo = new Photo(photoId, post,compressedImageContent);
		Optional<Photo> postO = Optional.of(photo);
		
		when(photoRepo.findById(photoId))
			.thenReturn(postO);
		
		assertThat(extrasServiceTest.getPostImage(photoId, username))
			.isEqualTo(extrasServiceTest.decompress(compressedImageContent));
		
		ArgumentCaptor<Long> studentArgumentCaptor =
				ArgumentCaptor.forClass(Long.class);
		
		verify(photoRepo,times(1)).findById(studentArgumentCaptor.capture());
		
		assertThat(studentArgumentCaptor.getValue())
			.isEqualTo(photoId);
	}
	
	@Test
	void testGetPostImageWhenPhotoIsEmpty() {
		final Long anyphotoId = (long)1;
		when(photoRepo.findById(anyLong()))
			.thenReturn(Optional.empty());
		assertThatThrownBy(() -> extrasServiceTest.getPostImage(anyphotoId, null))
			.isInstanceOf(NoSuchElementException.class);
		verify(photoRepo,times(1)).findById(anyphotoId);
		
	}
	
	@Test
	void testGetPostImageWhenMainUsernameIsNotEqualToUsernameAndMainUserIsNotAFriendOfUsername() {
		final String username = "testUsername";
		final String otherMainUser = "mainTestUsername";
		final Long photoId = (long)1;
		Weightlifter weightlifter = Weightlifter.builder()
				.username(username).build();
		
		Post post = Post.builder()
				.weightlifter(weightlifter).build();
		
		Photo photo = new Photo(photoId, post,null);
		
		Optional<Photo> postO = Optional.of(photo);
		
		when(photoRepo.findById(photoId))
			.thenReturn(postO);
		
		when(weightlifterRepo.findFriend(otherMainUser, username))
			.thenReturn(Optional.empty());
		
		
		assertThatThrownBy(() -> extrasServiceTest.getPostImage(photoId, otherMainUser))
		.isInstanceOf(IlegalAccessApiException.class);
		
		ArgumentCaptor<Long> argumentCaptor =
				ArgumentCaptor.forClass(Long.class);
		
		verify(photoRepo,times(1)).findById(argumentCaptor.capture());
		verify(weightlifterRepo,times(1)).findFriend(otherMainUser, username);
		
		assertThat(argumentCaptor.getValue())
			.isEqualTo(photoId);
		
		
		
		
		
	}
	
	
}
