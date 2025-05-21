import React, { useState, useRef, useEffect } from 'react';
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  Avatar,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Divider,
} from '@mui/material';
import { Send as SendIcon } from '@mui/icons-material';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import api from '../services/api';

const ChatInterface = ({ currentSession }) => {
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    if (currentSession?.messages) {
      setMessages(currentSession.messages);
    }
  }, [currentSession]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSendMessage = async () => {
    if (!message.trim() || !currentSession) return;

    const userMessage = {
      message: message.trim(),
      role: 'user',
      createdDateTime: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setMessage('');

    try {
      const response = await api.sendMessage(currentSession.sessionId, message.trim());
      setMessages((prev) => [...prev, response]);
    } catch (error) {
      console.error('Error sending message:', error);
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      handleSendMessage();
    }
  };

  const renderMessage = (msg) => {
    const isUser = msg.role === 'user';
    const hasCode = msg.message.includes('```');

    if (hasCode) {
      const parts = msg.message.split('```');
      return parts.map((part, index) => {
        if (index % 2 === 1) {
          return (
            <SyntaxHighlighter
              key={index}
              language="javascript"
              style={vscDarkPlus}
              customStyle={{ margin: '8px 0', borderRadius: '4px' }}
            >
              {part}
            </SyntaxHighlighter>
          );
        }
        return <Typography key={index}>{part}</Typography>;
      });
    }

    return <Typography>{msg.message}</Typography>;
  };

  return (
    <Paper sx={{ height: '70vh', display: 'flex', flexDirection: 'column' }}>
      <Box sx={{ p: 2, borderBottom: 1, borderColor: 'divider' }}>
        <Typography variant="h6">
          {currentSession ? currentSession.sessionName : 'No Active Session'}
        </Typography>
      </Box>

      <List sx={{ flex: 1, overflow: 'auto', p: 2 }}>
        {messages.map((msg, index) => (
          <React.Fragment key={index}>
            <ListItem
              sx={{
                flexDirection: msg.role === 'user' ? 'row-reverse' : 'row',
                alignItems: 'flex-start',
              }}
            >
              <ListItemAvatar>
                <Avatar
                  sx={{
                    bgcolor: msg.role === 'user' ? 'primary.main' : 'secondary.main',
                  }}
                >
                  {msg.role === 'user' ? 'U' : 'A'}
                </Avatar>
              </ListItemAvatar>
              <ListItemText
                primary={renderMessage(msg)}
                secondary={new Date(msg.createdDateTime).toLocaleString()}
                sx={{
                  textAlign: msg.role === 'user' ? 'right' : 'left',
                }}
              />
            </ListItem>
            <Divider variant="inset" component="li" />
          </React.Fragment>
        ))}
        <div ref={messagesEndRef} />
      </List>

      <Box sx={{ p: 2, borderTop: 1, borderColor: 'divider' }}>
        <Box sx={{ display: 'flex', gap: 1 }}>
          <TextField
            fullWidth
            multiline
            maxRows={4}
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Type your message..."
            disabled={!currentSession}
          />
          <Button
            variant="contained"
            color="primary"
            onClick={handleSendMessage}
            disabled={!message.trim() || !currentSession}
          >
            <SendIcon />
          </Button>
        </Box>
      </Box>
    </Paper>
  );
};

export default ChatInterface; 