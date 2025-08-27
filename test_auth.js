// Script para testar autenticação e endpoints de relatórios
const testAuth = async () => {
  try {
    // Primeiro fazer login
    const loginResponse = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: 'admin@clinica.com',
        senha: 'admin123'
      })
    });
    
    const loginData = await loginResponse.json();
    console.log('Login realizado:', loginData.nome);
    console.log('Authorities:', loginData.authorities);
    
    const token = loginData.token;
    console.log('Token:', token.substring(0, 50) + '...');
    
    // Testar endpoint /auth/me para verificar se o token está válido
    console.log('\nTestando endpoint /api/auth/me...');
    const meResponse = await fetch('http://localhost:8080/api/auth/me', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    if (meResponse.ok) {
      const userData = await meResponse.json();
      console.log('✅ Usuário autenticado:', userData.nome, '- Tipo:', userData.tipo);
    } else {
      console.log('❌ Erro na verificação do usuário:', meResponse.status);
      const errorText = await meResponse.text();
      console.log('Erro:', errorText);
    }
    
    // Testar endpoint simples de materiais primeiro
    console.log('\nTestando endpoint /api/materiais...');
    const materiaisResponse = await fetch('http://localhost:8080/api/materiais', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    if (materiaisResponse.ok) {
      const materiais = await materiaisResponse.json();
      console.log('✅ Materiais carregados:', materiais.length, 'itens');
    } else {
      console.log('❌ Erro ao carregar materiais:', materiaisResponse.status);
      const errorText = await materiaisResponse.text();
      console.log('Erro materiais:', errorText);
    }
    
    // Testar endpoint específico de movimentações
    console.log('\nTestando endpoint /api/movimentacoes-material...');
    const movimentacoesListResponse = await fetch('http://localhost:8080/api/movimentacoes-material', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    if (movimentacoesListResponse.ok) {
      const movimentacoes = await movimentacoesListResponse.json();
      console.log('✅ Lista de movimentações carregada:', movimentacoes.length, 'itens');
    } else {
      console.log('❌ Erro ao carregar lista de movimentações:', movimentacoesListResponse.status);
      const errorText = await movimentacoesListResponse.text();
      console.log('Erro lista movimentações:', errorText);
    }
    
    // Testar endpoint de movimentações por período
    console.log('\nTestando endpoint /api/movimentacoes-material/periodo...');
    const movimentacoesResponse = await fetch('http://localhost:8080/api/movimentacoes-material/periodo?dataInicio=2025-08-01&dataFim=2025-08-31', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    if (movimentacoesResponse.ok) {
      const movimentacoes = await movimentacoesResponse.json();
      console.log('✅ Movimentações por período carregadas:', movimentacoes.length, 'itens');
    } else {
      console.log('❌ Erro ao carregar movimentações por período:', movimentacoesResponse.status);
      const errorText = await movimentacoesResponse.text();
      console.log('Erro movimentações período:', errorText);
    }
    
  } catch (error) {
    console.error('Erro no teste:', error);
  }
};

testAuth();