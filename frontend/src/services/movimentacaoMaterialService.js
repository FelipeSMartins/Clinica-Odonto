import api from './api';

const movimentacaoMaterialService = {
  // Registrar movimentação de material
  registrar: async (movimentacaoData) => {
    const response = await api.post('/movimentacoes-material', movimentacaoData);
    return response.data;
  },

  // Listar todas as movimentações
  listarTodas: async () => {
    const response = await api.get('/movimentacoes-material');
    return response.data;
  },

  // Listar movimentações com paginação
  listarComPaginacao: async (page = 0, size = 10) => {
    const response = await api.get(`/movimentacoes-material/paginado?page=${page}&size=${size}`);
    return response.data;
  },

  // Buscar movimentação por ID
  buscarPorId: async (id) => {
    const response = await api.get(`/movimentacoes-material/${id}`);
    return response.data;
  },

  // Buscar movimentações por material
  buscarPorMaterial: async (materialId) => {
    const response = await api.get(`/movimentacoes-material/material/${materialId}`);
    return response.data;
  },

  // Buscar movimentações por tipo
  buscarPorTipo: async (tipo) => {
    const response = await api.get(`/movimentacoes-material/tipo?tipo=${tipo}`);
    return response.data;
  },

  // Buscar movimentações por período
  buscarPorPeriodo: async (dataInicio, dataFim) => {
    const response = await api.get(`/movimentacoes-material/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`);
    return response.data;
  },

  // Buscar movimentações por material e período
  buscarPorMaterialEPeriodo: async (materialId, dataInicio, dataFim) => {
    const response = await api.get(`/movimentacoes-material/material/${materialId}/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`);
    return response.data;
  },

  // Buscar movimentações por consulta
  buscarPorConsulta: async (consultaId) => {
    const response = await api.get(`/movimentacoes-material/consulta/${consultaId}`);
    return response.data;
  },

  // Contar movimentações do mês atual
  contarMesAtual: async () => {
    const response = await api.get('/movimentacoes-material/estatisticas/mes-atual');
    return response.data;
  },

  // Listar tipos de movimentação
  listarTipos: async () => {
    const response = await api.get('/movimentacoes-material/tipos');
    return response.data;
  }
};

export default movimentacaoMaterialService;