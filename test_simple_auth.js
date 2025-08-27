const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api';

async function testSimpleAuth() {
    console.log('=== TESTE SIMPLES DE AUTORIZAÇÃO ===\n');
    
    try {
        // 1. Login
        console.log('1. Fazendo login...');
        const loginResponse = await axios.post(`${BASE_URL}/auth/login`, {
            email: 'admin@clinica.com',
            senha: 'admin123'
        });
        
        const token = loginResponse.data.token;
        console.log('✅ Login realizado com sucesso');
        console.log('Token:', token.substring(0, 50) + '...');
        console.log('Authorities:', loginResponse.data.authorities);
        
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
        
        // 2. Testar endpoint básico que funciona
        console.log('\n2. Testando /api/movimentacoes-material (básico)...');
        const basicResponse = await axios.get(`${BASE_URL}/movimentacoes-material`, { headers });
        console.log('✅ Endpoint básico funcionou:', basicResponse.data.length, 'itens');
        
        // 3. Testar endpoint com @PreAuthorize simples
        console.log('\n3. Testando /api/movimentacoes-material/paginado...');
        const paginatedResponse = await axios.get(`${BASE_URL}/movimentacoes-material/paginado`, { headers });
        console.log('✅ Endpoint paginado funcionou:', paginatedResponse.data.totalElements, 'total');
        
        // 4. Testar endpoint com parâmetros simples
        console.log('\n4. Testando /api/materiais...');
        const materiaisResponse = await axios.get(`${BASE_URL}/materiais`, { headers });
        console.log('✅ Endpoint materiais funcionou:', materiaisResponse.data.length, 'itens');
        
        // 5. Testar endpoint com parâmetros de data (sem parâmetros obrigatórios)
        console.log('\n5. Testando /api/movimentacoes-material/periodo SEM parâmetros...');
        try {
            const periodoResponse = await axios.get(`${BASE_URL}/movimentacoes-material/periodo`, { headers });
            console.log('✅ Endpoint período funcionou:', periodoResponse.data.length, 'itens');
        } catch (error) {
            console.log('❌ Erro no endpoint período SEM parâmetros:');
            console.log('Status:', error.response?.status);
            console.log('Dados:', error.response?.data);
        }
        
        // 6. Testar endpoint com parâmetros de data válidos
        console.log('\n6. Testando /api/movimentacoes-material/periodo COM parâmetros...');
        try {
            const periodoComParamsResponse = await axios.get(
                `${BASE_URL}/movimentacoes-material/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, 
                { headers }
            );
            console.log('✅ Endpoint período COM parâmetros funcionou:', periodoComParamsResponse.data.length, 'itens');
        } catch (error) {
            console.log('❌ Erro no endpoint período COM parâmetros:');
            console.log('Status:', error.response?.status);
            console.log('Dados:', error.response?.data);
        }
        
    } catch (error) {
        console.error('❌ Erro geral:', error.response?.data || error.message);
    }
}

testSimpleAuth();