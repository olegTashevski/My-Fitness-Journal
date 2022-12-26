package mk.codecademy.tashevski.java.restController;

import static mk.codecademy.tashevski.java.Constants.AUTHEN_COOKIE_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.PostRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class WeighlifterGetApiIntegrattionTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WeightlifterRepo repo;
	
	@Autowired
	private PostRepo postRepo;
	
	@BeforeEach
	void setUp() {
		repo.deleteAll();
	}

	@Test
	void testGetApiGetPostsWhenUsernameIsValid() throws Exception {
		String username = "testUsername";
		String mainUser = "mainUser";
		int indexPagable = 0;
		int expectedPostsSize = 3;
		String postHead = "test";
		Weightlifter weightlifter = Weightlifter.builder()
				.username(username)
				.build();
		
		Weightlifter mainWeightlifter = Weightlifter.builder()
				.username(mainUser)
				.myFriends(
						Set.of(weightlifter)
						)
				.build();
		weightlifter = repo.save(weightlifter);
		repo.save(mainWeightlifter);
		Set<Post> posts = new HashSet<>(
				Arrays.asList(
						postRepo.save(Post.builder().weightlifter(weightlifter).head(postHead+1).build())
						,postRepo.save(Post.builder().weightlifter(weightlifter).head(postHead+2).build())
						,postRepo.save(Post.builder().weightlifter(weightlifter).head(postHead+3).build())
						)
				);
		weightlifter.setPosts(posts);
		

		
		mockMvc.perform(get("/getApi/getPosts")
				.param("username", username)
				.param("pageIndex",""+ indexPagable)
				.cookie(new Cookie(AUTHEN_COOKIE_NAME, mainUser))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content[0].head").value(postHead+1))
				.andExpect(jsonPath("$.content[1].head").value(postHead+2))
				.andExpect(jsonPath("$.content[2].head").value(postHead+3))
				.andExpect(jsonPath("$.numberOfElements").value(expectedPostsSize))
				.andDo(print());
		
	}

}
