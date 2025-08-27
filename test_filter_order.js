const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

async function testFilterOrder() {
    try {
        console.log('=== Teste de Ordem dos Filtros ===\n');
        
        // 1. Login como ADMIN
        console.log('1. Fazendo login como ADMIN...');
        const loginResponse = await axios.post(`${BASE_URL}/api/auth/login`, {
            email: 'admin@clinica.com',
            senha: 'admin123'
        });
        
        const token = loginResponse.data.token;
        console.log('✓ Login realizado com sucesso');
        console.log('✓ Token:', token.substring(0, 20) + '...');
        
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
        
        // 2. Testar endpoint que funciona
        console.log('\n2. Testando endpoint que funciona (/api/movimentacoes-material)...');
        try {
            const workingResponse = await axios.get(`${BASE_URL}/api/movimentacoes-material`, { headers });
            console.log('✓ Endpoint funcionou - Status:', workingResponse.status);
        } catch (error) {
            console.log('✗ Endpoint falhou:', error.response?.status, error.response?.data?.message);
        }
        
        // 3. Testar endpoint problemático sem parâmetros
        console.log('\n3. Testando endpoint problemático sem parâmetros (/api/movimentacoes-material/periodo)...');
        try {
            const problematicResponse = await axios.get(`${BASE_URL}/api/movimentacoes-material/periodo`, { headers });
            console.log('✓ Endpoint funcionou - Status:', problematicResponse.status);
        } catch (error) {
            console.log('✗ Endpoint falhou:');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
            console.log('  Headers da resposta:', error.response?.headers);
        }
        
        // 4. Testar endpoint problemático com parâmetros válidos
        console.log('\n4. Testando endpoint problemático com parâmetros válidos...');
        try {
            const validParamsResponse = await axios.get(
                `${BASE_URL}/api/movimentacoes-material/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, 
                { headers }
            );
            console.log('✓ Endpoint com parâmetros funcionou - Status:', validParamsResponse.status);
        } catch (error) {
            console.log('✗ Endpoint com parâmetros falhou:');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
            console.log('  Headers da resposta:', error.response?.headers);
        }
        
        // 5. Testar com token inválido para comparar
        console.log('\n5. Testando com token inválido para comparar...');
        const invalidHeaders = {
            'Authorization': 'Bearer invalid-token',
            'Content-Type': 'application/json'
        };
        
        try {
            const invalidTokenResponse = await axios.get(`${BASE_URL}/api/movimentacoes-material`, { headers: invalidHeaders });
            console.log('✓ Token inválido funcionou (inesperado) - Status:', invalidTokenResponse.status);
        } catch (error) {
            console.log('✗ Token inválido falhou (esperado):');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
        }
        
        // 6. Testar endpoint problemático com token inválido
        console.log('\n6. Testando endpoint problemático com token inválido...');
        try {
            const invalidTokenPeriodoResponse = await axios.get(`${BASE_URL}/api/movimentacoes-material/periodo`, { headers: invalidHeaders });
            console.log('✓ Token inválido no endpoint problemático funcionou (inesperado) - Status:', invalidTokenPeriodoResponse.status);
        } catch (error) {
            console.log('✗ Token inválido no endpoint problemático falhou:');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
            console.log('  Comparação: Mesmo erro que com token válido?', 
                error.response?.data?.message === 'Token de acesso inválido ou expirado');
        }
        
    } catch (error) {
        console.error('Erro durante o teste:', error.message);
    }
}

testFilterOrder();