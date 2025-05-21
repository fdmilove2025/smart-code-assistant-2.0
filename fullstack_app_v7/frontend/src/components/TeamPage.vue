<template>
  <div class="team-page">
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
      <h1 class="title">The Code Reapers</h1>
      <p class="subtitle">Meet the talented team behind the Code Reaper</p>

      <div class="team-grid">
        <div class="team-section">
          <h2 class="section-title">
            <span class="icon">⚡</span>
            Backend Development
          </h2>
          <div class="members">
            <div class="member-card" v-for="member in backendMembers" :key="member.name">
              <div class="member-header">
                <div class="member-avatar">
                  <span class="avatar-icon">{{ member.icon }}</span>
                </div>
                <div class="member-title">
                  <h3 class="member-name">{{ member.name }}</h3>
                  <p class="member-role">{{ member.role }}</p>
                </div>
              </div>
              <div class="member-skills">
                <div class="skill-tag" v-for="skill in member.skills" :key="skill">{{ skill }}</div>
              </div>
              <p class="member-bio">{{ member.bio }}</p>
            </div>
          </div>
        </div>

        <div class="team-section">
          <h2 class="section-title">
            <span class="icon">🎨</span>
            Frontend Development
          </h2>
          <div class="members">
            <div class="member-card" v-for="member in frontendMembers" :key="member.name">
              <div class="member-header">
                <div class="member-avatar">
                  <span class="avatar-icon">{{ member.icon }}</span>
                </div>
                <div class="member-title">
                  <h3 class="member-name">{{ member.name }}</h3>
                  <p class="member-role">{{ member.role }}</p>
                </div>
              </div>
              <div class="member-skills">
                <div class="skill-tag" v-for="skill in member.skills" :key="skill">{{ skill }}</div>
              </div>
              <p class="member-bio">{{ member.bio }}</p>
            </div>
          </div>
        </div>
      </div>

      <button class="back-button" @click="$emit('back')">
        <span class="button-icon">←</span>
        <span class="button-text">Back to Chat</span>
      </button>
    </div>
  </div>
</template>

<script setup>
const backendMembers = [
  { 
    name: 'Isaac Chan', 
    role: 'Backend Developer', 
    icon: '⚡',
    skills: ['Java', 'Spring Boot', 'AI Integration', 'System Architecture'],
    bio: 'Experienced in building scalable backend systems and integrating AI solutions. Passionate about creating efficient and reliable applications.'
  },
  { 
    name: 'Henry Lau', 
    role: 'Backend Developer', 
    icon: '⚡',
    skills: ['Java', 'Spring Boot', 'Database Design', 'API Development'],
    bio: 'Specialized in database optimization and RESTful API development. Focused on delivering high-performance solutions.'
  }
];

const frontendMembers = [
  { 
    name: 'Gaukhar', 
    role: 'Frontend Developer', 
    icon: '🎨',
    skills: ['Vue.js', 'UI/UX Design', 'Responsive Design', 'State Management'],
    bio: 'Expert in creating intuitive and beautiful user interfaces. Committed to delivering exceptional user experiences.'
  },
  { 
    name: 'Marcus Liu', 
    role: 'Frontend Developer', 
    icon: '🎨',
    skills: ['Vue.js', 'JavaScript', 'CSS3', 'Component Architecture'],
    bio: 'Skilled in modern frontend development practices and component-based architecture. Focused on code quality and maintainability.'
  }
];

defineEmits(['back']);
</script>

<style scoped>
.team-page {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--background-color);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  z-index: 1000;
  overflow-y: auto;
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
  color: var(--text-color);
  margin-bottom: 1rem;
  text-align: center;
}

.subtitle {
  font-size: 1.2rem;
  color: var(--text-color);
  opacity: 0.8;
  margin-bottom: 3rem;
  text-align: center;
}

.team-grid {
  display: flex;
  flex-direction: column;
  gap: 3rem;
  margin-bottom: 3rem;
}

.team-section {
  flex: 1;
  background: var(--glass-effect);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 2rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 2rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.section-title .icon {
  color: var(--primary-color);
  font-size: 1.8rem;
}

.members {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.member-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 1.5rem;
  transition: all 0.3s ease;
  text-align: left;
}

.member-card:hover {
  border-color: var(--primary-color);
  background: rgba(255, 255, 255, 0.08);
  transform: translateY(-4px);
}

.member-header {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.member-avatar {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  background: rgba(46, 125, 50, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid var(--primary-color);
}

.avatar-icon {
  font-size: 1.8rem;
  color: var(--primary-color);
}

.member-title {
  text-align: left;
}

.member-name {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 0.25rem;
}

.member-role {
  font-size: 0.9rem;
  color: var(--text-color);
  opacity: 0.8;
}

.member-skills {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.skill-tag {
  background: rgba(46, 125, 50, 0.1);
  color: var(--primary-color);
  padding: 0.25rem 0.75rem;
  border-radius: 16px;
  font-size: 0.8rem;
  font-weight: 500;
}

.member-bio {
  font-size: 0.9rem;
  color: var(--text-color);
  opacity: 0.7;
  line-height: 1.6;
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

.button-icon {
  transition: transform 0.3s ease;
}

.back-button:hover .button-icon {
  transform: translateX(-4px);
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

@media (max-width: 768px) {
  .members {
    grid-template-columns: 1fr;
  }
  
  .member-header {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }
  
  .member-title {
    text-align: center;
  }
}
</style> 