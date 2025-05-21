<template>
  <div class="model-selector">
    <div class="background-effects">
      <div class="grid-overlay"></div>
      <div class="particles"></div>
      <div class="glow-effects">
        <div class="glow glow-1"></div>
        <div class="glow glow-2"></div>
        <div class="glow glow-3"></div>
      </div>
    </div>

    <div class="content">
      <h1 class="title">Choose Your AI Champion</h1>
      <p class="subtitle">Select the model that will assist you in your coding journey</p>

      <div class="battle-arena">
        <div class="model-card" :class="{ 'selected': selectedModel === 'qwen' }" @click="selectModel('qwen')">
          <div class="model-avatar">
            <div class="robot qwen-robot">
              <div class="robot-head">
                <div class="eye left"></div>
                <div class="eye right"></div>
                <div class="antenna"></div>
              </div>
              <div class="robot-body">
                <div class="circuit-lines"></div>
              </div>
              <div class="robot-arm left"></div>
              <div class="robot-arm right"></div>
              <div class="energy-aura"></div>
            </div>
          </div>
          <div class="model-info">
            <h2 class="model-name">Qwen 2.5 Coder</h2>
            <p class="model-specs">0.5B Parameters</p>
            <div class="model-stats">
              <div class="stat">
                <span class="stat-label">Speed</span>
                <div class="stat-bar">
                  <div class="stat-fill" style="width: 90%"></div>
                </div>
              </div>
              <div class="stat">
                <span class="stat-label">Accuracy</span>
                <div class="stat-bar">
                  <div class="stat-fill" style="width: 45%"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="vs-badge">
          <span>VS</span>
        </div>

        <div class="model-card" :class="{ 'selected': selectedModel === 'deepseek' }" @click="selectModel('deepseek')">
          <div class="model-avatar">
            <div class="robot deepseek-robot">
              <div class="robot-head">
                <div class="eye left"></div>
                <div class="eye right"></div>
                <div class="antenna"></div>
              </div>
              <div class="robot-body">
                <div class="circuit-lines"></div>
              </div>
              <div class="robot-arm left"></div>
              <div class="robot-arm right"></div>
              <div class="energy-aura"></div>
            </div>
          </div>
          <div class="model-info">
            <h2 class="model-name">DeepSeek R1</h2>
            <p class="model-specs">8B Parameters</p>
            <div class="model-stats">
              <div class="stat">
                <span class="stat-label">Speed</span>
                <div class="stat-bar">
                  <div class="stat-fill" style="width: 45%"></div>
                </div>
              </div>
              <div class="stat">
                <span class="stat-label">Accuracy</span>
                <div class="stat-bar">
                  <div class="stat-fill" style="width: 95%"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <button class="confirm-button" @click="confirmSelection" :disabled="!selectedModel">
        <span class="button-text">Confirm Selection</span>
        <span class="button-icon">→</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const selectedModel = ref(null);

const selectModel = (model) => {
  selectedModel.value = model;
};

const confirmSelection = () => {
  if (selectedModel.value) {
    emit('select', selectedModel.value);
  }
};

const emit = defineEmits(['select']);
</script>

<style scoped>
.model-selector {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--background-color);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.background-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}

.grid-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(rgba(108, 99, 255, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(108, 99, 255, 0.1) 1px, transparent 1px);
  background-size: 50px 50px;
  opacity: 0.3;
  animation: gridMove 20s linear infinite;
}

.particles {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at center, transparent 0%, var(--background-color) 70%);
}

.particles::before,
.particles::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Ccircle cx='50' cy='50' r='1' fill='%236C63FF' fill-opacity='0.3'/%3E%3C/svg%3E");
  background-size: 50px 50px;
  animation: particleFloat 30s linear infinite;
}

.particles::after {
  animation-delay: -15s;
  opacity: 0.5;
}

.glow-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.3;
  animation: glowPulse 8s ease-in-out infinite;
}

.glow-1 {
  width: 400px;
  height: 400px;
  background: var(--primary-color);
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.glow-2 {
  width: 300px;
  height: 300px;
  background: #4CAF50;
  bottom: -50px;
  right: -50px;
  animation-delay: -2s;
}

.glow-3 {
  width: 200px;
  height: 200px;
  background: #FF6B6B;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation-delay: -4s;
}

.content {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 2rem;
  max-width: 1200px;
  width: 100%;
}

.title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  color: var(--text-color);
  text-align: center;
}

.subtitle {
  font-size: 1.2rem;
  color: var(--text-color);
  opacity: 0.8;
  margin-bottom: 3rem;
  text-align: center;
}

.battle-arena {
  display: flex;
  gap: 2rem;
  align-items: center;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.model-card {
  flex: 1;
  max-width: 400px;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.model-card:hover {
  border-color: var(--primary-color);
  background: rgba(255, 255, 255, 0.05);
}

.model-card.selected {
  border-color: var(--primary-color);
  background: rgba(46, 125, 50, 0.1);
}

.model-avatar {
  width: 180px;
  height: 180px;
  margin: 0 auto;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.robot-avatar img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  transition: all 0.3s ease;
}

.model-card:hover .robot-avatar img {
  transform: scale(1.05);
}

.model-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 1rem;
}

.model-name {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-color);
  margin: 0;
}

.model-specs {
  font-size: 0.9rem;
  color: var(--text-color);
  opacity: 0.8;
  margin: 0;
}

.model-stats {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin: 0;
}

.stat {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  text-align: left;
}

.stat-label {
  font-size: 0.9rem;
  color: var(--text-color);
  opacity: 0.8;
}

.stat-bar {
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  overflow: hidden;
}

.stat-fill {
  height: 100%;
  background: var(--primary-gradient);
  border-radius: 3px;
  transition: width 1s ease-out;
}

.vs-badge {
  width: 60px;
  height: 60px;
  background: var(--primary-gradient);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  box-shadow: 0 0 30px rgba(108, 99, 255, 0.5);
  animation: rotate 4s linear infinite;
}

.confirm-button {
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 8px;
  padding: 1rem 2rem;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 2rem;
}

.confirm-button:hover {
  background: #1B5E20;
}

.confirm-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.button-icon {
  transition: transform 0.3s ease;
}

.confirm-button:hover:not(:disabled) .button-icon {
  transform: translateX(4px);
}

.back-button {
  position: absolute;
  top: 20px;
  left: 20px;
  background: none;
  border: 1px solid var(--border-color);
  color: var(--text-color);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.back-button:hover {
  border-color: var(--primary-color);
  background: rgba(255, 255, 255, 0.05);
}

@keyframes gridMove {
  0% { transform: translateY(0); }
  100% { transform: translateY(50px); }
}

@keyframes particleFloat {
  0% { transform: translateY(0) rotate(0deg); }
  100% { transform: translateY(-50px) rotate(360deg); }
}

@keyframes glowPulse {
  0%, 100% { transform: scale(1); opacity: 0.3; }
  50% { transform: scale(1.2); opacity: 0.4; }
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0px);
  }
}

.robot {
  position: relative;
  width: 140px;
  height: 180px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* Qwen Robot - Sleek and Modern Design */
.qwen-robot {
  background: linear-gradient(135deg, #2E7D32 0%, #1B5E20 100%);
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(46, 125, 50, 0.3);
}

.qwen-robot .robot-head {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  position: relative;
  margin-bottom: 20px;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.qwen-robot .eye {
  width: 20px;
  height: 20px;
  background: #fff;
  border-radius: 50%;
  position: absolute;
  top: 30%;
  animation: blink 3s infinite;
}

.qwen-robot .eye.left {
  left: 20%;
}

.qwen-robot .eye.right {
  right: 20%;
}

.qwen-robot .antenna {
  width: 4px;
  height: 20px;
  background: #fff;
  position: absolute;
  top: -20px;
  left: 50%;
  transform: translateX(-50%);
}

.qwen-robot .robot-body {
  width: 100px;
  height: 60px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  position: relative;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.qwen-robot .circuit-lines {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.2) 50%, transparent 100%),
    linear-gradient(0deg, transparent 0%, rgba(255, 255, 255, 0.2) 50%, transparent 100%);
  background-size: 20px 20px;
  animation: circuitFlow 2s linear infinite;
}

.qwen-robot .robot-arm {
  width: 20px;
  height: 60px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  position: absolute;
  top: 40px;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.qwen-robot .robot-arm.left {
  left: -30px;
  transform: rotate(-15deg);
}

.qwen-robot .robot-arm.right {
  right: -30px;
  transform: rotate(15deg);
}

/* DeepSeek Robot - Angular and Powerful Design */
.deepseek-robot {
  background: linear-gradient(135deg, #1a237e 0%, #0d47a1 100%);
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(26, 35, 126, 0.3);
}

.deepseek-robot .robot-head {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.1);
  clip-path: polygon(50% 0%, 100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%);
  position: relative;
  margin-bottom: 20px;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.deepseek-robot .eye {
  width: 24px;
  height: 24px;
  background: #fff;
  clip-path: polygon(50% 0%, 100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%);
  position: absolute;
  top: 30%;
  animation: blink 3s infinite;
}

.deepseek-robot .eye.left {
  left: 15%;
}

.deepseek-robot .eye.right {
  right: 15%;
}

.deepseek-robot .antenna {
  width: 4px;
  height: 25px;
  background: #fff;
  position: absolute;
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
  clip-path: polygon(50% 0%, 100% 100%, 0% 100%);
}

.deepseek-robot .robot-body {
  width: 100px;
  height: 60px;
  background: rgba(255, 255, 255, 0.1);
  clip-path: polygon(10% 0%, 90% 0%, 100% 50%, 90% 100%, 10% 100%, 0% 50%);
  position: relative;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.deepseek-robot .circuit-lines {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    linear-gradient(45deg, transparent 0%, rgba(255, 255, 255, 0.2) 50%, transparent 100%),
    linear-gradient(-45deg, transparent 0%, rgba(255, 255, 255, 0.2) 50%, transparent 100%);
  background-size: 20px 20px;
  animation: circuitFlow 2s linear infinite;
}

.deepseek-robot .robot-arm {
  width: 20px;
  height: 70px;
  background: rgba(255, 255, 255, 0.1);
  clip-path: polygon(50% 0%, 100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%);
  position: absolute;
  top: 40px;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.deepseek-robot .robot-arm.left {
  left: -35px;
  transform: rotate(-20deg);
}

.deepseek-robot .robot-arm.right {
  right: -35px;
  transform: rotate(20deg);
}

/* Energy Aura Effects */
.energy-aura {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 20px;
  background: radial-gradient(circle at center, transparent 0%, rgba(255, 255, 255, 0.1) 50%, transparent 100%);
  animation: auraPulse 3s ease-in-out infinite;
}

/* Animations */
@keyframes blink {
  0%, 48%, 52%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

@keyframes circuitFlow {
  0% { background-position: 0 0; }
  100% { background-position: 20px 20px; }
}

@keyframes auraPulse {
  0%, 100% { transform: scale(1); opacity: 0.5; }
  50% { transform: scale(1.1); opacity: 0.8; }
}
</style> 