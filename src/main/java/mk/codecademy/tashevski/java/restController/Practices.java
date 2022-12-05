package mk.codecademy.tashevski.java.restController;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import java.util.HashSet;

import mk.codecademy.tashevski.java.dto.RequestPost;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;

@RestController
@RequestMapping("/practice/")
public class Practices {
	@Autowired
	private WeightlifterRepo weightlifterRepo;
	
	private Set<Timestamp> timeStamps = new HashSet<>();
	
	@PostMapping("/getPage")
	public void getFriend(@Valid @RequestBody  RequestPost  post ) {
		System.out.println(post);
	}
}
 