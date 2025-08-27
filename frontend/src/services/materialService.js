import api from './api';

const materialService = {
  // Listar todos os materiais
  listarTodos: async () => {
    const response = await api.get('/materiais');
    return response.data;
  },

  // Listar materiais com paginação
  listarComPaginacao: async (page = 0, size = 10) => {
    const response = await api.get(`/materiais/paginado?page=${page}&size=${size}`);
    return response.data;
  },

  // Buscar material por ID
  buscarPorId: async (id) => {
    const response = await api.get(`/materiais/${id}`);
    return response.data;
  },

  // Buscar materiais por nome
  buscarPorNome: async (nome) => {
    const response = await api.get(`/materiais/buscar?nome=${encodeURIComponent(nome)}`);
    return response.data;
  },

  // Buscar material por código
  buscarPorCodigo: async (codigo) => {
    const response = await api.get(`/materiais/codigo/${codigo}`);
    return response.data;
  },

  // Buscar materiais por categoria
  buscarPorCategoria: async (categoria) => {
    const response = await api.get(`/materiais/categoria?categoria=${encodeURIComponent(categoria)}`);
    return response.data;
  },

  // Listar materiais com estoque baixo
  listarEstoqueBaixo: async () => {
    const response = await api.get('/materiais/estoque-baixo');
    return response.data;
  },

  // Listar todas as categorias
  listarCategorias: async () => {
    const response = await api.get('/materiais/categorias');
    return response.data;
  },

  // Criar novo material
  criar: async (materialData) => {
    const response = await api.post('/materiais', materialData);
    return response.data;
  },

  // Atualizar material
  atualizar: async (id, materialData) => {
    const response = await api.put(`/materiais/${id}`, materialData);
    return response.data;
  },

  // Atualizar estoque
  atualizarEstoque: async (id, novoEstoque) => {
    const response = await api.patch(`/materiais/${id}/estoque`, { estoqueAtual: novoEstoque });
    return response.data;
  },

  // Inativar material
  inativar: async (id) => {
    const response = await api.patch(`/materiais/${id}/inativar`);
    return response.data;
  },

  // Ativar material
  ativar: async (id) => {
    const response = await api.patch(`/materiais/${id}/ativar`);
    return response.data;
  },

  // Contar materiais ativos
  contarAtivos: async () => {
    const response = await api.get('/materiais/contar/ativos');
    return response.data;
  },

  // Contar materiais com estoque baixo
  contarEstoqueBaixo: async () => {
    const response = await api.get('/materiais/contar/estoque-baixo');
    return response.data;
  }
};

export default materialService;