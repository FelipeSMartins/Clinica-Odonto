const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

async function testPeriodoWithValidParams() {
    try {
        console.log('=== Teste de Endpoint /periodo com Parâmetros Válidos ===\n');
        
        // 1. Login como ADMIN
        console.log('1. Fazendo login como ADMIN...');
        const loginResponse = await axios.post(`${BASE_URL}/api/auth/login`, {
            email: 'admin@clinica.com',
            senha: 'admin123'
        });
        
        const token = loginResponse.data.token;
        console.log('✓ Login realizado com sucesso');
        console.log('Token:', token.substring(0, 20) + '...');
        
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
        
        // 2. Verificar informações do usuário
        console.log('\n2. Verificando informações do usuário...');
        const userResponse = await axios.get(`${BASE_URL}/api/auth/me`, { headers });
        console.log('✓ Usuário:', userResponse.data.email);
        console.log('✓ Authorities:', userResponse.data.authorities);
        
        // 3. Testar endpoint básico que funciona
        console.log('\n3. Testando endpoint básico /api/movimentacoes-material...');
        try {
            const basicResponse = await axios.get(`${BASE_URL}/api/movimentacoes-material`, { headers });
            console.log('✓ Endpoint básico funcionou - Status:', basicResponse.status);
        } catch (error) {
            console.log('✗ Endpoint básico falhou:', error.response?.status, error.response?.data?.message);
        }
        
        // 4. Testar /periodo com parâmetros válidos
        console.log('\n4. Testando /api/movimentacoes-material/periodo com parâmetros válidos...');
        const dataInicio = '2024-01-01';
        const dataFim = '2024-12-31';
        
        try {
            const periodoResponse = await axios.get(
                `${BASE_URL}/api/movimentacoes-material/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`, 
                { headers }
            );
            console.log('✓ Endpoint /periodo funcionou - Status:', periodoResponse.status);
            console.log('✓ Dados retornados:', periodoResponse.data.length, 'registros');
        } catch (error) {
            console.log('✗ Endpoint /periodo falhou:');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
            console.log('  Headers de resposta:', error.response?.headers);
        }
        
        // 5. Testar /periodo com datas diferentes
        console.log('\n5. Testando /periodo com datas mais recentes...');
        const dataInicio2 = '2024-12-01';
        const dataFim2 = '2024-12-31';
        
        try {
            const periodoResponse2 = await axios.get(
                `${BASE_URL}/api/movimentacoes-material/periodo?dataInicio=${dataInicio2}&dataFim=${dataFim2}`, 
                { headers }
            );
            console.log('✓ Endpoint /periodo (datas recentes) funcionou - Status:', periodoResponse2.status);
        } catch (error) {
            console.log('✗ Endpoint /periodo (datas recentes) falhou:');
            console.log('  Status:', error.response?.status);
            console.log('  Mensagem:', error.response?.data?.message || error.response?.data);
        }
        
        // 6. Testar outros endpoints /periodo para comparação
        console.log('\n6. Testando outros endpoints /periodo...');
        
        // MaterialConsulta periodo
        try {
            const materialConsultaPeriodo = await axios.get(
                `${BASE_URL}/api/materiais-consulta/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`, 
                { headers }
            );
            console.log('✓ /api/materiais-consulta/periodo funcionou - Status:', materialConsultaPeriodo.status);
        } catch (error) {
            console.log('✗ /api/materiais-consulta/periodo falhou:', error.response?.status, error.response?.data?.message);
        }
        
        // Consulta periodo
        try {
            const consultaPeriodo = await axios.get(
                `${BASE_URL}/api/consultas/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`, 
                { headers }
            );
            console.log('✓ /api/consultas/periodo funcionou - Status:', consultaPeriodo.status);
        } catch (error) {
            console.log('✗ /api/consultas/periodo falhou:', error.response?.status, error.response?.data?.message);
        }
        
    } catch (error) {
        console.error('Erro durante o teste:', error.message);
        if (error.response) {
            console.error('Status:', error.response.status);
            console.error('Data:', error.response.data);
        }
    }
}

testPeriodoWithValidParams();