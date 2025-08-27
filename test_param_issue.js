const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

async function testParameterIssue() {
    try {
        console.log('=== Teste de Problema com @RequestParam ===\n');
        
        // 1. Login como ADMIN
        console.log('1. Fazendo login como ADMIN...');
        const loginResponse = await axios.post(`${BASE_URL}/api/auth/login`, {
            email: 'admin@clinica.com',
            senha: 'admin123'
        });
        
        const token = loginResponse.data.token;
        console.log('✓ Login realizado com sucesso');
        
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
        
        // 2. Testar endpoint simples sem parâmetros
        console.log('\n2. Testando endpoint simples sem parâmetros...');
        try {
            const simpleResponse = await axios.get(`${BASE_URL}/api/test/simple`, { headers });
            console.log('✓ Endpoint simples funcionou - Status:', simpleResponse.status);
            console.log('✓ Resposta:', simpleResponse.data.message);
        } catch (error) {
            console.log('✗ Endpoint simples falhou:', error.response?.status, error.response?.data?.message);
        }
        
        // 3. Testar endpoint com parâmetro obrigatório
        console.log('\n3. Testando endpoint com parâmetro obrigatório...');
        try {
            const paramResponse = await axios.get(`${BASE_URL}/api/test/with-param?testParam=hello`, { headers });
            console.log('✓ Endpoint com parâmetro funcionou - Status:', paramResponse.status);
            console.log('✓ Resposta:', paramResponse.data.message);
            console.log('✓ Parâmetro recebido:', paramResponse.data.receivedParam);
        } catch (error) {
            console.log('✗ Endpoint com parâmetro falhou:');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
        }
        
        // 4. Testar endpoint com parâmetro obrigatório sem fornecer o parâmetro
        console.log('\n4. Testando endpoint com parâmetro obrigatório SEM fornecer parâmetro...');
        try {
            const noParamResponse = await axios.get(`${BASE_URL}/api/test/with-param`, { headers });
            console.log('✓ Endpoint sem parâmetro funcionou (inesperado) - Status:', noParamResponse.status);
        } catch (error) {
            console.log('✗ Endpoint sem parâmetro falhou (esperado):');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
        }
        
        // 5. Testar endpoint com parâmetro opcional
        console.log('\n5. Testando endpoint com parâmetro opcional...');
        try {
            const optionalResponse = await axios.get(`${BASE_URL}/api/test/with-optional-param`, { headers });
            console.log('✓ Endpoint com parâmetro opcional funcionou - Status:', optionalResponse.status);
            console.log('✓ Resposta:', optionalResponse.data.message);
        } catch (error) {
            console.log('✗ Endpoint com parâmetro opcional falhou:', error.response?.status, error.response?.data?.message);
        }
        
        // 6. Comparar com endpoint original problemático
        console.log('\n6. Testando endpoint original problemático...');
        try {
            const originalResponse = await axios.get(
                `${BASE_URL}/api/movimentacoes-material/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, 
                { headers }
            );
            console.log('✓ Endpoint original funcionou - Status:', originalResponse.status);
        } catch (error) {
            console.log('✗ Endpoint original falhou:');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
        }
        
    } catch (error) {
        console.error('Erro durante o teste:', error.message);
        if (error.response) {
            console.error('Status:', error.response.status);
            console.error('Data:', error.response.data);
        }
    }
}

testParameterIssue();