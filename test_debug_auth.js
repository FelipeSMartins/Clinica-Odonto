const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api';

async function testDebugAuth() {
    try {
        console.log('=== TESTE DE DEBUG DE AUTENTICAÇÃO ===\n');
        
        // 1. Login
        console.log('1. Fazendo login...');
        const loginResponse = await axios.post(`${BASE_URL}/auth/login`, {
            email: 'admin@clinica.com',
            senha: 'admin123'
        });
        
        const { token, authorities } = loginResponse.data;
        console.log('✅ Login realizado com sucesso');
        console.log('Token:', token.substring(0, 50) + '...');
        console.log('Authorities:', authorities);
        console.log();
        
        // 2. Teste do endpoint /me
        console.log('2. Testando /api/auth/me...');
        const meResponse = await axios.get(`${BASE_URL}/auth/me`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        console.log('✅ /api/auth/me funcionou:', meResponse.data.nome, '-', meResponse.data.tipo);
        console.log();
        
        // 3. Teste do endpoint base de movimentações
        console.log('3. Testando /api/movimentacoes-material...');
        const movimentacoesResponse = await axios.get(`${BASE_URL}/movimentacoes-material`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        console.log('✅ /api/movimentacoes-material funcionou:', movimentacoesResponse.data.length, 'itens');
        console.log();
        
        // 4. Teste do endpoint problemático com parâmetros
        console.log('4. Testando /api/movimentacoes-material/periodo com parâmetros...');
        const dataInicio = '2024-01-01';
        const dataFim = '2024-12-31';
        
        try {
            const periodoResponse = await axios.get(`${BASE_URL}/movimentacoes-material/periodo`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                params: {
                    dataInicio: dataInicio,
                    dataFim: dataFim
                }
            });
            console.log('✅ /api/movimentacoes-material/periodo funcionou:', periodoResponse.data.length, 'itens');
        } catch (error) {
            console.log('❌ Erro no endpoint /periodo:');
            console.log('Status:', error.response?.status);
            console.log('Headers da resposta:', error.response?.headers);
            console.log('Dados da resposta:', error.response?.data);
            
            // Vamos tentar sem parâmetros
            console.log('\n5. Tentando sem parâmetros obrigatórios...');
            try {
                const periodoSemParamsResponse = await axios.get(`${BASE_URL}/movimentacoes-material/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                console.log('✅ Funcionou com parâmetros na URL:', periodoSemParamsResponse.data.length, 'itens');
            } catch (error2) {
                console.log('❌ Ainda com erro:', error2.response?.status, error2.response?.data);
            }
        }
        
        // 6. Teste de outros endpoints com @PreAuthorize
        console.log('\n6. Testando outros endpoints com @PreAuthorize...');
        try {
            const estatisticasResponse = await axios.get(`${BASE_URL}/movimentacoes-material/estatisticas/mes-atual`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            console.log('✅ /estatisticas/mes-atual funcionou:', estatisticasResponse.data);
        } catch (error) {
            console.log('❌ Erro em /estatisticas/mes-atual:', error.response?.status, error.response?.data);
        }
        
        try {
            const materiaisConsultaResponse = await axios.get(`${BASE_URL}/materiais-consulta/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            console.log('✅ /materiais-consulta/periodo funcionou:', materiaisConsultaResponse.data.length, 'itens');
        } catch (error) {
            console.log('❌ Erro em /materiais-consulta/periodo:', error.response?.status, error.response?.data);
        }
        
    } catch (error) {
        console.error('Erro geral:', error.message);
        if (error.response) {
            console.error('Status:', error.response.status);
            console.error('Data:', error.response.data);
        }
    }
}

testDebugAuth();