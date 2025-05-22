package com.fdmgroup.smart_code_assistant.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fdmgroup.smart_code_assistant.model.ChatSession;
import com.fdmgroup.smart_code_assistant.model.User;
import com.fdmgroup.smart_code_assistant.repo.ChatSessionRepository;

@Service
public class ChatSessionService {

	@Autowired
	private ChatSessionRepository chatSessionRepository;

	public ChatSession getNewSession(String name, String botType, String language) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ChatSession session = new ChatSession(name, botType, language, currentUser);
		return chatSessionRepository.save(session);
	}

	public ChatSession getNewSession(String name, String botType) {
		return getNewSession(name, botType, "Python");
	}

	public Optional<ChatSession> getExistingSession(Long id) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<ChatSession> session = chatSessionRepository.findById(id);
		if (session.isPresent() && session.get().getUser().getUserId().equals(currentUser.getUserId())) {
			return session;
		}
		return Optional.empty();
	}

	public ChatSession updateSession(Long id, ChatSession updatedSession) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<ChatSession> existingSession = chatSessionRepository.findById(id);
		if (existingSession.isEmpty() || !existingSession.get().getUser().getUserId().equals(currentUser.getUserId())) {
			throw new RuntimeException("Session not found or access denied");
		}

		ChatSession session = existingSession.get();
		session.setSessionName(updatedSession.getSessionName());
		session.setBotType(updatedSession.getBotType());
		session.setMessages(existingSession.get().getMessages());
		return chatSessionRepository.save(session);
	}

	public Set<ChatSession> getUserSessions() {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return currentUser.getChatSessions();
	}

	public List<String> getAvailableBotTypes() {
		return Arrays.asList(
				"code_review",
				"leetcode",
				"code_mentor",
				"code_explainer");
	}

	public List<String> getAvailableLanguages() {
		return Arrays.asList(
				"Python",
				"Java",
				"C++",
				"Javascript",
				"Mojo",
				"Scala"
		);
	}
}
