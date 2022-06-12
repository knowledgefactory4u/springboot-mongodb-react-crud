package com.knf.sibin.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.knf.sibin.dev.document.User;
import com.knf.sibin.dev.exception.ResourceNotFoundException;
import com.knf.sibin.dev.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 4800)
@RestController
@RequestMapping("/api/v1/")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}

	
	@PostMapping("/users")
	public User createUser(@RequestBody User user) {
		
		Random random = new Random();
		user.setId((user.getFirstName() + user.getLastName() + user.getEmailId()) + random.nextInt(1000));
		return userRepository.save(user);
	}

	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable String id) {
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));
		return ResponseEntity.ok(user);
	}

	
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));

		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setEmailId(userDetails.getEmailId());
		User updatedUser = userRepository.save(user);
		return ResponseEntity.ok(updatedUser);
	}

	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable String id) {
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));

		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
