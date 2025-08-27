import React from 'react';
import {
  Box,
  Typography,
  Button,
  Container,
  Paper,
  Alert,
} from '@mui/material';
import { Home, ArrowBack, Block } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Unauthorized = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  const handleGoHome = () => {
    navigate('/dashboard');
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <Container maxWidth="md">
      <Box
        sx={{
          minHeight: '100vh',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <Paper
          elevation={3}
          sx={{
            p: 6,
            textAlign: 'center',
            maxWidth: 500,
            width: '100%',
          }}
        >
          <Block
            sx={{
              fontSize: '4rem',
              color: 'error.main',
              mb: 2,
            }}
          />
          
          <Typography variant="h4" component="h1" gutterBottom>
            Acesso Negado
          </Typography>
          
          <Typography
            variant="body1"
            color="textSecondary"
            sx={{ mb: 3 }}
          >
            Você não tem permissão para acessar esta página.
          </Typography>
          
          <Alert severity="warning" sx={{ mb: 4, textAlign: 'left' }}>
            <Typography variant="body2">
              <strong>Usuário:</strong> {user?.nome}<br />
              <strong>Tipo:</strong> {user?.tipo}<br />
              <strong>Email:</strong> {user?.email}
            </Typography>
          </Alert>
          
          <Typography
            variant="body2"
            color="textSecondary"
            sx={{ mb: 4 }}
          >
            Se você acredita que deveria ter acesso a esta página, 
            entre em contato com o administrador do sistema.
          </Typography>
          
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
            <Button
              variant="contained"
              startIcon={<Home />}
              onClick={handleGoHome}
            >
              Ir para Dashboard
            </Button>
            
            <Button
              variant="outlined"
              startIcon={<ArrowBack />}
              onClick={handleGoBack}
            >
              Voltar
            </Button>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default Unauthorized;