<template>
  <div class="register-container">
    <div class="register-box glass-effect">
      <h2>Register</h2>
      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            v-model="username"
            required
            placeholder="Choose a username"
          />
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input
            type="email"
            id="email"
            v-model="email"
            required
            placeholder="Enter your email"
          />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="password"
            required
            placeholder="Choose a password"
          />
        </div>
        <div class="form-group">
          <label for="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            required
            placeholder="Confirm your password"
          />
        </div>
        <div v-if="error" class="error-message">{{ error }}</div>
        <button type="submit" :disabled="!isValid || isLoading" class="submit-button">
          {{ isLoading ? "Registering..." : "Register" }}
        </button>
      </form>
      <p class="switch-form">
        Already have an account?
        <a href="#" @click.prevent="$emit('show-login')">Login here</a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import authService from '../services/authService';

const emit = defineEmits(['register-success', 'show-login']);

const username = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const error = ref('');
const isLoading = ref(false);

const isValid = computed(() => {
  return (
    username.value.trim() &&
    email.value.trim() &&
    password.value.trim() &&
    confirmPassword.value.trim() &&
    password.value === confirmPassword.value
  );
});

const handleRegister = async () => {
  if (!isValid.value) return;

  error.value = '';
  isLoading.value = true;

  try {
    const response = await authService.register(username.value, password.value, email.value);
    if (response.token) {
      emit('register-success');
    } else {
      error.value = response.message || 'Registration failed. Please try again.';
    }
  } catch (err) {
    error.value = err.message || 'Registration failed. Please try again.';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 1rem;
  background: var(--background-color);
}

.register-box {
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

.register-form {
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