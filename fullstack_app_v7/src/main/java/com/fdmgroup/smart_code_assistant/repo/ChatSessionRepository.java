package com.fdmgroup.smart_code_assistant.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.smart_code_assistant.model.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

}
