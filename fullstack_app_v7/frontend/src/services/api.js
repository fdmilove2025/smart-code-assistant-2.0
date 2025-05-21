import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/code';

// Create axios instance with default config
const axiosInstance = axios.create({
  baseURL: API_BASE_URL
});

// Add request interceptor to add auth token
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

const api = {
  createSession: async (name) => {
    const response = await axiosInstance.post(`/session/${name}`);
    return response.data;
  },

  getSession: async (id) => {
    const response = await axiosInstance.get(`/history/${id}`);
    return response.data;
  },

  sendMessage: async (sessionId, message) => {
    const response = await axiosInstance.post(`/${sessionId}`, {
      message
    });
    return response.data;
  },

  sendMessageString: async (sessionId, message) => {
    const response = await axiosInstance.post(`/string/${sessionId}`, {
      message
    });
    return response.data;
  }
};

export default api; 