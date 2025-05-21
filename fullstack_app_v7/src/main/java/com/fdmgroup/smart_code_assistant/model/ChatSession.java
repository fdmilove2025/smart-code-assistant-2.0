package com.fdmgroup.smart_code_assistant.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_session")
public class ChatSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sessionId;

	private String sessionName;

	private String botType;

	@OneToMany(mappedBy = "chatSession", cascade = jakarta.persistence.CascadeType.ALL)
	private Set<ChatMessage> messages = new HashSet<>();

	@ManyToOne
	@JsonIgnore
	private User user;

	public ChatSession() {
		super();
	}

	public ChatSession(String sessionName, String botType, User user) {
		super();
		this.sessionName = sessionName;
		this.botType = botType;
		this.user = user;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getBotType() {
		return botType;
	}

	public void setBotType(String botType) {
		this.botType = botType;
	}

	public Set<ChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(Set<ChatMessage> messages) {
		this.messages = messages;
	}

	public void addMessage(ChatMessage message) {
		this.messages.add(message);
		message.setChatSession(this);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
