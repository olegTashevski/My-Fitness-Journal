package mk.codecademy.tashevski.java.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class RequestPost {
	@NotBlank
	private String weightlifterUsername;
	
	@Size(max = 100,message = "max charachters of the head is 100")
	private String head;
	@Size(max = 250,message = "max charachters of the description is 250 and must not be blank")
	@NotBlank(message = "description must not be blank")
	private String description;
	private MultipartFile[] images;
	private String hasImages;
	@Override
	public int hashCode() {
		return Objects.hash(hasImages, weightlifterUsername);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestPost other = (RequestPost) obj;
		return Objects.equals(hasImages, other.hasImages)
				&& Objects.equals(weightlifterUsername, other.weightlifterUsername);
	}
	
	
	
	
	
}
