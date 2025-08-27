import api from './api';

class AuthService {
  // Fazer login
  async login(email, senha) {
    try {
      const response = await api.post('/auth/login', {
        email,
        senha,
      });
      
      const { token, ...userData } = response.data;
      
      // Salvar token e dados do usuário no localStorage
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userData));
      
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Fazer logout
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  // Verificar se o usuário está autenticado
  isAuthenticated() {
    const token = localStorage.getItem('token');
    return !!token;
  }

  // Obter dados do usuário logado
  getCurrentUser() {
    const userData = localStorage.getItem('user');
    return userData ? JSON.parse(userData) : null;
  }

  // Obter token
  getToken() {
    return localStorage.getItem('token');
  }

  // Registrar novo usuário
  async register(userData) {
    try {
      const response = await api.post('/auth/register', userData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Obter informações do usuário atual do servidor
  async getMe() {
    try {
      const response = await api.get('/auth/me');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export default new AuthService();