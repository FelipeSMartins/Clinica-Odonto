const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api';

async function testEndpointDirect() {
    console.log('=== TESTE DIRETO DE ENDPOINT ===\n');
    
    try {
        // 1. Login
        console.log('1. Fazendo login...');
        const loginResponse = await axios.post(`${BASE_URL}/auth/login`, {
            email: 'admin@clinica.com',
            senha: 'admin123'
        });
        
        const token = loginResponse.data.token;
        console.log('✅ Login realizado com sucesso');
        
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
        
        // 2. Testar endpoint que sabemos que funciona
        console.log('\n2. Testando endpoint que funciona: /api/movimentacoes-material/paginado');
        try {
            const workingResponse = await axios.get(`${BASE_URL}/movimentacoes-material/paginado`, { headers });
            console.log('✅ Endpoint funcionou:', workingResponse.status);
        } catch (error) {
            console.log('❌ Erro inesperado no endpoint que deveria funcionar:', error.response?.status, error.response?.data);
        }
        
        // 3. Testar endpoint problemático com diferentes abordagens
        console.log('\n3. Testando endpoint problemático: /api/movimentacoes-material/periodo');
        
        // 3a. Sem parâmetros (deve dar erro de parâmetro obrigatório, não 401)
        console.log('\n3a. Sem parâmetros...');
        try {
            const noParamsResponse = await axios.get(`${BASE_URL}/movimentacoes-material/periodo`, { headers });
            console.log('✅ Funcionou sem parâmetros:', noParamsResponse.status);
        } catch (error) {
            console.log('❌ Erro sem parâmetros:');
            console.log('Status:', error.response?.status);
            console.log('Dados:', error.response?.data);
            console.log('Headers de resposta:', Object.fromEntries(Object.entries(error.response?.headers || {})));
        }
        
        // 3b. Com parâmetros válidos
        console.log('\n3b. Com parâmetros válidos...');
        try {
            const withParamsResponse = await axios.get(
                `${BASE_URL}/movimentacoes-material/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, 
                { headers }
            );
            console.log('✅ Funcionou com parâmetros:', withParamsResponse.status);
        } catch (error) {
            console.log('❌ Erro com parâmetros:');
            console.log('Status:', error.response?.status);
            console.log('Dados:', error.response?.data);
        }
        
        // 4. Testar outros endpoints com @PreAuthorize para comparar
        console.log('\n4. Testando outros endpoints com @PreAuthorize...');
        
        // 4a. MaterialConsulta periodo
        console.log('\n4a. /api/materiais-consulta/periodo...');
        try {
            const mcPeriodoResponse = await axios.get(
                `${BASE_URL}/materiais-consulta/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, 
                { headers }
            );
            console.log('✅ MaterialConsulta periodo funcionou:', mcPeriodoResponse.status);
        } catch (error) {
            console.log('❌ Erro MaterialConsulta periodo:', error.response?.status, error.response?.data);
        }
        
        // 4b. Consulta periodo
        console.log('\n4b. /api/consultas/periodo...');
        try {
            const consultaPeriodoResponse = await axios.get(
                `${BASE_URL}/consultas/periodo?dataInicio=2024-01-01&dataFim=2024-12-31`, 
                { headers }
            );
            console.log('✅ Consulta periodo funcionou:', consultaPeriodoResponse.status);
        } catch (error) {
            console.log('❌ Erro Consulta periodo:', error.response?.status, error.response?.data);
        }
        
    } catch (error) {
        console.error('❌ Erro geral:', error.response?.data || error.message);
    }
}

testEndpointDirect();