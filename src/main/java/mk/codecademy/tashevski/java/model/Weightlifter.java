package mk.codecademy.tashevski.java.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Objects;
import java.util.Set;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "weightLifters")
public class Weightlifter{
	
	
	
	
	@Id
	@Column(name = "usernames",updatable = false,length = 15)
	private String username;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String gender;
	
	private Date dateOfBirth;
	
	private String bio;
	
	
	@Lob
	private byte[] profilePic;
	
    @JsonManagedReference
	@OneToOne(mappedBy = "weightlifter",cascade = CascadeType.ALL,orphanRemoval = true)
	private MonthlySchedule monthlySchedule;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "weightlifter",cascade = CascadeType.ALL)
	private Set<Post> posts;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "friends",joinColumns = {
			@JoinColumn(name = "weightlifter")
	},inverseJoinColumns = {
			@JoinColumn(name="friend_weightlifter")
	})
	Set<Weightlifter> myFriends;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "friends",joinColumns = {
			@JoinColumn(name = "friend_weightlifter")
	},inverseJoinColumns = {
			@JoinColumn(name="weightlifter")
	})
	private Set<Weightlifter> friendOf;
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Weightlifter other = (Weightlifter) obj;
		return Objects.equals(username, other.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}
	
	
	
	
	
}
