import api from './api';

const materialConsultaService = {
  // Registrar uso de material em consulta
  registrar: async (materialConsultaData) => {
    const response = await api.post('/materiais-consulta', materialConsultaData);
    return response.data;
  },

  // Listar todos os materiais de consulta
  listarTodos: async () => {
    const response = await api.get('/materiais-consulta');
    return response.data;
  },

  // Listar materiais de consulta com paginação
  listarComPaginacao: async (page = 0, size = 10) => {
    const response = await api.get(`/materiais-consulta/paginado?page=${page}&size=${size}`);
    return response.data;
  },

  // Buscar material consulta por ID
  buscarPorId: async (id) => {
    const response = await api.get(`/materiais-consulta/${id}`);
    return response.data;
  },

  // Buscar materiais por consulta
  buscarPorConsulta: async (consultaId) => {
    const response = await api.get(`/materiais-consulta/consulta/${consultaId}`);
    return response.data;
  },

  // Buscar consultas por material
  buscarPorMaterial: async (materialId) => {
    const response = await api.get(`/materiais-consulta/material/${materialId}`);
    return response.data;
  },

  // Buscar por período
  buscarPorPeriodo: async (dataInicio, dataFim) => {
    const response = await api.get(`/materiais-consulta/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`);
    return response.data;
  },

  // Calcular valor total por consulta
  calcularValorTotalConsulta: async (consultaId) => {
    const response = await api.get(`/materiais-consulta/consulta/${consultaId}/valor-total`);
    return response.data;
  },

  // Calcular quantidade total por material
  calcularQuantidadeTotalMaterial: async (materialId, dataInicio, dataFim) => {
    const response = await api.get(`/materiais-consulta/material/${materialId}/quantidade-total?dataInicio=${dataInicio}&dataFim=${dataFim}`);
    return response.data;
  },

  // Calcular valor total do mês
  calcularValorTotalMes: async () => {
    const response = await api.get('/materiais-consulta/estatisticas/valor-total-mes');
    return response.data;
  },

  // Contar registros do mês
  contarMesAtual: async () => {
    const response = await api.get('/materiais-consulta/estatisticas/count-mes-atual');
    return response.data;
  },

  // Atualizar quantidade
  atualizarQuantidade: async (id, novaQuantidade) => {
    const response = await api.patch(`/materiais-consulta/${id}/quantidade`, { quantidadeUtilizada: novaQuantidade });
    return response.data;
  },

  // Remover material da consulta
  remover: async (id) => {
    const response = await api.delete(`/materiais-consulta/${id}`);
    return response.data;
  }
};

export default materialConsultaService;