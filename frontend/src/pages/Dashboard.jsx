import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  CircularProgress,
  Alert,
  Paper,
  Avatar,
} from '@mui/material';
import {
  People,
  PersonAdd,
  MedicalServices,
  CalendarToday,
  CheckCircle,
  Schedule,
  AttachMoney,
  Group,
} from '@mui/icons-material';
import dashboardService from '../services/dashboardService';
import { useAuth } from '../contexts/AuthContext';

const Dashboard = () => {
  const [metrics, setMetrics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    loadMetrics();
  }, []);

  const loadMetrics = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await dashboardService.getMetrics();
      setMetrics(data);
    } catch (error) {
      console.error('Erro ao carregar métricas:', error);
      setError('Erro ao carregar as métricas do dashboard.');
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value || 0);
  };

  const MetricCard = ({ title, value, icon, color = 'primary', subtitle }) => (
    <Card sx={{ height: '100%' }}>
      <CardContent>
        <Box display="flex" alignItems="center" justifyContent="space-between">
          <Box>
            <Typography color="textSecondary" gutterBottom variant="body2">
              {title}
            </Typography>
            <Typography variant="h4" component="div" color={color}>
              {value}
            </Typography>
            {subtitle && (
              <Typography variant="body2" color="textSecondary">
                {subtitle}
              </Typography>
            )}
          </Box>
          <Avatar
            sx={{
              backgroundColor: `${color}.main`,
              width: 56,
              height: 56,
            }}
          >
            {icon}
          </Avatar>
        </Box>
      </CardContent>
    </Card>
  );

  if (loading) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="400px"
      >
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mb: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>
      
      <Typography variant="body1" color="textSecondary" sx={{ mb: 3 }}>
        Bem-vindo(a), {user?.nome}! Aqui estão as métricas principais da clínica.
      </Typography>

      <Grid container spacing={3}>
        {/* Total de Pacientes */}
        <Grid item xs={12} sm={6} md={3}>
          <MetricCard
            title="Total de Pacientes"
            value={metrics?.totalPacientes || 0}
            icon={<People />}
            color="primary"
          />
        </Grid>

        {/* Pacientes Cadastrados no Mês */}
        <Grid item xs={12} sm={6} md={3}>
          <MetricCard
            title="Novos Pacientes"
            value={metrics?.pacientesCadastradosNoMes || 0}
            icon={<PersonAdd />}
            color="success"
            subtitle="Este mês"
          />
        </Grid>

        {/* Total de Dentistas */}
        <Grid item xs={12} sm={6} md={3}>
          <MetricCard
            title="Dentistas Ativos"
            value={metrics?.totalDentistas || 0}
            icon={<MedicalServices />}
            color="info"
          />
        </Grid>

        {/* Total de Usuários */}
        <Grid item xs={12} sm={6} md={3}>
          <MetricCard
            title="Usuários do Sistema"
            value={metrics?.totalUsuarios || 0}
            icon={<Group />}
            color="warning"
          />
        </Grid>

        {/* Consultas Hoje */}
        <Grid item xs={12} sm={6} md={4}>
          <MetricCard
            title="Consultas Hoje"
            value={metrics?.consultasHoje || 0}
            icon={<CalendarToday />}
            color="primary"
          />
        </Grid>

        {/* Consultas Agendadas Hoje */}
        <Grid item xs={12} sm={6} md={4}>
          <MetricCard
            title="Agendadas Hoje"
            value={metrics?.consultasAgendadasHoje || 0}
            icon={<Schedule />}
            color="warning"
          />
        </Grid>

        {/* Consultas Concluídas Hoje */}
        <Grid item xs={12} sm={6} md={4}>
          <MetricCard
            title="Concluídas Hoje"
            value={metrics?.consultasConcluidasHoje || 0}
            icon={<CheckCircle />}
            color="success"
          />
        </Grid>

        {/* Consultas no Mês */}
        <Grid item xs={12} sm={6}>
          <MetricCard
            title="Consultas no Mês"
            value={metrics?.consultasNoMes || 0}
            icon={<CalendarToday />}
            color="info"
            subtitle="Total mensal"
          />
        </Grid>

        {/* Faturamento Mensal */}
        <Grid item xs={12} sm={6}>
          <MetricCard
            title="Faturamento Mensal"
            value={formatCurrency(metrics?.faturamentoMensal)}
            icon={<AttachMoney />}
            color="success"
            subtitle="Receita do mês"
          />
        </Grid>
      </Grid>

      {/* Resumo Rápido */}
      <Paper sx={{ p: 3, mt: 3 }}>
        <Typography variant="h6" gutterBottom>
          Resumo Rápido
        </Typography>
        <Grid container spacing={2}>
          <Grid item xs={12} md={6}>
            <Typography variant="body2" color="textSecondary">
              • {metrics?.totalPacientes || 0} pacientes cadastrados no sistema
            </Typography>
            <Typography variant="body2" color="textSecondary">
              • {metrics?.pacientesCadastradosNoMes || 0} novos pacientes este mês
            </Typography>
            <Typography variant="body2" color="textSecondary">
              • {metrics?.totalDentistas || 0} dentistas ativos
            </Typography>
          </Grid>
          <Grid item xs={12} md={6}>
            <Typography variant="body2" color="textSecondary">
              • {metrics?.consultasHoje || 0} consultas programadas para hoje
            </Typography>
            <Typography variant="body2" color="textSecondary">
              • {metrics?.consultasNoMes || 0} consultas realizadas este mês
            </Typography>
            <Typography variant="body2" color="textSecondary">
              • {formatCurrency(metrics?.faturamentoMensal)} de faturamento mensal
            </Typography>
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );
};

export default Dashboard;