<template>
  <div v-if="show" class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <h2>Select Chatbot</h2>
      <div class="bot-grid">
        <div
          v-for="bot in availableBots"
          :key="bot.type"
          class="bot-card"
          :class="{ selected: selectedBot === bot.type }"
          @click="selectBot(bot.type)"
        >
          <div class="bot-icon">
            <i :class="bot.icon"></i>
          </div>
          <h3>{{ bot.name }}</h3>
          <p>{{ bot.description }}</p>
        </div>
      </div>
      <div class="modal-actions">
        <button class="cancel" @click="close">Cancel</button>
        <button
          class="select"
          @click="confirmSelection"
          :disabled="!selectedBot"
        >
          Select
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useApi } from "../composables/useApi";

const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(["select", "close"]);

const { getAvailableBotTypes } = useApi();
const availableBots = ref([
  {
    type: "code_review",
    name: "Code Review Bot",
    description: "Get detailed code reviews and enhancement suggestions",
    icon: "fas fa-code-review",
  },
  {
    type: "leetcode",
    name: "LeetCode Practice Bot",
    description: "Practice coding problems and get detailed explanations",
    icon: "fas fa-brain",
  },
  {
    type: "code_mentor",
    name: "Code Mentor Bot",
    description: "Get personalized coding guidance and best practices",
    icon: "fas fa-graduation-cap",
  },
  {
    type: "code_explainer",
    name: "Code Explainer Bot",
    description: "Understand complex code with detailed explanations",
    icon: "fas fa-book-open",
  },
]);

const selectedBot = ref(null);

const selectBot = (botType) => {
  selectedBot.value = botType;
};

const confirmSelection = () => {
  if (selectedBot.value) {
    emit("select", selectedBot.value);
    close();
  }
};

const close = () => {
  selectedBot.value = null;
  emit("close");
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.modal-content {
  background: var(--glass-effect);
  padding: 32px;
  border-radius: 20px;
  width: 90%;
  max-width: 800px;
  border: 1px solid var(--border-color);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  position: relative;
  animation: slideUp 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

h2 {
  color: var(--text-color);
  margin-bottom: 24px;
  font-size: 1.75rem;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.bot-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.bot-card {
  background: var(--background-color);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.bot-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  background: var(--glass-effect);
}

.bot-card.selected {
  border-color: var(--primary-color);
  background: var(--glass-effect);
}

.bot-icon {
  font-size: 2rem;
  color: var(--primary-color);
  margin-bottom: 12px;
}

.bot-card h3 {
  color: var(--text-color);
  margin-bottom: 8px;
  font-size: 1.1rem;
  font-weight: 600;
}

.bot-card p {
  color: var(--text-color);
  opacity: 0.8;
  font-size: 0.9rem;
  line-height: 1.4;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

button {
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: 500;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.cancel {
  background: var(--background-color);
  border: 1px solid var(--border-color);
  color: var(--text-muted);
}

.cancel:hover {
  background: var(--glass-effect);
  border-color: var(--primary-color);
  color: var(--text-color);
}

.select {
  background: var(--primary-color);
  border: none;
  color: var(--background-color);
  opacity: 0.3;
  box-shadow: 0 4px 15px var(--primary-color);
}

.select:hover:not(:disabled),
.select:not(:disabled) {
  transform: translateY(-2px);
  opacity: 1;
  box-shadow: 0 6px 20px var(--primary-color);
}

.select:disabled {
  opacity: 0.1;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
