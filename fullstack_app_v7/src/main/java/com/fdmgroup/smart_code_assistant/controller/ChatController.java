package com.fdmgroup.smart_code_assistant.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.smart_code_assistant.model.ChatMessage;
import com.fdmgroup.smart_code_assistant.model.ChatSession;
import com.fdmgroup.smart_code_assistant.service.ChatMessageService;
import com.fdmgroup.smart_code_assistant.service.ChatSessionService;

@CrossOrigin()
@RestController
@RequestMapping("/api/code")
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	@Autowired
	ChatMessageService chatMessageService;
	@Autowired
	ChatSessionService chatSessionService;

	@PostMapping("/session/{name}/{botType}")
	public ResponseEntity<ChatSession> createNewSession(@PathVariable String name, @PathVariable String botType) {
		try {
			logger.info("Creating new session with name: {} and bot type: {}", name, botType);
			ChatSession newSession = chatSessionService.getNewSession(name, botType);
			logger.info("Created new session: {}", newSession.getSessionId());
			return ResponseEntity.ok(newSession);
		} catch (Exception e) {
			logger.error("Error creating new session: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/bot-types")
	public ResponseEntity<List<String>> getAvailableBotTypes() {
		try {
			logger.info("Retrieving available bot types");
			List<String> botTypes = chatSessionService.getAvailableBotTypes();
			logger.info("Retrieved {} bot types", botTypes.size());
			return ResponseEntity.ok(botTypes);
		} catch (Exception e) {
			logger.error("Error retrieving bot types: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/history/{id}")
	public ResponseEntity<ChatSession> retrieveSession(@PathVariable Long id) {
		try {
			logger.info("Retrieving session: {}", id);
			Optional<ChatSession> session = chatSessionService.getExistingSession(id);
			if (session.isEmpty()) {
				logger.warn("Session not found: {}", id);
				return ResponseEntity.notFound().build();
			}
			logger.info("Retrieved session: {}", id);
			return ResponseEntity.ok(session.get());
		} catch (Exception e) {
			logger.error("Error retrieving session {}: {}", id, e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/sessions")
	public ResponseEntity<Set<ChatSession>> getUserSessions() {
		try {
			logger.info("Retrieving user sessions");
			Set<ChatSession> sessions = chatSessionService.getUserSessions();
			logger.info("Retrieved {} sessions", sessions.size());
			return ResponseEntity.ok(sessions);
		} catch (Exception e) {
			logger.error("Error retrieving user sessions: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/{id}")
	public ResponseEntity<ChatMessage> getResponse(@PathVariable Long id, @RequestBody ChatMessage userInput) {
		try {
			logger.info("Processing message for session {}: {}", id, userInput.getMessage());

			// Ensure model is set
			if (userInput.getModel() == null || userInput.getModel().trim().isEmpty()) {
				userInput.setModel("qwen");
			}

			ChatMessage response = chatMessageService.createResponse(id, userInput);
			logger.info("Generated response for session {}", id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error processing message for session {}: {}", id, e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/string/{id}")
	public ResponseEntity<String> getStringResponse(@PathVariable Long id, @RequestBody ChatMessage userInput) {
		try {
			logger.info("Processing string message for session {}: {}", id, userInput.getMessage());

			// Ensure model is set
			if (userInput.getModel() == null || userInput.getModel().trim().isEmpty()) {
				userInput.setModel("qwen");
			}

			ChatMessage response = chatMessageService.createResponse(id, userInput);
			logger.info("Generated string response for session {}", id);
			return ResponseEntity.ok(response.getMessage());
		} catch (Exception e) {
			logger.error("Error processing string message for session {}: {}", id, e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/session/{id}")
	public ResponseEntity<ChatSession> updateSession(@PathVariable Long id, @RequestBody ChatSession updatedSession) {
		try {
			logger.info("Updating session {}", id);
			ChatSession session = chatSessionService.updateSession(id, updatedSession);
			logger.info("Updated session {}", id);
			return ResponseEntity.ok(session);
		} catch (Exception e) {
			logger.error("Error updating session {}: {}", id, e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}
}
