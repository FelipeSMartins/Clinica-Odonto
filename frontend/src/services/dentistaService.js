import api from './api';

const dentistaService = {
  // Listar todos os dentistas ativos
  listarTodos: async () => {
    const response = await api.get('/dentistas');
    return response.data;
  },

  // Listar dentistas com paginação
  listarComPaginacao: async (page = 0, size = 10, sort = 'nome') => {
    const response = await api.get('/dentistas/paginado', {
      params: { page, size, sort }
    });
    return response.data;
  },

  // Buscar dentista por ID
  buscarPorId: async (id) => {
    const response = await api.get(`/dentistas/${id}`);
    return response.data;
  },

  // Buscar dentistas por nome
  buscarPorNome: async (nome) => {
    const response = await api.get('/dentistas/buscar/nome', {
      params: { nome }
    });
    return response.data;
  },

  // Buscar dentistas por especialidade
  buscarPorEspecialidade: async (especialidade) => {
    const response = await api.get('/dentistas/buscar/especialidade', {
      params: { especialidade }
    });
    return response.data;
  },

  // Buscar dentista por CRO
  buscarPorCro: async (cro) => {
    const response = await api.get('/dentistas/buscar/cro', {
      params: { cro }
    });
    return response.data;
  },

  // Criar novo dentista
  criar: async (dentistaData) => {
    const response = await api.post('/dentistas', dentistaData);
    return response.data;
  },

  // Atualizar dentista
  atualizar: async (id, dentistaData) => {
    const response = await api.put(`/dentistas/${id}`, dentistaData);
    return response.data;
  },

  // Ativar dentista
  ativar: async (id) => {
    const response = await api.patch(`/dentistas/${id}/ativar`);
    return response.data;
  },

  // Inativar dentista
  inativar: async (id) => {
    const response = await api.patch(`/dentistas/${id}/inativar`);
    return response.data;
  }
};

export default dentistaService;