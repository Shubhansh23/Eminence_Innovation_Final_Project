package com.ei.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ei.util.JwtUtil;

@RestController
public class Task1Controller {

	private final JwtUtil jwtUtil;

	@Autowired
	public Task1Controller(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@RequestMapping(value = "/api/task1", method = RequestMethod.GET)
	public ResponseEntity<?> getTask1Data(@RequestHeader("Authorization") String authorizationHeader) {
		String token = authorizationHeader.substring(7);

		String username = jwtUtil.extractUsername(token);
		Date expiration = jwtUtil.extractExpiration(token);

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("username", username);
		responseData.put("expiration", expiration);

		return ResponseEntity.ok(responseData);
	}
}
