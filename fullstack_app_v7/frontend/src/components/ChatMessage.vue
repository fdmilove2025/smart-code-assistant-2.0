<template>
  <div
    class="message"
    :class="{
      'user-message': isUser,
      'ai-message': !isUser && !isSystem,
      'system-message': isSystem
    }"
  >
    <div class="avatar">
      <span v-if="isUser">👤</span>
      <span v-else-if="isSystem">ℹ️</span>
      <span v-else>🤖</span>
    </div>
    <div class="content">
      <div class="text" v-html="formattedMessage"></div>
      <div class="timestamp">
        {{ formattedTime }}
        <span v-if="!isUser && !isSystem && message.model" class="model-indicator">
          {{ message.model === 'qwen' ? 'Qwen 2.5 Coder' : 'DeepSeek R1' }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import hljs from "highlight.js";
import "highlight.js/styles/github-dark.css";

const props = defineProps({
  message: {
    type: Object,
    required: true,
  },
});

const isUser = computed(() => props.message.role === "user");
const isSystem = computed(() => props.message.role === "system");

const formattedMessage = computed(() => {
  let text = props.message.message;
  // Replace code blocks with highlighted versions
  text = text.replace(/```(\w+)?\n([\s\S]*?)```/g, (match, lang, code) => {
    const language = lang || "plaintext";
    const highlighted = hljs.highlight(code, { language }).value;
    return `<pre><code class="hljs ${language}">${highlighted}</code></pre>`;
  });
  return text;
});

const formattedTime = computed(() => {
  const date = new Date(props.message.createdDateTime);
  return date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
});
</script>

<style scoped>
.message {
  display: flex;
  gap: 16px;
  padding: 20px;
  margin: 12px 0;
  border-radius: 16px;
  animation: fadeIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  max-width: 85%;
}

.message::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.05));
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.message:hover::before {
  opacity: 1;
}

.user-message {
  margin-left: auto;
  background: linear-gradient(135deg, rgba(108, 99, 255, 0.1), rgba(76, 175, 80, 0.1));
  border: 1px solid rgba(108, 99, 255, 0.2);
  box-shadow: 0 4px 20px rgba(108, 99, 255, 0.1);
}

.ai-message {
  margin-right: auto;
  background: linear-gradient(135deg, rgba(76, 175, 80, 0.1), rgba(108, 99, 255, 0.1));
  border: 1px solid rgba(76, 175, 80, 0.2);
  box-shadow: 0 4px 20px rgba(76, 175, 80, 0.1);
}

.system-message {
  margin: 8px auto;
  background: linear-gradient(135deg, rgba(255, 193, 7, 0.1), rgba(255, 152, 0, 0.1));
  border: 1px solid rgba(255, 193, 7, 0.2);
  box-shadow: 0 4px 20px rgba(255, 193, 7, 0.1);
  max-width: 70%;
  text-align: center;
  padding: 12px 20px;
}

.system-message .avatar {
  background: linear-gradient(135deg, rgba(255, 193, 7, 0.2), rgba(255, 152, 0, 0.2));
}

.system-message .text {
  color: var(--text-color);
  font-style: italic;
  text-align: center;
}

.avatar {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(108, 99, 255, 0.2), rgba(76, 175, 80, 0.2));
  border-radius: 50%;
  flex-shrink: 0;
  font-size: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.message:hover .avatar {
  transform: scale(1.1);
}

.content {
  flex: 1;
  min-width: 0;
}

.text {
  color: var(--text-color);
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 15px;
}

.text :deep(pre) {
  margin: 12px 0;
  padding: 16px;
  border-radius: 12px;
  overflow-x: auto;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  position: relative;
}

.text :deep(pre)::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(108, 99, 255, 0.3), transparent);
}

.text :deep(code) {
  font-family: 'Fira Code', monospace;
  font-size: 14px;
  line-height: 1.5;
}

.timestamp {
  margin-top: 8px;
  font-size: 0.8em;
  color: rgba(255, 255, 255, 0.5);
  display: flex;
  align-items: center;
  gap: 8px;
}

.model-indicator {
  display: inline-flex;
  align-items: center;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.85em;
  background: linear-gradient(135deg, rgba(108, 99, 255, 0.15), rgba(76, 175, 80, 0.15));
  border: 1px solid rgba(108, 99, 255, 0.2);
  color: rgba(255, 255, 255, 0.8);
}

.model-indicator::before {
  content: '🤖';
  margin-right: 4px;
  font-size: 0.9em;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Custom scrollbar for code blocks */
.text :deep(pre::-webkit-scrollbar) {
  height: 8px;
}

.text :deep(pre::-webkit-scrollbar-track) {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.text :deep(pre::-webkit-scrollbar-thumb) {
  background: var(--primary-color);
  border-radius: 4px;
}

/* Syntax highlighting improvements */
.text :deep(.hljs) {
  background: transparent;
}

.text :deep(.hljs-keyword),
.text :deep(.hljs-selector-tag),
.text :deep(.hljs-title),
.text :deep(.hljs-section),
.text :deep(.hljs-doctag),
.text :deep(.hljs-name),
.text :deep(.hljs-strong) {
  color: #ff79c6;
}

.text :deep(.hljs-string),
.text :deep(.hljs-attr),
.text :deep(.hljs-template-tag),
.text :deep(.hljs-template-variable) {
  color: #50fa7b;
}

.text :deep(.hljs-number),
.text :deep(.hljs-literal),
.text :deep(.hljs-variable),
.text :deep(.hljs-variable.constant_) {
  color: #bd93f9;
}

.text :deep(.hljs-comment),
.text :deep(.hljs-quote) {
  color: #6272a4;
  font-style: italic;
}
</style>
