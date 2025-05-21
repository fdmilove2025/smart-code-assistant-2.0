import React, { useState } from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { Container, Box } from '@mui/material';
import ChatInterface from './components/ChatInterface';
import SessionManager from './components/SessionManager';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#90caf9',
    },
    secondary: {
      main: '#f48fb1',
    },
  },
});

function App() {
  const [currentSession, setCurrentSession] = useState(null);

  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <Box sx={{ my: 4 }}>
          <SessionManager 
            currentSession={currentSession} 
            setCurrentSession={setCurrentSession} 
          />
          <ChatInterface currentSession={currentSession} />
        </Box>
      </Container>
    </ThemeProvider>
  );
}

export default App; 