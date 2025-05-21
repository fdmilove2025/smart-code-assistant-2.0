<template>
  <div class="app">
    <WelcomePage
      v-if="!showChatInterface && !showTeamPage && !showAuth"
      @start="handleStart"
    />
    <div v-else-if="!authService.isAuthenticated()" class="auth-container">
      <Login
        v-if="showLogin"
        @login-success="handleAuthSuccess"
        @show-register="showLogin = false"
      />
      <Register
        v-else
        @register-success="handleAuthSuccess"
        @show-login="showLogin = true"
      />
    </div>
    <div v-else>
      <TeamPage v-if="showTeamPage" @back="showTeamPage = false" />
      <ModelSelector
        v-else-if="showModelSelector"
        @select="selectModel"
        @back="showModelSelector = false"
      />
      <div v-else class="chat-interface">
        <div class="app-header">
          <h1 class="app-title"><span>Smart</span> Code Assistant</h1>
          <div class="header-controls">
            <div class="theme-toggle-container">
              <button class="theme-toggle" @click="toggleTheme">
                <div class="toggle-track">
                  <div class="toggle-thumb" :class="{ light: !isDarkMode }">
                    <span class="toggle-icon">{{
                      isDarkMode ? "🌙" : "☀️"
                    }}</span>
                  </div>
                </div>
              </button>
            </div>
            <div class="bot-indicator" v-if="currentSession">
              <span class="bot-icon">{{
                getBotIcon(currentSession.botType)
              }}</span>
              <span class="bot-name">{{
                getBotName(currentSession.botType)
              }}</span>
            </div>
            <div class="model-selector-container">
              <button
                class="model-selector-button"
                @click="showModelSelector = true"
              >
                <span class="model-name">{{
                  currentModel === "qwen" ? "Qwen 2.5 Coder" : "DeepSeek R1"
                }}</span>
                <span class="button-icon">🤖</span>
                <span class="change-text">Change Model</span>
              </button>
            </div>
          </div>
        </div>
        <Sidebar
          :sessions="Object.values(sessions)"
          :current-session-id="currentSession?.sessionId"
          @new-chat="handleNewChat"
          @select-session="loadSession"
          @update-session="updateSession"
        />
        <main class="main-content">
          <div v-if="!currentSession" class="empty-state">
            <div class="empty-state-content">
              <div class="empty-state-icon">⚡</div>
              <h2 class="empty-state-title">Start a New Chat</h2>
              <p class="empty-state-text">
                Select a bot to begin your coding journey
              </p>
              <button class="empty-state-button" @click="handleNewChat">
                Create New Chat
              </button>
            </div>
          </div>
          <template v-else>
            <div class="messages-container" ref="messagesContainer">
              <ChatMessage
                v-for="(message, index) in displayMessages"
                :key="`${message.createdDateTime}-${index}`"
                :message="message"
              />
              <div v-if="isLoading" class="loading-indicator">
                <div class="loading-dots">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
            <div class="input-container glass-effect">
              <textarea
                v-model="userInput"
                @keydown.enter.prevent="sendMessage"
                placeholder="Ask the Code Reaper for help..."
                rows="3"
                :disabled="!currentSession || isLoading"
              ></textarea>
              <div class="input-actions">
                <button
                  class="voice-button"
                  @click="isListening ? stopListening() : startListening()"
                  :class="{ listening: isListening }"
                  :disabled="!currentSession || isLoading"
                >
                  <span class="button-icon">{{
                    isListening ? "🎤" : "🎙️"
                  }}</span>
                </button>
                <button
                  @click="sendMessage"
                  :disabled="!userInput.trim() || !currentSession || isLoading"
                  class="send-button"
                >
                  {{ isLoading ? "Reaping..." : "Send" }}
                </button>
              </div>
            </div>
          </template>
          <div class="bottom-buttons">
            <div class="user-info">
              <span>{{ authService.getCurrentUser()?.username }}</span>
              <button class="logout-button" @click="handleLogout">
                Logout
              </button>
            </div>
            <button class="team-button" @click="showTeamPage = true">
              <span class="button-icon">👥</span>
              <span class="button-text">Meet the Team</span>
            </button>
          </div>
        </main>
        <NewSessionModal
          :show="showModal"
          @create="handleCreateSession"
          @close="showModal = false"
        />
      </div>
    </div>
    <BotSelectorModal
      :show="showBotSelector"
      @select="handleBotSelect"
      @close="showBotSelector = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch } from "vue";
import Sidebar from "./components/Sidebar.vue";
import ChatMessage from "./components/ChatMessage.vue";
import NewSessionModal from "./components/NewSessionModal.vue";
import WelcomePage from "./components/WelcomePage.vue";
import TeamPage from "./components/TeamPage.vue";
import ModelSelector from "./components/ModelSelector.vue";
import Login from "./components/Login.vue";
import Register from "./components/Register.vue";
import { useApi } from "./composables/useApi";
import { useSpeechRecognition } from "./composables/useSpeechRecognition";
import authService from "./services/authService";
import BotSelectorModal from "./components/BotSelectorModal.vue";

const {
  sendMessage: sendMessageApi,
  createSession: createSessionApi,
  getSession: getSessionApi,
  updateSessionMessages,
  updateSession: updateSessionApi,
  sessions,
  fetchUserSessions,
} = useApi();

const currentSession = ref(null);
const userInput = ref("");
const messagesContainer = ref(null);
const isLoading = ref(false);
const showModal = ref(false);
const showChatInterface = ref(false);
const showTeamPage = ref(false);
const showModelSelector = ref(false);
const currentModel = ref("qwen");
const showLogin = ref(true);
const showAuth = ref(false);
const { isListening, transcript, error, startListening, stopListening } =
  useSpeechRecognition();
const isDarkMode = ref(true);
const showBotSelector = ref(false);
const selectedBotType = ref(null);

// Watch for transcript changes and update userInput
watch(transcript, (newTranscript) => {
  if (newTranscript) {
    userInput.value = newTranscript;
  }
});

// Computed property for displaying messages of the current session
const displayMessages = computed(() => {
  if (!currentSession.value || !currentSession.value.sessionId) return [];
  const session = sessions.value[currentSession.value.sessionId];
  const messages = session?.messages || [];
  return [...messages].sort(
    (a, b) => new Date(a.createdDateTime) - new Date(b.createdDateTime)
  );
});

const scrollToBottom = async () => {
  await nextTick();
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};

const showNewSessionModal = () => {
  showModal.value = true;
};

const createNewSession = async (name = "New Chat", botType = null) => {
  try {
    if (!authService.isAuthenticated()) {
      console.error("Cannot create session: User not authenticated");
      showAuth.value = true;
      showChatInterface.value = false;
      return;
    }
    console.log("Creating new session with name:", name);
    const session = await createSessionApi(name, botType);
    currentSession.value = session;
    showModal.value = false;
    await scrollToBottom();
  } catch (error) {
    console.error("Error creating new session:", error);
    // Only redirect to auth if it's an authentication error
    if (error.response?.status === 403) {
      console.error("Authentication error, redirecting to login");
      authService.logout();
      showAuth.value = true;
      showChatInterface.value = false;
    }
    throw error;
  }
};

const loadSession = async (sessionId) => {
  try {
    const session = await getSessionApi(sessionId);
    if (session) {
      currentSession.value = session;
      await scrollToBottom();
    }
  } catch (error) {
    console.error("Error loading session:", error);
    // If session not found or unauthorized, remove it from the local sessions
    if (error.response?.status === 403 || error.response?.status === 404) {
      await fetchUserSessions(); // Refresh the sessions list
    }
  }
};

const selectModel = (model) => {
  // Only proceed if the model is actually changing
  if (model !== currentModel.value && currentSession.value) {
    // Create a system message for the model change
    const systemMessage = {
      role: "system",
      message: `AI Model changed to ${model === "qwen" ? "Qwen 2.5 Coder" : "DeepSeek R1"}`,
      createdDateTime: new Date().toISOString(),
      model: model,
      chatSession: currentSession.value
    };

    // Add the system message to the session's messages
    currentSession.value.messages.push(systemMessage);
    
    // Update the current model
    currentModel.value = model;
    
    // Close the model selector
    showModelSelector.value = false;

    // Ensure we scroll to the bottom after the model change
    nextTick(() => {
      scrollToBottom();
    });
  } else {
    // If no actual change, just close the selector
    showModelSelector.value = false;
  }
};

const sendMessage = async () => {
  if (!userInput.value.trim() || !currentSession.value || isLoading.value)
    return;

  const sessionId = currentSession.value.sessionId;
  if (!sessionId) {
    console.error("No valid session ID");
    return;
  }

  const currentMessages = [...(sessions.value[sessionId]?.messages || [])];
  const currentInput = userInput.value.trim();
  const userMessage = {
    message: currentInput,
    role: "user",
    createdDateTime: new Date().toISOString(),
    model: currentModel.value,
  };

  try {
    // Update UI with user message immediately
    currentMessages.push(userMessage);
    await updateSessionMessages(sessionId, currentMessages);

    // Clear input and set loading state
    userInput.value = "";
    isLoading.value = true;

    // Send message to backend
    const response = await sendMessageApi(
      currentInput,
      sessionId,
      currentModel.value
    );

    if (!response || !response.message) {
      throw new Error("Invalid response from server");
    }

    // Update UI with assistant message
    currentMessages.push(response);
    await updateSessionMessages(sessionId, currentMessages);

    // Refresh session to ensure we have the latest state
    await loadSession(sessionId);
  } catch (error) {
    console.error("Error sending message:", error);

    // Add error message to chat
    currentMessages.push({
      message:
        error.response?.data?.message ||
        "Sorry, there was an error processing your request. Please try again.",
      role: "assistant",
      createdDateTime: new Date().toISOString(),
      model: currentModel.value,
    });
    await updateSessionMessages(sessionId, currentMessages);
  } finally {
    isLoading.value = false;
    await scrollToBottom();
  }
};

const handleStart = () => {
  if (authService.isAuthenticated()) {
    console.log("User is authenticated, showing chat interface");
    showChatInterface.value = true;
    showAuth.value = false;
    createNewSession("New Chat");
  } else {
    console.log("User is not authenticated, showing auth interface");
    showAuth.value = true;
  }
};

const updateSession = async (updatedSession) => {
  try {
    await updateSessionApi(updatedSession);
    const refreshedSession = await getSessionApi(updatedSession.sessionId);
    currentSession.value = refreshedSession;
  } catch (error) {
    console.error("Error updating session:", error);
  }
};

const handleLogout = () => {
  authService.logout();
  currentSession.value = null;
  // Clear all sessions when logging out
  sessions.value = {};
  showChatInterface.value = false;
  showTeamPage.value = false;
  showModelSelector.value = false;
  showAuth.value = false;
  showLogin.value = true;
};

const handleAuthSuccess = async () => {
  console.log("Auth success, transitioning to chat interface");
  showAuth.value = false;
  showChatInterface.value = true;

  // Ensure we have a valid token before creating a session
  if (authService.isAuthenticated()) {
    try {
      // Fetch user's sessions first
      const userSessions = await fetchUserSessions();
      console.log("Fetched user sessions:", userSessions);

      // If there are existing sessions, set the first one as current
      if (userSessions && userSessions.length > 0) {
        currentSession.value = userSessions[0];
        await scrollToBottom();
      } else {
        // Create a new session if user has none
        await createNewSession("New Chat");
      }
    } catch (error) {
      console.error("Error setting up sessions:", error);
      // Don't redirect back to auth if we're already authenticated
      if (error.response?.status !== 403) {
        showAuth.value = true;
        showChatInterface.value = false;
      }
    }
  } else {
    console.error("No token found after successful authentication");
    showAuth.value = true;
    showChatInterface.value = false;
  }
};

const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value;
  document.documentElement.setAttribute(
    "data-theme",
    isDarkMode.value ? "dark" : "light"
  );
};

const handleNewChat = () => {
  showBotSelector.value = true;
};

const handleBotSelect = (botType) => {
  selectedBotType.value = botType;
  showBotSelector.value = false;
  showModal.value = true;
};

const handleCreateSession = async (sessionName) => {
  try {
    const newSession = await createNewSession(
      sessionName,
      selectedBotType.value
    );
    sessions.value[newSession.sessionId] = newSession;
    currentSession.value = newSession;
    showModal.value = false;
    selectedBotType.value = null;
    await scrollToBottom();
  } catch (error) {
    console.error("Error creating session:", error);
  }
};

const getBotIcon = (botType) => {
  switch (botType) {
    case "code_review":
      return "🔍";
    case "code_generator":
      return "⚡";
    default:
      return "🤖";
  }
};

const getBotName = (botType) => {
  switch (botType) {
    case "code_review":
      return "Code Review Bot";
    case "code_generator":
      return "Code Generator Bot";
    case "leetcode":
      return "LeetCode Bot";
    case "code_mentor":
      return "Code Mentor Bot";
    case "code_explainer":
      return "Code Explainer Bot";
    case "system_design":
      return "System Design Bot";
    case "algorithm":
      return "Algorithm Bot";
    case "debug":
      return "Debug Assistant";
    case "refactor":
      return "Refactoring Bot";
    case "documentation":
      return "Documentation Bot";
    default:
      return "Code Reaper";
  }
};

onMounted(async () => {
  if (authService.isAuthenticated()) {
    showChatInterface.value = true;
    try {
      // Fetch user's sessions
      const userSessions = await fetchUserSessions();
      console.log("Fetched user sessions on mount:", userSessions);

      // If there are existing sessions, set the first one as current
      if (userSessions && userSessions.length > 0) {
        currentSession.value = userSessions[0];
        await scrollToBottom();
      } else {
        // Create a new session if user has none
        await createNewSession("New Chat");
      }
    } catch (error) {
      console.error("Error setting up sessions:", error);
      if (error.response?.status === 403) {
        handleLogout();
      }
    }
  }
});
</script>

<style>
:root {
  /* Dark theme variables */
  --primary-color: #2e7d32;
  --primary-gradient: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
  --background-color: #121212;
  --sidebar-bg: rgba(18, 18, 18, 0.95);
  --message-bg-user: rgba(46, 125, 50, 0.1);
  --message-bg-assistant: rgba(255, 255, 255, 0.05);
  --text-color: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
  --border-color: rgba(255, 255, 255, 0.1);
  --glass-effect: rgba(17, 17, 17, 0.7);
  --button-hover: rgba(46, 125, 50, 0.15);
  --button-active: rgba(46, 125, 50, 0.25);
  --sidebar-width: 280px;
  --header-height: 64px;
  --input-container-bg: rgba(18, 18, 18, 0.98);
}

:root[data-theme="light"] {
  --primary-color: #2e7d32;
  --primary-gradient: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
  --background-color: #ffffff;
  --sidebar-bg: rgba(255, 255, 255, 0.95);
  --message-bg-user: rgba(46, 125, 50, 0.1);
  --message-bg-assistant: rgba(0, 0, 0, 0.05);
  --text-color: #1a1a1a;
  --text-secondary: rgba(0, 0, 0, 0.7);
  --border-color: rgba(0, 0, 0, 0.1);
  --glass-effect: rgba(255, 255, 255, 0.7);
  --button-hover: rgba(46, 125, 50, 0.15);
  --button-active: rgba(46, 125, 50, 0.25);
  --input-container-bg: rgba(255, 255, 255, 0.98);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    sans-serif;
  background-color: var(--background-color);
  color: var(--text-color);
  line-height: 1.6;
  min-height: 100vh;
}

.app {
  min-height: 100vh;
  background: var(--background-color);
  position: relative;
  overflow: hidden;
}

.auth-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: var(--background-color);
  z-index: 1000;
}

.chat-interface {
  display: flex;
  width: 100%;
  height: 100%;
  position: relative;
  background: var(--background-color);
  overflow: hidden;
}

.chat-interface::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
      circle at 20% 20%,
      rgba(46, 125, 50, 0.1) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 80% 80%,
      rgba(108, 99, 255, 0.1) 0%,
      transparent 50%
    );
  pointer-events: none;
}

.app-header {
  position: fixed;
  top: 0;
  right: 0;
  left: var(--sidebar-width);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1.5rem;
  height: var(--header-height);
  background-color: var(--sidebar-bg);
  border-bottom: 1px solid var(--border-color);
  z-index: 10;
  backdrop-filter: blur(10px);
  font-family: "Poppins", sans-serif;
  gap: 1rem;
}

.app-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--text-color);
  letter-spacing: -0.5px;
  display: flex;
  align-items: center;
  gap: 11px;
  position: relative;
  padding-left: 40px;
  flex-shrink: 0;
}

.app-title::before {
  content: "⚡";
  position: absolute;
  left: 0;
  font-size: 1.5rem;
  animation: pulse 2s infinite;
  color: #ffd700; /* Bright yellow color */
  filter: drop-shadow(0 0 8px rgba(255, 215, 0, 0.5));
  text-shadow: 0 0 10px rgba(255, 215, 0, 0.8);
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
    filter: drop-shadow(0 0 8px rgba(255, 215, 0, 0.5));
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
    filter: drop-shadow(0 0 12px rgba(255, 215, 0, 0.7));
  }
  100% {
    transform: scale(1);
    opacity: 1;
    filter: drop-shadow(0 0 8px rgba(255, 215, 0, 0.5));
  }
}

.header-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-left: auto;
}

.theme-toggle-container {
  flex-shrink: 0;
}

.bot-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  transition: all 0.3s ease;
  flex-shrink: 0;
  min-width: 120px;
  justify-content: center;
}

.bot-indicator:hover {
  background: var(--button-hover);
  border-color: var(--primary-color);
  transform: translateY(-1px);
}

.bot-icon {
  font-size: 20px;
  flex-shrink: 0;
}

.bot-name {
  font-weight: 500;
  color: var(--text-color);
  font-size: 0.95rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.model-selector-container {
  position: relative;
  flex-shrink: 0;
}

.model-selector-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  color: var(--text-color);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: 500;
  font-size: 0.95rem;
  position: relative;
  overflow: hidden;
}

.model-selector-button::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.1),
    transparent
  );
  transition: 0.5s;
}

.model-selector-button:hover::before {
  left: 100%;
}

.model-selector-button:hover {
  background: var(--button-hover);
  border-color: var(--primary-color);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(46, 125, 50, 0.2);
}

.model-selector-button .model-name {
  font-weight: 600;
  color: var(--primary-color);
}

.model-selector-button .button-icon {
  font-size: 18px;
  transition: transform 0.3s ease;
}

.model-selector-button:hover .button-icon {
  transform: scale(1.1) rotate(5deg);
}

.model-selector-button .change-text {
  color: var(--text-secondary);
  font-size: 0.9rem;
  opacity: 0.8;
  transition: opacity 0.3s ease;
}

.model-selector-button:hover .change-text {
  opacity: 1;
}

.team-button {
  position: fixed;
  bottom: 6rem;
  left: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  color: var(--text-color);
  cursor: pointer;
  transition: all 0.2s ease;
  z-index: 100;
  backdrop-filter: blur(10px);
}

.team-button:hover {
  background-color: var(--button-hover);
  border-color: var(--primary-color);
}

.button-icon {
  font-size: 1.2rem;
}

.button-text {
  font-size: 0.9rem;
}

.main-content {
  flex: 1;
  margin-left: var(--sidebar-width);
  margin-top: var(--header-height);
  display: flex;
  flex-direction: column;
  height: calc(100vh - var(--header-height));
  background: var(--background-color);
  position: relative;
  z-index: 1;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  scroll-behavior: smooth;
  position: relative;
  margin-bottom: 180px;
}

.messages-container::-webkit-scrollbar {
  width: 8px;
}

.messages-container::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.messages-container::-webkit-scrollbar-thumb {
  background: var(--primary-color);
  border-radius: 4px;
  opacity: 0.5;
}

.messages-container::-webkit-scrollbar-thumb:hover {
  opacity: 0.8;
}

.input-container {
  position: fixed;
  bottom: 0;
  left: var(--sidebar-width);
  right: 0;
  padding: 24px;
  background: var(--glass-effect);
  border-top: 1px solid var(--border-color);
  backdrop-filter: blur(10px);
  box-shadow: 0 -2px 20px rgba(0, 0, 0, 0.05);
  z-index: 10;
  display: flex;
  flex-direction: column;
  gap: 12px;
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.input-container textarea {
  width: 100%;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 12px;
  color: var(--text-color);
  font-size: 14px;
  resize: none;
  outline: none;
  min-height: 80px;
  max-height: 200px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.input-container textarea:focus {
  border-color: var(--primary-color);
  box-shadow: 0 4px 12px rgba(46, 125, 50, 0.2);
  background: var(--glass-effect);
}

.input-actions {
  display: flex;
  gap: 8px;
  align-items: flex-end;
  justify-content: flex-end;
}

.voice-button {
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 8px;
  color: var(--text-color);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 40px;
  height: 40px;
}

.voice-button:hover {
  background: var(--button-hover);
  border-color: var(--primary-color);
}

.voice-button.listening {
  background: var(--primary-color);
  border-color: var(--primary-color);
  animation: pulse 1.5s infinite;
}

.voice-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.send-button {
  background: var(--primary-gradient);
  color: white;
  border: none;
  padding: 12px 28px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  font-size: 0.95rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 2px 8px rgba(46, 125, 50, 0.2);
  position: relative;
  overflow: hidden;
}

.send-button::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transition: 0.5s;
}

.send-button:hover::before {
  left: 100%;
}

.send-button:hover {
  background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(46, 125, 50, 0.3);
}

.loading-indicator {
  display: flex;
  justify-content: center;
  padding: 16px;
  position: relative;
}

.loading-dots {
  display: flex;
  gap: 4px;
  position: relative;
}

.loading-dots span {
  width: 8px;
  height: 8px;
  background: var(--primary-color);
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
  position: relative;
}

.loading-dots span::before {
  content: "";
  position: absolute;
  inset: -4px;
  border-radius: 50%;
  background: var(--primary-color);
  opacity: 0.2;
  animation: pulse 1.4s infinite ease-in-out;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
    opacity: 0.2;
  }
  50% {
    transform: scale(1.5);
    opacity: 0.1;
  }
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

.tutorial-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  pointer-events: none;
}

.tutorial-backdrop {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(4px);
  pointer-events: auto;
}

.tutorial-tooltip {
  position: absolute;
  background: var(--sidebar-bg);
  border: 1px solid var(--primary-color);
  border-radius: 12px;
  padding: 20px;
  max-width: 300px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  pointer-events: auto;
  animation: fadeIn 0.3s ease-out;
}

.tooltip-content {
  margin-bottom: 16px;
}

.tooltip-content h3 {
  color: var(--primary-color);
  margin-bottom: 8px;
  font-size: 1.1rem;
}

.tooltip-content p {
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.5;
}

.tooltip-arrow {
  position: absolute;
  width: 0;
  height: 0;
  border: 8px solid transparent;
}

.tooltip-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.skip-button,
.next-button {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.skip-button {
  background: transparent;
  border: 1px solid var(--border-color);
  color: var(--text-secondary);
}

.next-button {
  background: var(--primary-color);
  border: none;
  color: white;
}

.skip-button:hover {
  background: var(--button-hover);
  border-color: var(--primary-color);
}

.next-button:hover {
  background: var(--primary-gradient);
  transform: translateY(-1px);
}

/* Position tooltips */
.model-selector-tooltip {
  top: 20px;
  right: 20px;
}

.model-selector-tooltip .tooltip-arrow {
  top: 50%;
  right: -16px;
  transform: translateY(-50%);
  border-left-color: var(--primary-color);
}

.team-button-tooltip {
  bottom: 80px;
  left: 20px;
}

.team-button-tooltip .tooltip-arrow {
  bottom: -16px;
  left: 20px;
  border-top-color: var(--primary-color);
}

.chat-input-tooltip {
  bottom: 100px;
  left: 50%;
  transform: translateX(-50%);
}

.chat-input-tooltip .tooltip-arrow {
  bottom: -16px;
  left: 50%;
  transform: translateX(-50%);
  border-top-color: var(--primary-color);
}

.sidebar-tooltip {
  top: 50%;
  left: 20px;
  transform: translateY(-50%);
}

.sidebar-tooltip .tooltip-arrow {
  top: 50%;
  left: -16px;
  transform: translateY(-50%);
  border-right-color: var(--primary-color);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.theme-toggle-container {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  z-index: 20;
}

.theme-toggle {
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  position: relative;
}

.toggle-track {
  width: 60px;
  height: 32px;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  position: relative;
  transition: all 0.3s ease;
}

.toggle-thumb {
  width: 28px;
  height: 28px;
  background: var(--primary-color);
  border-radius: 50%;
  position: absolute;
  top: 1px;
  left: 1px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-thumb.light {
  transform: translateX(28px);
  background: var(--primary-color);
}

.toggle-icon {
  font-size: 14px;
  line-height: 1;
}

.theme-toggle:hover .toggle-track {
  background: var(--button-hover);
  border-color: var(--primary-color);
}

.theme-toggle:hover .toggle-thumb {
  transform: scale(1.1);
}

.theme-toggle:hover .toggle-thumb.light {
  transform: translateX(28px) scale(1.1);
}

.bottom-buttons {
  position: fixed;
  bottom: 1rem;
  left: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  z-index: 100;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  color: var(--text-color);
  padding: 0.5rem 1rem;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  backdrop-filter: blur(10px);
}

.logout-button {
  background: none;
  border: 1px solid var(--border-color);
  color: var(--text-color);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout-button:hover {
  border-color: var(--primary-color);
  background: rgba(255, 255, 255, 0.05);
}

.team-button {
  position: fixed;
  bottom: 6rem;
  left: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  color: var(--text-color);
  cursor: pointer;
  transition: all 0.2s ease;
  z-index: 100;
  backdrop-filter: blur(10px);
}

.team-button:hover {
  background-color: var(--button-hover);
  border-color: var(--primary-color);
}

.glass-effect {
  background: var(--glass-effect);
  backdrop-filter: blur(10px);
  border: 1px solid var(--border-color);
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.bot-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  margin-right: 16px;
  transition: all 0.3s ease;
}

.bot-indicator:hover {
  background: var(--button-hover);
  border-color: var(--primary-color);
  transform: translateY(-1px);
}

.bot-icon {
  font-size: 20px;
}

.bot-name {
  font-weight: 500;
  color: var(--text-color);
  font-size: 0.95rem;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 2rem;
  text-align: center;
  background: var(--background-color);
}

.empty-state-content {
  max-width: 400px;
  padding: 2rem;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.empty-state-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  color: var(--primary-color);
  filter: drop-shadow(0 0 8px rgba(46, 125, 50, 0.3));
}

.empty-state-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.empty-state-text {
  color: var(--text-secondary);
  margin-bottom: 1.5rem;
  font-size: 1rem;
}

.empty-state-button {
  background: var(--primary-gradient);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  font-size: 1rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 8px rgba(46, 125, 50, 0.2);
}

.empty-state-button:hover {
  background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(46, 125, 50, 0.3);
}

.profile-button-container,
.profile-button,
.profile-icon,
.profile-button .username {
  display: none;
}
</style>
