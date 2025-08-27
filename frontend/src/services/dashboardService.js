import api from './api';

class DashboardService {
  // Buscar métricas do dashboard
  async getMetrics() {
    try {
      const response = await api.get('/dashboard/metrics');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export default new DashboardService();