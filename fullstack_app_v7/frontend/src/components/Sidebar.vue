<template>
  <div class="sidebar">
    <div class="sidebar-header">
      <button class="new-chat" @click="$emit('new-chat')">
        <span class="icon">+</span>
        New Chat
      </button>
    </div>
    <div class="sessions">
      <div
        v-for="session in sessions"
        :key="session.sessionId"
        class="session-item"
        :class="{ active: currentSessionId === session.sessionId }"
      >
        <div
          class="session-content"
          @click="$emit('select-session', session.sessionId)"
        >
          <span class="icon">💬</span>
          <span
            v-if="!editingSession || editingSession !== session.sessionId"
            class="title"
          >
            {{ session.sessionName }}
          </span>
          <input
            v-else
            v-model="editName"
            type="text"
            class="edit-input"
            @keyup.enter="saveEdit(session)"
            @keyup.esc="cancelEdit"
            @blur="saveEdit(session)"
            @click.stop
            ref="editInput"
          />
        </div>
        <button
          v-if="!editingSession || editingSession !== session.sessionId"
          class="edit-button"
          @click.stop="startEdit(session)"
        >
          ✏️
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from "vue";
import { useApi } from "../composables/useApi";

const { fetchUserSessions } = useApi();

const props = defineProps({
  sessions: {
    type: Array,
    default: () => [],
  },
  currentSessionId: {
    type: [Number, String],
    default: null,
  },
});

const emit = defineEmits(["new-chat", "select-session", "update-session"]);

const editingSession = ref(null);
const editName = ref("");
const editInput = ref(null);

const startEdit = async (session) => {
  editingSession.value = session.sessionId;
  editName.value = session.sessionName;
  await nextTick();
  editInput.value?.focus();
};

const saveEdit = async (session) => {
  if (editName.value.trim() && editName.value !== session.sessionName) {
    emit("update-session", {
      ...session,
      sessionName: editName.value.trim(),
    });
  }
  editingSession.value = null;
  editName.value = "";
};

const cancelEdit = () => {
  editingSession.value = null;
  editName.value = "";
};
</script>

<style scoped>
.sidebar {
  width: var(--sidebar-width);
  height: 100vh;
  background: var(--sidebar-bg);
  color: var(--text-color);
  display: flex;
  flex-direction: column;
  padding: 16px;
  position: fixed;
  left: 0;
  top: 0;
  border-right: 1px solid var(--border-color);
}

.sidebar-header {
  padding: 8px;
  margin-bottom: 24px;
}

.new-chat {
  width: 100%;
  padding: 16px;
  background: var(--primary-gradient);
  border: none;
  border-radius: 12px;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 500;
  font-size: 15px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(46, 125, 50, 0.2);
  position: relative;
  overflow: hidden;
}

.new-chat::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    45deg,
    transparent,
    rgba(255, 255, 255, 0.1),
    transparent
  );
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.new-chat:hover {
  background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(46, 125, 50, 0.3);
}

.new-chat:hover::before {
  transform: translateX(100%);
}

.new-chat:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(46, 125, 50, 0.2);
}

.new-chat .icon {
  font-size: 22px;
  font-weight: 300;
  background: rgba(255, 255, 255, 0.2);
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.new-chat:hover .icon {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.sessions {
  flex: 1;
  overflow-y: auto;
  margin-top: 8px;
  padding-right: 8px;
}

.sessions::-webkit-scrollbar {
  width: 6px;
}

.sessions::-webkit-scrollbar-track {
  background: var(--glass-effect);
  border-radius: 3px;
}

.sessions::-webkit-scrollbar-thumb {
  background: var(--primary-color);
  border-radius: 3px;
}

.session-item {
  padding: 12px;
  margin: 8px 0;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s ease;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  position: relative;
}

.session-content {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  min-width: 0;
  position: relative;
  z-index: 1;
}

.session-item:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: var(--primary-color);
}

.session-item.active {
  background: rgba(46, 125, 50, 0.1);
  border-color: var(--primary-color);
}

.icon {
  font-size: 18px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: var(--glass-effect);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.session-item:hover .icon {
  background: rgba(46, 125, 50, 0.1);
}

.title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--text-color);
}

.edit-button {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  font-size: 16px;
  opacity: 0.7;
  transition: all 0.3s ease;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-color);
}

.edit-button:hover {
  opacity: 1;
  background: var(--glass-effect);
}

.edit-input {
  flex: 1;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 4px 8px;
  color: var(--text-color);
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
}

.edit-input:focus {
  border-color: var(--primary-color);
  background: rgba(255, 255, 255, 0.05);
}
</style>
