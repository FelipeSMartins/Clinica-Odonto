import api from './api';

const pacienteService = {
  // Listar todos os pacientes
  listarTodos: async () => {
    const response = await api.get('/pacientes');
    return response.data;
  },

  // Listar pacientes com paginação
  listarComPaginacao: async (page = 0, size = 10) => {
    const response = await api.get(`/pacientes/paginado?page=${page}&size=${size}`);
    return response.data;
  },

  // Buscar paciente por ID
  buscarPorId: async (id) => {
    const response = await api.get(`/pacientes/${id}`);
    return response.data;
  },

  // Buscar pacientes por nome
  buscarPorNome: async (nome) => {
    const response = await api.get(`/pacientes/buscar?nome=${encodeURIComponent(nome)}`);
    return response.data;
  },

  // Buscar paciente por CPF
  buscarPorCpf: async (cpf) => {
    const response = await api.get(`/pacientes/cpf/${cpf}`);
    return response.data;
  },

  // Criar novo paciente
  criar: async (pacienteData) => {
    const response = await api.post('/pacientes', pacienteData);
    return response.data;
  },

  // Atualizar paciente
  atualizar: async (id, pacienteData) => {
    const response = await api.put(`/pacientes/${id}`, pacienteData);
    return response.data;
  },

  // Inativar paciente
  inativar: async (id) => {
    const response = await api.patch(`/pacientes/${id}/inativar`);
    return response.data;
  },

  // Ativar paciente
  ativar: async (id) => {
    const response = await api.patch(`/pacientes/${id}/ativar`);
    return response.data;
  }
};

export default pacienteService;