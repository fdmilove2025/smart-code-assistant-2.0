<template>
  <div class="login-container">
    <div class="login-box glass-effect">
      <h2>Login</h2>
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            v-model="username"
            required
            placeholder="Enter your username"
          />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="password"
            required
            placeholder="Enter your password"
          />
        </div>
        <div v-if="error" class="error-message">{{ error }}</div>
        <button type="submit" :disabled="!isValid || isLoading" class="submit-button">
          {{ isLoading ? "Logging in..." : "Login" }}
        </button>
      </form>
      <p class="switch-form">
        Don't have an account?
        <a href="#" @click.prevent="$emit('show-register')">Register here</a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import authService from '../services/authService';

const emit = defineEmits(['login-success', 'show-register']);

const username = ref('');
const password = ref('');
const error = ref('');
const isLoading = ref(false);

const isValid = computed(() => {
  return username.value.trim() && password.value.trim();
});

const handleLogin = async () => {
  if (!isValid.value) return;

  error.value = '';
  isLoading.value = true;

  try {
    console.log('Starting login process...');
    const response = await authService.login(username.value, password.value);
    console.log('Login response received:', response);
    
    if (response && response.token) {
      console.log('Login successful, token present in response');
      // Verify token is stored
      const storedToken = localStorage.getItem('token');
      console.log('Token in localStorage after login:', storedToken ? 'Present' : 'Missing');
      
      if (storedToken) {
        console.log('Login successful, emitting success event');
        emit('login-success');
      } else {
        console.error('Token not stored in localStorage despite successful login');
        error.value = 'Login failed: Token not stored properly';
        // Clear any partial data
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    } else {
      console.error('Login failed: No token in response');
      error.value = 'Login failed. Please try again.';
    }
  } catch (err) {
    console.error('Login error caught:', err);
    error.value = err.message || 'Login failed. Please try again.';
    // Clear any partial data
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  } finally {
    console.log('Login process completed');
    isLoading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 1rem;
  background: var(--background-color);
}

.login-box {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--text-color);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  color: var(--text-color);
  font-weight: 500;
}

input {
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  background: var(--input-background);
  color: var(--text-color);
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

input:focus {
  outline: none;
  border-color: var(--primary-color);
}

.error-message {
  color: #ff4444;
  text-align: center;
  font-size: 0.9rem;
}

.submit-button {
  padding: 0.75rem;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover:not(:disabled) {
  background: var(--primary-color-hover);
}

.submit-button:disabled {
  background: var(--disabled-color);
  cursor: not-allowed;
}

.switch-form {
  text-align: center;
  margin-top: 1.5rem;
  color: var(--text-color);
}

.switch-form a {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
}

.switch-form a:hover {
  text-decoration: underline;
}
</style> 