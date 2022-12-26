package mk.codecademy.tashevski.java.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditPost {
	@NotNull
	@Min(0)
	private Long postId;
	
	@Size(max = 100,message = "max charachters of the head is 100")
	private String newHead;
	@Size(min = 1 ,max = 250,message = "max charachters of the description is 250 and must not be blank")
	private String newDescription;
	private MultipartFile[] newImages;
	private Long[] imagesIdsRemoved;

}
