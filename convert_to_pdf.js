const fs = require('fs');
const path = require('path');

// Script para converter Markdown para PDF
// Este script pode ser executado com Node.js se você tiver as dependências instaladas

console.log('='.repeat(60));
console.log('📄 CONVERSOR MARKDOWN PARA PDF');
console.log('='.repeat(60));
console.log();

const markdownFile = path.join(__dirname, 'GUIA_DESENVOLVIMENTO.md');
const outputFile = path.join(__dirname, 'GUIA_DESENVOLVIMENTO.pdf');

console.log('📁 Arquivo Markdown:', markdownFile);
console.log('📄 Arquivo PDF de saída:', outputFile);
console.log();

// Verificar se o arquivo Markdown existe
if (!fs.existsSync(markdownFile)) {
    console.error('❌ Erro: Arquivo GUIA_DESENVOLVIMENTO.md não encontrado!');
    process.exit(1);
}

console.log('✅ Arquivo Markdown encontrado!');
console.log();

console.log('🔧 OPÇÕES PARA CONVERTER PARA PDF:');
console.log();

console.log('1️⃣  OPÇÃO 1 - Usando markdown-pdf (Node.js):');
console.log('   npm install -g markdown-pdf');
console.log('   markdown-pdf GUIA_DESENVOLVIMENTO.md');
console.log();

console.log('2️⃣  OPÇÃO 2 - Usando Pandoc:');
console.log('   # Instalar Pandoc: https://pandoc.org/installing.html');
console.log('   pandoc GUIA_DESENVOLVIMENTO.md -o GUIA_DESENVOLVIMENTO.pdf');
console.log();

console.log('3️⃣  OPÇÃO 3 - Online (Recomendado):');
console.log('   • Acesse: https://www.markdowntopdf.com/');
console.log('   • Ou: https://md2pdf.netlify.app/');
console.log('   • Faça upload do arquivo GUIA_DESENVOLVIMENTO.md');
console.log('   • Baixe o PDF gerado');
console.log();

console.log('4️⃣  OPÇÃO 4 - VSCode:');
console.log('   • Instale a extensão "Markdown PDF"');
console.log('   • Abra o arquivo .md no VSCode');
console.log('   • Ctrl+Shift+P > "Markdown PDF: Export (pdf)"');
console.log();

console.log('5️⃣  OPÇÃO 5 - Chrome/Edge:');
console.log('   • Abra o arquivo .md em um visualizador online');
console.log('   • Use Ctrl+P > "Salvar como PDF"');
console.log();

console.log('='.repeat(60));
console.log('📋 O arquivo GUIA_DESENVOLVIMENTO.md está pronto!');
console.log('📍 Localização:', markdownFile);
console.log('='.repeat(60));

// Mostrar informações do arquivo
const stats = fs.statSync(markdownFile);
console.log();
console.log('📊 INFORMAÇÕES DO ARQUIVO:');
console.log('   Tamanho:', Math.round(stats.size / 1024), 'KB');
console.log('   Criado em:', stats.birthtime.toLocaleString('pt-BR'));
console.log('   Linhas:', fs.readFileSync(markdownFile, 'utf8').split('\n').length);
console.log();

console.log('🎯 RECOMENDAÇÃO:');
console.log('   Use a OPÇÃO 3 (online) para uma conversão rápida e fácil!');
console.log('   Ou a OPÇÃO 4 (VSCode) se você já tem o VSCode instalado.');
console.log();