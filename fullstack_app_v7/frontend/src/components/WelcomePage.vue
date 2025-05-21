<template>
  <div class="welcome-page">
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
      <div class="logo-container">
        <div class="logo">
          <span class="logo-icon">⚡</span>
        </div>
        <h1 class="title">Code Reaper</h1>
      </div>
      
      <div class="description">
        <p class="tagline">Your AI-powered coding companion</p>
        <p class="subtitle">Harness the power of artificial intelligence to transform your code</p>
      </div>
      
      <div class="features">
        <div class="feature">
          <span class="feature-icon">🤖</span>
          <span class="feature-text">Smart Code Analysis</span>
        </div>
        <div class="feature">
          <span class="feature-icon">⚡</span>
          <span class="feature-text">Lightning Fast Responses</span>
        </div>
        <div class="feature">
          <span class="feature-icon">🎯</span>
          <span class="feature-text">Precise Solutions</span>
        </div>
      </div>
      
      <button class="start-button" @click="$emit('start')">
        <span class="button-text">Start Reaping</span>
        <span class="button-icon">→</span>
      </button>
    </div>
  </div>
</template>

<script setup>
defineEmits(['start']);
</script>

<style scoped>
.welcome-page {
  height: 100vh;
  width: 100vw;
  background: var(--background-color);
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.background-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
  overflow: hidden;
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
  transform-origin: center;
  perspective: 1000px;
}

.grid-overlay::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at center, transparent 0%, var(--background-color) 70%);
  animation: gridFade 8s ease-in-out infinite;
}

.particles {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at center, transparent 0%, var(--background-color) 70%);
  transform-style: preserve-3d;
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
  transform-style: preserve-3d;
}

.particles::after {
  animation-delay: -15s;
  opacity: 0.5;
  transform: translateZ(-50px);
}

.glow-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  transform-style: preserve-3d;
}

.glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.3;
  animation: glowPulse 8s ease-in-out infinite;
  transform-style: preserve-3d;
}

.glow-1 {
  width: 400px;
  height: 400px;
  background: var(--primary-color);
  top: -100px;
  left: -100px;
  animation-delay: 0s;
  transform: translateZ(-100px);
}

.glow-2 {
  width: 300px;
  height: 300px;
  background: #4CAF50;
  bottom: -50px;
  right: -50px;
  animation-delay: -2s;
  transform: translateZ(-50px);
}

.glow-3 {
  width: 200px;
  height: 200px;
  background: #FF6B6B;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) translateZ(-25px);
  animation-delay: -4s;
}

.content {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 2rem;
  max-width: 800px;
}

.logo-container {
  margin-bottom: 2rem;
  animation: logoAppear 1s ease-out;
}

.logo {
  width: 120px;
  height: 120px;
  margin: 0 auto 1rem;
  background: var(--primary-gradient);
  border-radius: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 0 40px rgba(108, 99, 255, 0.3);
  transform: rotate(45deg);
  animation: logoRotate 10s linear infinite;
  position: relative;
  overflow: hidden;
}

.logo::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent 0%,
    rgba(255, 255, 255, 0.1) 50%,
    transparent 100%
  );
  animation: logoShine 3s linear infinite;
}

.logo::after {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 30px;
  padding: 2px;
  background: linear-gradient(45deg, var(--primary-color), transparent);
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  animation: logoBorder 3s linear infinite;
}

.logo-icon {
  font-size: 3rem;
  transform: rotate(-45deg);
}

.title {
  font-size: 3.5rem;
  font-weight: 800;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  margin: 0;
  letter-spacing: -2px;
  animation: titleAppear 1s ease-out 0.3s both;
}

.description {
  margin-bottom: 3rem;
  animation: fadeIn 1s ease-out 0.6s both;
}

.tagline {
  font-size: 1.5rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 0.5rem;
}

.subtitle {
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.6);
}

.features {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin-bottom: 3rem;
  animation: fadeIn 1s ease-out 0.9s both;
}

.feature {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.feature::before {
  content: '';
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

.feature:hover::before {
  left: 100%;
}

.feature:hover {
  transform: translateY(-2px) scale(1.02);
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(108, 99, 255, 0.3);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  font-size: 1.2rem;
}

.feature-text {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.95rem;
}

.start-button {
  background: var(--primary-gradient);
  border: none;
  padding: 1rem 2.5rem;
  border-radius: 12px;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin: 0 auto;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(108, 99, 255, 0.3);
  animation: buttonAppear 1s ease-out 1.2s both;
  position: relative;
  overflow: hidden;
}

.start-button::before {
  content: '';
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

.start-button:hover::before {
  left: 100%;
}

.start-button:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 6px 25px rgba(108, 99, 255, 0.4);
}

.button-icon {
  transition: transform 0.3s ease;
}

.start-button:hover .button-icon {
  transform: translateX(4px);
}

@keyframes gridMove {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(50px);
  }
}

@keyframes particleFloat {
  0% {
    transform: translateY(0) rotate(0deg);
  }
  100% {
    transform: translateY(-50px) rotate(360deg);
  }
}

@keyframes glowPulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.4;
  }
}

@keyframes logoAppear {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes logoRotate {
  from {
    transform: rotate(45deg);
  }
  to {
    transform: rotate(405deg);
  }
}

@keyframes titleAppear {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
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

@keyframes buttonAppear {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes gridFade {
  0%, 100% { opacity: 0.3; }
  50% { opacity: 0.5; }
}

@keyframes logoShine {
  0% { transform: translateX(-100%) rotate(45deg); }
  100% { transform: translateX(100%) rotate(45deg); }
}

@keyframes logoBorder {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 