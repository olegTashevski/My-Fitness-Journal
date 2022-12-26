package mk.codecademy.tashevski.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import mk.codecademy.tashevski.java.dto.RequestPost;
import mk.codecademy.tashevski.java.dto.WeightlifterUsername;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_MYPAGE;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_FRIEND;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_NOTFRIEND;
@ExtendWith(MockitoExtension.class)
class WeightLifterGetServiceTests {
	
	private WeightLifterGetService getService;
	@Mock
	private WeightlifterRepo weightlifterRepo;

	@BeforeEach
	void setUp() throws Exception {
		getService = new WeightLifterGetService(weightlifterRepo, null, null, null, null);
	}

	@Test
	void testSetModelForHomePageWhenTypeEqualsAUTHORI_TYPE_MYPAGE() {
		String username = "testUsername";
		Weightlifter weightlifter = Weightlifter.builder()
				.username(username).build();
		Model model = new  ExtendedModelMap();
		when(weightlifterRepo.findById(username))
			.thenReturn(Optional.of(weightlifter));
		Map<String, Object> modelMapComperator = new HashMap<>();
		modelMapComperator.put("weightlifterUsername", new WeightlifterUsername(username));
		modelMapComperator.put("weightlifter", weightlifter);
		modelMapComperator.put(AUTHORI_TYPE_MYPAGE, true);
		modelMapComperator.put(AUTHORI_TYPE_FRIEND, false);
		modelMapComperator.put(AUTHORI_TYPE_NOTFRIEND, false);
		modelMapComperator.put("postRequest", RequestPost.builder().weightlifterUsername(username)
				.hasImages("true")
				.build());
		
		getService.setModelForHomePage(username, model, AUTHORI_TYPE_MYPAGE);
		
		assertThat(model.asMap())
			.containsAllEntriesOf(modelMapComperator);
		
		ArgumentCaptor<String> studentArgumentCaptor =
				ArgumentCaptor.forClass(String.class);
		
		verify(weightlifterRepo,times(1))
			.findById(studentArgumentCaptor.capture());
		
		assertThat(studentArgumentCaptor.getValue())
			.isEqualTo(username);
		
	}
	
	
	
	@Test
	void testSetModelForHomePageWhenTypeEqualsAUTHORI_TYPE_FRIEND() {
		String username = "testUsername";
		Weightlifter weightlifter = Weightlifter.builder()
				.username(username).build();
		Model model = new  ExtendedModelMap();
		when(weightlifterRepo.findById(username))
			.thenReturn(Optional.of(weightlifter));
		Map<String, Object> modelMapComperator = new HashMap<>();
		modelMapComperator.put("weightlifterUsername", new WeightlifterUsername(username));
		modelMapComperator.put("weightlifter", weightlifter);
		modelMapComperator.put(AUTHORI_TYPE_MYPAGE, false);
		modelMapComperator.put(AUTHORI_TYPE_FRIEND, true);
		modelMapComperator.put(AUTHORI_TYPE_NOTFRIEND, false);
		getService.setModelForHomePage(username, model, AUTHORI_TYPE_FRIEND);
		
		assertThat(model.asMap())
			.containsAllEntriesOf(modelMapComperator);
		
		ArgumentCaptor<String> studentArgumentCaptor =
				ArgumentCaptor.forClass(String.class);
		
		verify(weightlifterRepo,times(1))
			.findById(studentArgumentCaptor.capture());
		
		assertThat(studentArgumentCaptor.getValue())
			.isEqualTo(username);
		
	}
	
	@Test
	void testSetModelForHomePageWhenTypeEqualsAUTHORI_TYPE_NOTFRIEND() {
		String username = "testUsername";
		Weightlifter weightlifter = Weightlifter.builder()
				.username(username).build();
		Model model = new  ExtendedModelMap();
		when(weightlifterRepo.findById(username))
			.thenReturn(Optional.of(weightlifter));
		Map<String, Object> modelMapComperator = new HashMap<>();
		modelMapComperator.put("weightlifterUsername", new WeightlifterUsername(username));
		modelMapComperator.put("weightlifter", weightlifter);
		modelMapComperator.put(AUTHORI_TYPE_MYPAGE, false);
		modelMapComperator.put(AUTHORI_TYPE_FRIEND, false);
		modelMapComperator.put(AUTHORI_TYPE_NOTFRIEND, true);
		getService.setModelForHomePage(username, model, AUTHORI_TYPE_NOTFRIEND);
		
		assertThat(model.asMap())
			.containsAllEntriesOf(modelMapComperator);
		
		ArgumentCaptor<String> studentArgumentCaptor =
				ArgumentCaptor.forClass(String.class);
		
		verify(weightlifterRepo,times(1))
			.findById(studentArgumentCaptor.capture());
		
		assertThat(studentArgumentCaptor.getValue())
			.isEqualTo(username);
		
	}
	
	@Test
	void testSetModelForHomePageWhenWeighlifterDoesNotExist() {
		final String anyString = "anyString";
	
		
		when(weightlifterRepo.findById(anyString()))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> getService.setModelForHomePage(anyString, null, null))
			.isInstanceOf(NoSuchElementException.class);
		
		ArgumentCaptor<String> studentArgumentCaptor =
				ArgumentCaptor.forClass(String.class);
		
		verify(weightlifterRepo,times(1))
			.findById(studentArgumentCaptor.capture());
		
		assertThat(studentArgumentCaptor.getValue())
			.isEqualTo(anyString);
		
	}


}
