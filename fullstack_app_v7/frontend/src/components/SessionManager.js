import React, { useState } from 'react';
import {
  Box,
  TextField,
  Button,
  Typography,
  Paper,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import api from '../services/api';

const SessionManager = ({ currentSession, setCurrentSession }) => {
  const [newSessionName, setNewSessionName] = useState('');
  const [sessionId, setSessionId] = useState('');
  const [openDialog, setOpenDialog] = useState(false);

  const handleCreateSession = async () => {
    try {
      const session = await api.createSession(newSessionName);
      setCurrentSession(session);
      setNewSessionName('');
      setOpenDialog(false);
    } catch (error) {
      console.error('Error creating session:', error);
    }
  };

  const handleLoadSession = async () => {
    try {
      const session = await api.getSession(sessionId);
      setCurrentSession(session);
      setSessionId('');
    } catch (error) {
      console.error('Error loading session:', error);
    }
  };

  return (
    <Box sx={{ mb: 3 }}>
      <Paper sx={{ p: 2 }}>
        <Typography variant="h6" gutterBottom>
          Chat Session
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
          <Button
            variant="contained"
            color="primary"
            onClick={() => setOpenDialog(true)}
          >
            New Session
          </Button>
          <TextField
            label="Session ID"
            value={sessionId}
            onChange={(e) => setSessionId(e.target.value)}
            size="small"
          />
          <Button
            variant="outlined"
            onClick={handleLoadSession}
            disabled={!sessionId}
          >
            Load Session
          </Button>
        </Box>
      </Paper>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>Create New Session</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Session Name"
            fullWidth
            value={newSessionName}
            onChange={(e) => setNewSessionName(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button onClick={handleCreateSession} variant="contained">
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default SessionManager; 