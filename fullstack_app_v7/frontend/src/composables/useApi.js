import { ref } from 'vue'
import axios from 'axios'
import authService from '../services/authService'

const API_URL = 'http://localhost:8080/api/code'

export function useApi() {
  // Store sessions and messages in memory
  const sessions = ref({})
  
  const axiosInstance = axios.create({
    baseURL: API_URL,
  })

  axiosInstance.interceptors.request.use(
    (config) => {
      const token = authService.getToken()
      console.log('Current token:', token ? 'Present' : 'Missing')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
        console.log('Authorization header set:', config.headers.Authorization)
      } else {
        console.warn('No token found in localStorage')
      }
      return config
    },
    (error) => {
      console.error('Request interceptor error:', error)
      return Promise.reject(error)
    }
  )

  // Add response interceptor to handle 403 errors
  axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 403) {
        console.error('Authentication error (403):', error.response.data)
        // Only logout and reload if it's not a profile update request
        if (!error.config.url.includes('/users/profile')) {
          authService.logout()
          window.location.reload()
        }
      }
      return Promise.reject(error)
    }
  )

  const fetchUserSessions = async () => {
    try {
      console.log('Fetching user sessions')
      const response = await axiosInstance.get('/sessions')
      const userSessions = response.data
      
      // Always clear existing sessions before updating with new ones
      sessions.value = {}
      
      // Update the sessions ref with the fetched sessions
      userSessions.forEach(session => {
        sessions.value[session.sessionId] = session
      })
      
      console.log('User sessions fetched:', userSessions)
      return userSessions
    } catch (error) {
      console.error('Error fetching user sessions:', error)
      if (error.response) {
        console.error('Response data:', error.response.data)
        console.error('Response status:', error.response.status)
      }
      // Clear sessions on error to ensure clean state
      sessions.value = {}
      throw error
    }
  }

  const sendMessage = async (message, sessionId, model = 'qwen') => {
    try {
      console.log(`Sending message to session ${sessionId}: ${message} with model: ${model}`)
      
      // Ensure sessionId is a number
      const numericSessionId = Number(sessionId);
      
      // Send the message to the backend
      const response = await axiosInstance.post(`/${numericSessionId}`, {
        message: message,
        role: 'user',
        createdDateTime: new Date().toISOString(),
        model: model
      })
      
      console.log('Response received:', response.data)
      
      // Ensure we have a valid response with the expected format
      if (!response.data || !response.data.message) {
        throw new Error('Invalid response format from server');
      }
      
      return response.data;
    } catch (error) {
      console.error('Error sending message:', error)
      if (error.response) {
        console.error('Response data:', error.response.data)
        console.error('Response status:', error.response.status)
      }
      throw error
    }
  }

  const createSession = async (name, botType = 'code_review') => {
    try {
      console.log(`Creating new session: ${name} with bot type: ${botType}`);
      const response = await axiosInstance.post(`/session/${name}/${botType}`);
      const session = response.data;
      sessions.value[session.sessionId] = session;
      console.log('Session created:', session);
      return session;
    } catch (error) {
      console.error('Error creating session:', error);
      if (error.response) {
        console.error('Response data:', error.response.data);
        console.error('Response status:', error.response.status);
      }
      throw error;
    }
  };

  const getSession = async (sessionId) => {
    try {
      console.log(`Getting session: ${sessionId}`)
      const response = await axiosInstance.get(`/history/${sessionId}`)
      const session = response.data
      sessions.value[session.sessionId] = session
      console.log('Session retrieved:', session)
      
      return session
    } catch (error) {
      console.error('Error getting session:', error)
      if (error.response) {
        console.error('Response data:', error.response.data)
        console.error('Response status:', error.response.status)
      }
      throw error
    }
  }

  const updateSessionMessages = async (sessionId, messages) => {
    try {
      console.log(`Updating messages for session ${sessionId}:`, messages)
      const session = sessions.value[sessionId]
      if (session) {
        session.messages = messages
        sessions.value[sessionId] = { ...session }
        console.log('Session messages updated:', session)
      }
    } catch (error) {
      console.error('Error updating session messages:', error)
      throw error
    }
  }

  const updateSessionName = (sessionId, newName) => {
    if (sessions.value[sessionId]) {
      sessions.value[sessionId] = {
        ...sessions.value[sessionId],
        sessionName: newName
      }
    }
  }

  const updateSession = async (session) => {
    try {
      console.log(`Updating session: ${session.sessionId}`)
      const response = await axiosInstance.put(`/session/${session.sessionId}`, session)
      const updatedSession = response.data
      sessions.value[updatedSession.sessionId] = updatedSession
      console.log('Session updated:', updatedSession)
      
      return updatedSession
    } catch (error) {
      console.error('Error updating session:', error)
      if (error.response) {
        console.error('Response data:', error.response.data)
        console.error('Response status:', error.response.status)
      }
      throw error
    }
  }

  const getAvailableBotTypes = async () => {
    try {
      const response = await axios.get(`${API_URL}/bot-types`, {
        headers: {
          Authorization: `Bearer ${authService.getToken()}`
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching bot types:', error);
      throw error;
    }
  };
  
  const createNewSession = async (name, botType) => {
    try {
      const response = await axios.post(
        `${API_URL}/session/${name}/${botType}`,
        {},
        { headers: { Authorization: `Bearer ${authService.getToken()}` } }
      );
      return response.data;
    } catch (error) {
      console.error('Error creating new session:', error);
      throw error;
    }
  };
  
  const getSessionHistory = async (sessionId) => {
    try {
      const response = await axios.get(`${API_URL}/history/${sessionId}`, {
        headers: { Authorization: `Bearer ${authService.getToken()}` }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching session history:', error);
      throw error;
    }
  };
  
  const getUserSessions = async () => {
    try {
      const response = await axios.get(`${API_URL}/sessions`, {
        headers: { Authorization: `Bearer ${authService.getToken()}` }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching user sessions:', error);
      throw error;
    }
  };
  
  const setToken = (newToken) => {
    authService.setToken(newToken);
  };
  
  const clearToken = () => {
    authService.logout();
  };

  const deleteSession = async (sessionId) => {
    try {
      await axiosInstance.delete(`/sessions/${sessionId}`);
    } catch (error) {
      console.error('Error deleting session:', error);
      throw error;
    }
  };

  return {
    sendMessage,
    createSession,
    getSession,
    updateSessionMessages,
    updateSessionName,
    updateSession,
    sessions,
    fetchUserSessions,
    getAvailableBotTypes,
    createNewSession,
    getSessionHistory,
    getUserSessions,
    setToken,
    clearToken,
    deleteSession,
  }
} 