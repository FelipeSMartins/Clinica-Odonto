import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  CardActions,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Alert,
  CircularProgress,
  Divider,
  IconButton,
  Tooltip
} from '@mui/material';
import {
  Assessment as AssessmentIcon,
  Inventory as InventoryIcon,
  TrendingDown as TrendingDownIcon,
  TrendingUp as TrendingUpIcon,
  Warning as WarningIcon,
  GetApp as DownloadIcon,
  Refresh as RefreshIcon,
  DateRange as DateRangeIcon
} from '@mui/icons-material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { ptBR } from 'date-fns/locale';
import { format, startOfMonth, endOfMonth, subMonths } from 'date-fns';
import materialService from '../services/materialService';
import movimentacaoMaterialService from '../services/movimentacaoMaterialService';
import materialConsultaService from '../services/materialConsultaService';
import { useAuth } from '../contexts/AuthContext';

const Relatorios = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [materiaisEstoqueBaixo, setMateriaisEstoqueBaixo] = useState([]);
  const [movimentacoes, setMovimentacoes] = useState([]);
  const [materiaisConsulta, setMateriaisConsulta] = useState([]);
  const [estatisticas, setEstatisticas] = useState({
    totalMateriais: 0,
    valorTotalEstoque: 0,
    materiaisEstoqueBaixo: 0,
    movimentacoesMes: 0,
    valorConsumidoMes: 0
  });
  
  const [filtros, setFiltros] = useState({
    dataInicio: startOfMonth(new Date()),
    dataFim: endOfMonth(new Date()),
    tipoRelatorio: 'estoque'
  });

  useEffect(() => {
    carregarDados();
  }, []);

  const carregarDados = async () => {
    setLoading(true);
    try {
      await Promise.all([
        carregarEstatisticas(),
        carregarMateriaisEstoqueBaixo(),
        carregarMovimentacoes(),
        carregarMateriaisConsulta()
      ]);
    } catch (error) {
      console.error('Erro ao carregar dados dos relatórios:', error);
    } finally {
      setLoading(false);
    }
  };

  const carregarEstatisticas = async () => {
    try {
      const materiais = await materialService.listarTodos();
      const valorTotalEstoque = materiais.reduce((total, material) => 
        total + (material.estoqueAtual * material.precoUnitario), 0
      );
      const materiaisEstoqueBaixo = materiais.filter(material => 
        material.estoqueAtual <= material.estoqueMinimo
      ).length;
      
      const movimentacoesMes = await movimentacaoMaterialService.contarMesAtual();
      const valorConsumidoMes = await materialConsultaService.calcularValorTotalMes();
      
      setEstatisticas({
        totalMateriais: materiais.length,
        valorTotalEstoque,
        materiaisEstoqueBaixo,
        movimentacoesMes,
        valorConsumidoMes
      });
    } catch (error) {
      console.error('Erro ao carregar estatísticas:', error);
    }
  };

  const carregarMateriaisEstoqueBaixo = async () => {
    try {
      const materiais = await materialService.listarTodos();
      const estoqueBaixo = materiais.filter(material => 
        material.estoqueAtual <= material.estoqueMinimo
      );
      setMateriaisEstoqueBaixo(estoqueBaixo);
    } catch (error) {
      console.error('Erro ao carregar materiais com estoque baixo:', error);
    }
  };

  const carregarMovimentacoes = async () => {
    try {
      const dataInicio = format(filtros.dataInicio, 'yyyy-MM-dd');
      const dataFim = format(filtros.dataFim, 'yyyy-MM-dd');
      const movimentacoes = await movimentacaoMaterialService.buscarPorPeriodo(dataInicio, dataFim);
      setMovimentacoes(movimentacoes);
    } catch (error) {
      console.error('Erro ao carregar movimentações:', error);
    }
  };

  const carregarMateriaisConsulta = async () => {
    try {
      const dataInicio = format(filtros.dataInicio, 'yyyy-MM-dd');
      const dataFim = format(filtros.dataFim, 'yyyy-MM-dd');
      const materiais = await materialConsultaService.buscarPorPeriodo(dataInicio, dataFim);
      setMateriaisConsulta(materiais);
    } catch (error) {
      console.error('Erro ao carregar materiais de consulta:', error);
    }
  };

  const handleFiltroChange = (campo, valor) => {
    setFiltros(prev => ({ ...prev, [campo]: valor }));
  };

  const aplicarFiltros = () => {
    carregarMovimentacoes();
    carregarMateriaisConsulta();
  };

  const exportarRelatorio = () => {
    // Implementar exportação para PDF/Excel
    console.log('Exportar relatório:', filtros.tipoRelatorio);
  };

  const renderCardEstatistica = (titulo, valor, icone, cor = 'primary') => (
    <Card>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Box>
            <Typography color="textSecondary" gutterBottom variant="body2">
              {titulo}
            </Typography>
            <Typography variant="h4" component="div">
              {typeof valor === 'number' && titulo.includes('R$') ? 
                `R$ ${valor.toFixed(2)}` : valor
              }
            </Typography>
          </Box>
          <Box sx={{ color: `${cor}.main` }}>
            {icone}
          </Box>
        </Box>
      </CardContent>
    </Card>
  );

  const renderRelatorioEstoque = () => (
    <Paper sx={{ p: 3, mt: 3 }}>
      <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <WarningIcon color="warning" />
        Materiais com Estoque Baixo
      </Typography>
      
      {materiaisEstoqueBaixo.length > 0 ? (
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Material</TableCell>
                <TableCell>Código</TableCell>
                <TableCell align="right">Estoque Atual</TableCell>
                <TableCell align="right">Estoque Mínimo</TableCell>
                <TableCell align="right">Diferença</TableCell>
                <TableCell>Status</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {materiaisEstoqueBaixo.map((material) => (
                <TableRow key={material.id}>
                  <TableCell>{material.nome}</TableCell>
                  <TableCell>{material.codigo}</TableCell>
                  <TableCell align="right">
                    {material.estoqueAtual} {material.unidadeMedida}
                  </TableCell>
                  <TableCell align="right">
                    {material.estoqueMinimo} {material.unidadeMedida}
                  </TableCell>
                  <TableCell align="right">
                    {material.estoqueAtual - material.estoqueMinimo} {material.unidadeMedida}
                  </TableCell>
                  <TableCell>
                    <Chip 
                      label={material.estoqueAtual === 0 ? 'Sem Estoque' : 'Estoque Baixo'}
                      color={material.estoqueAtual === 0 ? 'error' : 'warning'}
                      size="small"
                    />
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Alert severity="success">
          Todos os materiais estão com estoque adequado!
        </Alert>
      )}
    </Paper>
  );

  const renderRelatorioMovimentacoes = () => (
    <Paper sx={{ p: 3, mt: 3 }}>
      <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <TrendingUpIcon color="primary" />
        Movimentações de Estoque
      </Typography>
      
      {movimentacoes.length > 0 ? (
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Data</TableCell>
                <TableCell>Material</TableCell>
                <TableCell>Tipo</TableCell>
                <TableCell align="right">Quantidade</TableCell>
                <TableCell>Observações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {movimentacoes.map((movimentacao) => (
                <TableRow key={movimentacao.id}>
                  <TableCell>
                    {format(new Date(movimentacao.dataMovimentacao), 'dd/MM/yyyy HH:mm')}
                  </TableCell>
                  <TableCell>{movimentacao.material?.nome}</TableCell>
                  <TableCell>
                    <Chip 
                      label={movimentacao.tipoMovimentacao}
                      color={movimentacao.tipoMovimentacao === 'ENTRADA' ? 'success' : 'error'}
                      size="small"
                      icon={movimentacao.tipoMovimentacao === 'ENTRADA' ? 
                        <TrendingUpIcon /> : <TrendingDownIcon />
                      }
                    />
                  </TableCell>
                  <TableCell align="right">
                    {movimentacao.quantidade} {movimentacao.material?.unidadeMedida}
                  </TableCell>
                  <TableCell>{movimentacao.observacoes || '-'}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Alert severity="info">
          Nenhuma movimentação encontrada no período selecionado.
        </Alert>
      )}
    </Paper>
  );

  const renderRelatorioConsumo = () => (
    <Paper sx={{ p: 3, mt: 3 }}>
      <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <AssessmentIcon color="primary" />
        Consumo de Materiais em Consultas
      </Typography>
      
      {materiaisConsulta.length > 0 ? (
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Data</TableCell>
                <TableCell>Paciente</TableCell>
                <TableCell>Material</TableCell>
                <TableCell align="right">Quantidade</TableCell>
                <TableCell align="right">Valor Unit.</TableCell>
                <TableCell align="right">Valor Total</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {materiaisConsulta.map((item) => (
                <TableRow key={item.id}>
                  <TableCell>
                    {format(new Date(item.consulta?.dataHora), 'dd/MM/yyyy')}
                  </TableCell>
                  <TableCell>{item.consulta?.paciente?.nome}</TableCell>
                  <TableCell>{item.material?.nome}</TableCell>
                  <TableCell align="right">
                    {item.quantidadeUtilizada} {item.material?.unidadeMedida}
                  </TableCell>
                  <TableCell align="right">
                    R$ {item.precoUnitario?.toFixed(2)}
                  </TableCell>
                  <TableCell align="right">
                    R$ {item.valorTotal?.toFixed(2)}
                  </TableCell>
                </TableRow>
              ))}
              <TableRow>
                <TableCell colSpan={5} sx={{ fontWeight: 'bold' }}>Total Geral:</TableCell>
                <TableCell align="right" sx={{ fontWeight: 'bold' }}>
                  R$ {materiaisConsulta.reduce((total, item) => total + (item.valorTotal || 0), 0).toFixed(2)}
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Alert severity="info">
          Nenhum consumo de material encontrado no período selecionado.
        </Alert>
      )}
    </Paper>
  );

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ptBR}>
      <Box sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
          <Typography variant="h4" component="h1">
            Relatórios de Materiais
          </Typography>
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Tooltip title="Atualizar dados">
              <IconButton onClick={carregarDados}>
                <RefreshIcon />
              </IconButton>
            </Tooltip>
            <Button
              variant="outlined"
              startIcon={<DownloadIcon />}
              onClick={exportarRelatorio}
            >
              Exportar
            </Button>
          </Box>
        </Box>

        {/* Cards de Estatísticas */}
        <Grid container spacing={3} sx={{ mb: 3 }}>
          <Grid item xs={12} sm={6} md={2.4}>
            {renderCardEstatistica(
              'Total de Materiais',
              estatisticas.totalMateriais,
              <InventoryIcon fontSize="large" />
            )}
          </Grid>
          <Grid item xs={12} sm={6} md={2.4}>
            {renderCardEstatistica(
              'Valor Total do Estoque',
              estatisticas.valorTotalEstoque,
              <AssessmentIcon fontSize="large" />,
              'success'
            )}
          </Grid>
          <Grid item xs={12} sm={6} md={2.4}>
            {renderCardEstatistica(
              'Estoque Baixo',
              estatisticas.materiaisEstoqueBaixo,
              <WarningIcon fontSize="large" />,
              'warning'
            )}
          </Grid>
          <Grid item xs={12} sm={6} md={2.4}>
            {renderCardEstatistica(
              'Movimentações (Mês)',
              estatisticas.movimentacoesMes,
              <TrendingUpIcon fontSize="large" />,
              'info'
            )}
          </Grid>
          <Grid item xs={12} sm={6} md={2.4}>
            {renderCardEstatistica(
              'Valor Consumido (Mês)',
              estatisticas.valorConsumidoMes,
              <TrendingDownIcon fontSize="large" />,
              'error'
            )}
          </Grid>
        </Grid>

        {/* Filtros */}
        <Paper sx={{ p: 3, mb: 3 }}>
          <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <DateRangeIcon />
            Filtros
          </Typography>
          
          <Grid container spacing={3} alignItems="center">
            <Grid item xs={12} sm={6} md={3}>
              <FormControl fullWidth>
                <InputLabel>Tipo de Relatório</InputLabel>
                <Select
                  value={filtros.tipoRelatorio}
                  onChange={(e) => handleFiltroChange('tipoRelatorio', e.target.value)}
                  label="Tipo de Relatório"
                >
                  <MenuItem value="estoque">Estoque</MenuItem>
                  <MenuItem value="movimentacoes">Movimentações</MenuItem>
                  <MenuItem value="consumo">Consumo</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            
            {filtros.tipoRelatorio !== 'estoque' && (
              <>
                <Grid item xs={12} sm={6} md={3}>
                  <DatePicker
                    label="Data Início"
                    value={filtros.dataInicio}
                    onChange={(date) => handleFiltroChange('dataInicio', date)}
                    renderInput={(params) => <TextField {...params} fullWidth />}
                  />
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                  <DatePicker
                    label="Data Fim"
                    value={filtros.dataFim}
                    onChange={(date) => handleFiltroChange('dataFim', date)}
                    renderInput={(params) => <TextField {...params} fullWidth />}
                  />
                </Grid>
                <Grid item xs={12} sm={6} md={3}>
                  <Button
                    variant="contained"
                    onClick={aplicarFiltros}
                    fullWidth
                  >
                    Aplicar Filtros
                  </Button>
                </Grid>
              </>
            )}
          </Grid>
        </Paper>

        {/* Relatórios */}
        {filtros.tipoRelatorio === 'estoque' && renderRelatorioEstoque()}
        {filtros.tipoRelatorio === 'movimentacoes' && renderRelatorioMovimentacoes()}
        {filtros.tipoRelatorio === 'consumo' && renderRelatorioConsumo()}
      </Box>
    </LocalizationProvider>
  );
};

export default Relatorios;