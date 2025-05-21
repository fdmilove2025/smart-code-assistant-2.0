<template>
  <div v-if="show" class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <h2>New Chat Session</h2>
      <div class="input-group">
        <label for="session-name">Session Name</label>
        <input
          id="session-name"
          v-model="sessionName"
          type="text"
          placeholder="Enter session name"
          @keyup.enter="createSession"
          autofocus
        />
      </div>
      <div class="modal-actions">
        <button class="cancel" @click="close">Cancel</button>
        <button
          class="create"
          @click="createSession"
          :disabled="!sessionName.trim()"
        >
          Create
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(["create", "close"]);

const sessionName = ref("");

const createSession = () => {
  if (sessionName.value.trim()) {
    emit("create", sessionName.value.trim());
    sessionName.value = "";
  }
};

const close = () => {
  sessionName.value = "";
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
  max-width: 400px;
  border: 1px solid var(--border-color);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  position: relative;
  animation: slideUp 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.modal-content::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    135deg,
    var(--primary-color),
    var(--primary-hover)
  );
  opacity: 0.1;
  z-index: -1;
}

h2 {
  color: var(--text-color);
  margin-bottom: 24px;
  font-size: 1.75rem;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.input-group {
  margin-bottom: 32px;
}

label {
  display: block;
  color: var(--text-muted);
  margin-bottom: 12px;
  font-size: 0.95rem;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 14px 16px;
  background: var(--background-color);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  color: var(--text-color);
  font-size: 1rem;
  transition: all 0.3s ease;
}

input::placeholder {
  color: var(--text-muted);
  opacity: 0.7;
}

input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px var(--primary-color);
  background: var(--background-color);
  color: var(--text-color);
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
  display: flex;
  align-items: center;
  gap: 8px;
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

.create {
  background: var(--primary-color);
  border: none;
  color: var(--background-color);
  box-shadow: 0 4px 15px var(--primary-color);
  opacity: 0.3;
}

.create:hover:not(:disabled),
.create:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px var(--primary-color);
  opacity: 1;
}

.create:disabled {
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
