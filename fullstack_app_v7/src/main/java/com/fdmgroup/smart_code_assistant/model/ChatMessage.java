package com.fdmgroup.smart_code_assistant.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	@Column(columnDefinition = "TEXT")
	private String message;
	private String role;
	private LocalDateTime createdDateTime;
	private String model;
	
	@ManyToOne
	@JsonIgnore
	private ChatSession chatSession;
	
	public ChatMessage() {
		super();
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public ChatSession getChatSession() {
		return chatSession;
	}
	
	public void setChatSession(ChatSession chatSession) {
		this.chatSession = chatSession;
	}
}
