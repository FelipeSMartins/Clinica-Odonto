import api from './api';

const consultaService = {
  // Listar todas as consultas
  listarTodas: async () => {
    const response = await api.get('/consultas');
    return response.data;
  },

  // Listar com paginação
  listarComPaginacao: async (page = 0, size = 10, sort = 'dataHora,asc') => {
    const response = await api.get('/consultas/paginacao', {
      params: { page, size, sort }
    });
    return response.data;
  },

  // Buscar por ID
  buscarPorId: async (id) => {
    const response = await api.get(`/consultas/${id}`);
    return response.data;
  },

  // Buscar por data
  buscarPorData: async (data) => {
    const response = await api.get(`/consultas/data/${data}`);
    return response.data;
  },

  // Buscar por paciente
  buscarPorPaciente: async (pacienteId) => {
    const response = await api.get(`/consultas/paciente/${pacienteId}`);
    return response.data;
  },

  // Buscar por dentista
  buscarPorDentista: async (dentistaId) => {
    const response = await api.get(`/consultas/dentista/${dentistaId}`);
    return response.data;
  },

  // Buscar por status
  buscarPorStatus: async (status) => {
    const response = await api.get(`/consultas/status/${status}`);
    return response.data;
  },

  // Buscar consultas de hoje
  buscarConsultasHoje: async () => {
    const response = await api.get('/consultas/hoje');
    return response.data;
  },

  // Buscar por período
  buscarPorPeriodo: async (inicio, fim) => {
    const response = await api.get('/consultas/periodo', {
      params: { inicio, fim }
    });
    return response.data;
  },

  // Criar consulta
  criar: async (consultaData) => {
    const response = await api.post('/consultas', consultaData);
    return response.data;
  },

  // Atualizar consulta
  atualizar: async (id, consultaData) => {
    const response = await api.put(`/consultas/${id}`, consultaData);
    return response.data;
  },

  // Alterar status
  alterarStatus: async (id, status) => {
    const response = await api.patch(`/consultas/${id}/status`, null, {
      params: { status }
    });
    return response.data;
  },

  // Cancelar consulta
  cancelar: async (id) => {
    const response = await api.delete(`/consultas/${id}`);
    return response.data;
  },

  // Confirmar consulta
  confirmar: async (id) => {
    return consultaService.alterarStatus(id, 'CONFIRMADA');
  },

  // Iniciar consulta
  iniciar: async (id) => {
    return consultaService.alterarStatus(id, 'EM_ANDAMENTO');
  },

  // Concluir consulta
  concluir: async (id) => {
    return consultaService.alterarStatus(id, 'CONCLUIDA');
  },

  // Utilitários para formatação
  formatarStatus: (status) => {
    const statusMap = {
      'AGENDADA': 'Agendada',
      'CONFIRMADA': 'Confirmada',
      'EM_ANDAMENTO': 'Em Andamento',
      'CONCLUIDA': 'Concluída',
      'CANCELADA': 'Cancelada'
    };
    return statusMap[status] || status;
  },

  getStatusColor: (status) => {
    const colorMap = {
      'AGENDADA': 'info',
      'CONFIRMADA': 'primary',
      'EM_ANDAMENTO': 'warning',
      'CONCLUIDA': 'success',
      'CANCELADA': 'error'
    };
    return colorMap[status] || 'default';
  },

  // Validar se pode alterar status
  podeAlterarStatus: (statusAtual, novoStatus) => {
    const transicoes = {
      'AGENDADA': ['CONFIRMADA', 'CANCELADA'],
      'CONFIRMADA': ['EM_ANDAMENTO', 'CANCELADA'],
      'EM_ANDAMENTO': ['CONCLUIDA'],
      'CONCLUIDA': [],
      'CANCELADA': []
    };
    return transicoes[statusAtual]?.includes(novoStatus) || false;
  },

  // Obter próximos status possíveis
  getProximosStatus: (statusAtual) => {
    const transicoes = {
      'AGENDADA': [
        { value: 'CONFIRMADA', label: 'Confirmar' },
        { value: 'CANCELADA', label: 'Cancelar' }
      ],
      'CONFIRMADA': [
        { value: 'EM_ANDAMENTO', label: 'Iniciar' },
        { value: 'CANCELADA', label: 'Cancelar' }
      ],
      'EM_ANDAMENTO': [
        { value: 'CONCLUIDA', label: 'Concluir' }
      ],
      'CONCLUIDA': [],
      'CANCELADA': []
    };
    return transicoes[statusAtual] || [];
  }
};

export default consultaService;