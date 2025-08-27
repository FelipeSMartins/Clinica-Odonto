import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { CssBaseline } from '@mui/material';
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Pacientes from './pages/Pacientes';
import Dentistas from './pages/Dentistas';
import Consultas from './pages/Consultas';
import Usuarios from './pages/Usuarios';
import PlanosSaude from './pages/PlanosSaude';
import NotFound from './pages/NotFound';
import Unauthorized from './pages/Unauthorized';

// Tema personalizado
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <Routes>
            {/* Rota pública de login */}
            <Route path="/login" element={<Login />} />
            
            {/* Rota de acesso negado */}
            <Route path="/unauthorized" element={<Unauthorized />} />
            
            {/* Rotas protegidas */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <Layout>
                    <Dashboard />
                  </Layout>
                </ProtectedRoute>
              }
            />
            
            {/* Rota de pacientes */}
            <Route
              path="/pacientes"
              element={
                <ProtectedRoute>
                  <Layout>
                    <Pacientes />
                  </Layout>
                </ProtectedRoute>
              }
            />
            
            <Route
              path="/dentistas"
              element={
                <ProtectedRoute requiredRoles={['ADMIN', 'RECEPCIONISTA']}>
                  <Layout>
                    <Dentistas />
                  </Layout>
                </ProtectedRoute>
              }
            />
            
            <Route
              path="/consultas"
              element={
                <ProtectedRoute>
                  <Layout>
                    <Consultas />
                  </Layout>
                </ProtectedRoute>
              }
            />
            
            <Route
              path="/usuarios"
              element={
                <ProtectedRoute requiredRoles={['ADMIN']}>
                  <Layout>
                    <Usuarios />
                  </Layout>
                </ProtectedRoute>
              }
            />
            
            <Route
              path="/planos-saude"
              element={
                <ProtectedRoute requiredRoles={['ADMIN', 'RECEPCIONISTA']}>
                  <Layout>
                    <PlanosSaude />
                  </Layout>
                </ProtectedRoute>
              }
            />
            
            {/* Redirecionamento da raiz para dashboard */}
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            
            {/* Rota 404 */}
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
