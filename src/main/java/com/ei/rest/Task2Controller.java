package com.ei.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class Task2Controller {

	private final RestTemplate restTemplate;

	private static final String MATCHES_API_BASE_URL = "https://jsonmock.hackerrank.com/api/football_matches";

	@Autowired
	public Task2Controller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/api/task2")
	public ResponseEntity<?> getTask2Data(@RequestParam int year) {
		String url = MATCHES_API_BASE_URL + "?year=" + year + "&page=1";

		CompletableFuture<Integer> drawMatchesCount = CompletableFuture.supplyAsync(() -> {
			try {
				ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
				Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 6000));

				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response.getBody());
				JsonNode dataNode = root.get("data");

				int count = 0;

				for (JsonNode matchNode : dataNode) {
					int team1Goals = matchNode.get("team1goals").asInt();
					int team2Goals = matchNode.get("team2goals").asInt();

					if (team1Goals == team2Goals) {
						count++;
					}
				}

				return count;

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Thread interrupted while fetching data", e);
			} catch (IOException e) {
				throw new RuntimeException("Error parsing JSON response", e);
			} catch (Exception e) {
				throw new RuntimeException("Error fetching data", e);
			}
		});

		try {
			Integer count = drawMatchesCount.get();
			return ResponseEntity.ok("Draw matches count for year " + year + ": " + count);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching data");
		}
	}
}
