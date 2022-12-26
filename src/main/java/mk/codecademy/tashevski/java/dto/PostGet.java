package mk.codecademy.tashevski.java.dto;


import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostGet {

	private Long id;
	private String head;
	private String description;
	private Set<Long> photosIds;
	
	

}
