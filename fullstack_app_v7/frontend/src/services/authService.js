import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

const authService = {
    async register(username, password, email) {
        try {
            const response = await axios.post(`${API_URL}/register`, {
                username,
                password,
                email
            });
            
            if (response.data && response.data.token) {
                localStorage.setItem('token', response.data.token);
                localStorage.setItem('user', JSON.stringify({ username, email }));
                return response.data;
            } else {
                throw new Error(response.data?.message || 'Registration failed');
            }
        } catch (error) {
            console.error('Registration error:', error);
            throw error.response?.data || { message: 'Registration failed' };
        }
    },

    async login(username, password) {
        try {
            console.log('Attempting login with:', { username });
            const response = await axios.post(`${API_URL}/login`, {
                username,
                password
            });
            
            console.log('Raw login response:', response);
            console.log('Login response data:', response.data);
            console.log('Response headers:', response.headers);
            
            // Check if the token is in the response data
            const token = response.data.token;
            if (token) {
                console.log('Token received:', token);
                try {
                    localStorage.setItem('token', token);
                    // Store user data with email from response
                    const userData = {
                        username: username,
                        email: response.data.email || response.data.user?.email || ''
                    };
                    localStorage.setItem('user', JSON.stringify(userData));
                    
                    // Verify storage immediately
                    const storedToken = localStorage.getItem('token');
                    console.log('Token stored in localStorage:', storedToken);
                    
                    if (!storedToken) {
                        console.error('Token storage failed - localStorage.getItem returned null');
                        throw new Error('Failed to store token');
                    }
                    
                    return { token, username, email: userData.email };
                } catch (storageError) {
                    console.error('Error storing in localStorage:', storageError);
                    throw new Error('Failed to store authentication data');
                }
            } else {
                console.error('Invalid response format:', response.data);
                throw new Error('Invalid response from server');
            }
        } catch (error) {
            console.error('Login error:', error);
            throw error.response?.data || { message: 'Login failed' };
        }
    },

    logout() {
        console.log('Logging out, clearing localStorage');
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        console.log('localStorage cleared, token:', localStorage.getItem('token'));
    },

    getCurrentUser() {
        const userStr = localStorage.getItem('user');
        console.log('Getting current user from localStorage:', userStr);
        if (userStr) {
            try {
                return JSON.parse(userStr);
            } catch (e) {
                console.error('Error parsing user data:', e);
                return null;
            }
        }
        return null;
    },

    getToken() {
        const token = localStorage.getItem('token');
        console.log('Getting token from localStorage:', token ? 'Present' : 'Missing');
        return token;
    },

    isAuthenticated() {
        const hasToken = !!this.getToken();
        console.log('Checking authentication status:', hasToken ? 'Authenticated' : 'Not authenticated');
        return hasToken;
    }
};

export default authService; 