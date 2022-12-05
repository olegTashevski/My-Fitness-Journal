package mk.codecademy.tashevski.java.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ResolvementOfFriendRequest {
	private String from;
	private String to;
	private boolean accepted;

}
