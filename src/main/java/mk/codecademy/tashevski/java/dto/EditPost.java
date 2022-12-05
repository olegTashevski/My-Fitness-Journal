package mk.codecademy.tashevski.java.dto;

import javax.validation.constraints.NotBlank;

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
	@NotBlank
	private Long postId;
	
	private String newHead;
	@NotBlank
	private String newDescription;
	private MultipartFile[] newImages;
	private Long[] imagesIdsRemoved;

}
