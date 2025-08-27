import api from './api';

const usuarioService = {
  // Listar todos os usuários
  listarTodos: async () => {
    try {
      const response = await api.get('/usuarios');
      return response.data;
    } catch (error) {
      console.error('Erro ao listar usuários:', error);
      throw error;
    }
  },

  // Listar usuários com paginação
  listarComPaginacao: async (page = 0, size = 10, sortBy = 'nome', sortDir = 'asc') => {
    try {
      const response = await api.get('/usuarios/paginado', {
        params: { page, size, sortBy, sortDir }
      });
      return response.data;
    } catch (error) {
      console.error('Erro ao listar usuários com paginação:', error);
      throw error;
    }
  },

  // Buscar usuário por ID
  buscarPorId: async (id) => {
    try {
      const response = await api.get(`/usuarios/${id}`);
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar usuário por ID:', error);
      throw error;
    }
  },

  // Buscar usuários por nome
  buscarPorNome: async (nome) => {
    try {
      const response = await api.get('/usuarios/buscar/nome', {
        params: { nome }
      });
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar usuários por nome:', error);
      throw error;
    }
  },

  // Buscar usuário por email
  buscarPorEmail: async (email) => {
    try {
      const response = await api.get('/usuarios/buscar/email', {
        params: { email }
      });
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar usuário por email:', error);
      throw error;
    }
  },

  // Listar usuários ativos
  listarAtivos: async () => {
    try {
      const response = await api.get('/usuarios/ativos');
      return response.data;
    } catch (error) {
      console.error('Erro ao listar usuários ativos:', error);
      throw error;
    }
  },

  // Criar novo usuário
  criar: async (usuarioData) => {
    try {
      const response = await api.post('/usuarios', usuarioData);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar usuário:', error);
      throw error;
    }
  },

  // Atualizar usuário
  atualizar: async (id, usuarioData) => {
    try {
      const response = await api.put(`/usuarios/${id}`, usuarioData);
      return response.data;
    } catch (error) {
      console.error('Erro ao atualizar usuário:', error);
      throw error;
    }
  },

  // Ativar usuário
  ativar: async (id) => {
    try {
      const response = await api.patch(`/usuarios/${id}/ativar`);
      return response.data;
    } catch (error) {
      console.error('Erro ao ativar usuário:', error);
      throw error;
    }
  },

  // Desativar usuário
  desativar: async (id) => {
    try {
      const response = await api.patch(`/usuarios/${id}/desativar`);
      return response.data;
    } catch (error) {
      console.error('Erro ao desativar usuário:', error);
      throw error;
    }
  },

  // Deletar usuário
  deletar: async (id) => {
    try {
      await api.delete(`/usuarios/${id}`);
      return true;
    } catch (error) {
      console.error('Erro ao deletar usuário:', error);
      throw error;
    }
  },

  // Alterar senha do usuário
  alterarSenha: async (id, novaSenha) => {
    try {
      const response = await api.patch(`/usuarios/${id}/senha`, {
        novaSenha
      });
      return response.data;
    } catch (error) {
      console.error('Erro ao alterar senha do usuário:', error);
      throw error;
    }
  },

  // Utilitários
  formatarTipoUsuario: (tipo) => {
    const tipos = {
      'ADMIN': 'Administrador',
      'DENTISTA': 'Dentista',
      'RECEPCIONISTA': 'Recepcionista'
    };
    return tipos[tipo] || tipo;
  },

  obterCorStatus: (ativo) => {
    return ativo ? 'success' : 'danger';
  },

  formatarStatus: (ativo) => {
    return ativo ? 'Ativo' : 'Inativo';
  },

  // Validações
  validarEmail: (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  },

  validarSenha: (senha) => {
    // Mínimo 6 caracteres
    return senha && senha.length >= 6;
  },

  // Opções para select
  obterTiposUsuario: () => [
    { value: 'ADMIN', label: 'Administrador' },
    { value: 'DENTISTA', label: 'Dentista' },
    { value: 'RECEPCIONISTA', label: 'Recepcionista' }
  ]
};

export default usuarioService;