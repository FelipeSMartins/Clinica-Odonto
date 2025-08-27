import api from './api';

const planoSaudeService = {
  // Listar todos os planos de saúde
  listarTodos: async () => {
    const response = await api.get('/planos-saude');
    return response.data;
  },

  // Listar planos de saúde com paginação
  listarComPaginacao: async (page = 0, size = 10) => {
    const response = await api.get(`/planos-saude/paginado?page=${page}&size=${size}`);
    return response.data;
  },

  // Buscar plano de saúde por ID
  buscarPorId: async (id) => {
    const response = await api.get(`/planos-saude/${id}`);
    return response.data;
  },

  // Buscar planos de saúde por nome
  buscarPorNome: async (nome) => {
    const response = await api.get(`/planos-saude/buscar?nome=${encodeURIComponent(nome)}`);
    return response.data;
  },

  // Criar novo plano de saúde
  criar: async (planoData) => {
    const response = await api.post('/planos-saude', planoData);
    return response.data;
  },

  // Atualizar plano de saúde
  atualizar: async (id, planoData) => {
    const response = await api.put(`/planos-saude/${id}`, planoData);
    return response.data;
  },

  // Ativar plano de saúde
  ativar: async (id) => {
    const response = await api.patch(`/planos-saude/${id}/ativar`);
    return response.data;
  },

  // Inativar plano de saúde
  inativar: async (id) => {
    const response = await api.patch(`/planos-saude/${id}/inativar`);
    return response.data;
  },

  // Deletar plano de saúde
  deletar: async (id) => {
    const response = await api.delete(`/planos-saude/${id}`);
    return response.data;
  }
};

export default planoSaudeService;