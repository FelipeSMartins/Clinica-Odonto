import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Alert,
  CircularProgress,
  Tooltip,
  InputAdornment,
  Card,
  CardContent,
  Tabs,
  Tab
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Search as SearchIcon,
  Visibility as VisibilityIcon,
  Warning as WarningIcon,
  Inventory as InventoryIcon,
  TrendingUp as TrendingUpIcon,
  Category as CategoryIcon,
  Update as UpdateIcon
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import materialService from '../services/materialService';
import movimentacaoMaterialService from '../services/movimentacaoMaterialService';

const Materiais = () => {
  const { user } = useAuth();
  const [materiais, setMateriais] = useState([]);
  const [movimentacoes, setMovimentacoes] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [tabValue, setTabValue] = useState(0);
  
  // Estados do modal
  const [openModal, setOpenModal] = useState(false);
  const [modalMode, setModalMode] = useState('create'); // 'create', 'edit', 'view', 'stock'
  const [selectedMaterial, setSelectedMaterial] = useState(null);
  
  // Estados do formulário
  const [formData, setFormData] = useState({
    nome: '',
    codigo: '',
    categoria: '',
    unidadeMedida: '',
    estoqueAtual: '',
    estoqueMinimo: '',
    precoUnitario: '',
    descricao: ''
  });

  // Estados para movimentação
  const [movimentacaoData, setMovimentacaoData] = useState({
    materialId: '',
    tipoMovimentacao: '',
    quantidade: '',
    observacoes: ''
  });

  useEffect(() => {
    carregarDados();
  }, []);

  const carregarDados = async () => {
    try {
      setLoading(true);
      await Promise.all([
        carregarMateriais(),
        carregarCategorias(),
        carregarMovimentacoes()
      ]);
      setError('');
    } catch (err) {
      setError('Erro ao carregar dados');
      console.error('Erro:', err);
    } finally {
      setLoading(false);
    }
  };

  const carregarMateriais = async () => {
    const data = await materialService.listarTodos();
    setMateriais(data);
  };

  const carregarCategorias = async () => {
    const data = await materialService.listarCategorias();
    setCategorias(data);
  };

  const carregarMovimentacoes = async () => {
    const data = await movimentacaoMaterialService.listarTodas();
    setMovimentacoes(data.slice(0, 10)); // Últimas 10 movimentações
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      carregarMateriais();
      return;
    }

    try {
      setLoading(true);
      const data = await materialService.buscarPorNome(searchTerm);
      setMateriais(data);
      setError('');
    } catch (err) {
      setError('Erro ao buscar materiais');
      console.error('Erro:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode, material = null) => {
    setModalMode(mode);
    setSelectedMaterial(material);
    
    if (mode === 'create') {
      setFormData({
        nome: '',
        codigo: '',
        categoria: '',
        unidadeMedida: '',
        estoqueAtual: '',
        estoqueMinimo: '',
        precoUnitario: '',
        descricao: ''
      });
    } else if (mode === 'edit' && material) {
      setFormData({
        nome: material.nome || '',
        codigo: material.codigo || '',
        categoria: material.categoria || '',
        unidadeMedida: material.unidadeMedida || '',
        estoqueAtual: material.estoqueAtual?.toString() || '',
        estoqueMinimo: material.estoqueMinimo?.toString() || '',
        precoUnitario: material.precoUnitario?.toString() || '',
        descricao: material.descricao || ''
      });
    } else if (mode === 'stock' && material) {
      setMovimentacaoData({
        materialId: material.id,
        tipoMovimentacao: '',
        quantidade: '',
        observacoes: ''
      });
    }
    
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedMaterial(null);
    setError('');
    setSuccess('');
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleMovimentacaoChange = (e) => {
    const { name, value } = e.target;
    setMovimentacaoData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async () => {
    try {
      if (modalMode === 'create') {
        await materialService.criar({
          ...formData,
          estoqueAtual: parseInt(formData.estoqueAtual),
          estoqueMinimo: parseInt(formData.estoqueMinimo),
          precoUnitario: parseFloat(formData.precoUnitario)
        });
        setSuccess('Material criado com sucesso!');
      } else if (modalMode === 'edit') {
        await materialService.atualizar(selectedMaterial.id, {
          ...formData,
          estoqueAtual: parseInt(formData.estoqueAtual),
          estoqueMinimo: parseInt(formData.estoqueMinimo),
          precoUnitario: parseFloat(formData.precoUnitario)
        });
        setSuccess('Material atualizado com sucesso!');
      } else if (modalMode === 'stock') {
        await movimentacaoMaterialService.registrar({
          ...movimentacaoData,
          quantidade: parseInt(movimentacaoData.quantidade)
        });
        setSuccess('Movimentação registrada com sucesso!');
      }
      
      await carregarDados();
      handleCloseModal();
    } catch (err) {
      setError('Erro ao salvar: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleToggleStatus = async (material) => {
    try {
      if (material.ativo) {
        await materialService.inativar(material.id);
        setSuccess('Material inativado com sucesso!');
      } else {
        await materialService.ativar(material.id);
        setSuccess('Material ativado com sucesso!');
      }
      await carregarMateriais();
    } catch (err) {
      setError('Erro ao alterar status: ' + (err.response?.data?.message || err.message));
    }
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('pt-BR');
  };

  const getStatusColor = (material) => {
    if (!material.ativo) return 'error';
    if (material.estoqueBaixo) return 'warning';
    return 'success';
  };

  const getStatusText = (material) => {
    if (!material.ativo) return 'Inativo';
    if (material.estoqueBaixo) return 'Estoque Baixo';
    return 'Ativo';
  };

  const materiaisAtivos = materiais.filter(m => m.ativo);
  const materiaisEstoqueBaixo = materiais.filter(m => m.ativo && m.estoqueBaixo);
  const valorTotalEstoque = materiaisAtivos.reduce((total, m) => total + (m.estoqueAtual * m.precoUnitario), 0);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Gestão de Materiais
      </Typography>

      {/* Cards de Resumo */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <InventoryIcon color="primary" sx={{ mr: 2 }} />
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Total de Materiais
                  </Typography>
                  <Typography variant="h5">
                    {materiaisAtivos.length}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <WarningIcon color="warning" sx={{ mr: 2 }} />
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Estoque Baixo
                  </Typography>
                  <Typography variant="h5">
                    {materiaisEstoqueBaixo.length}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <TrendingUpIcon color="success" sx={{ mr: 2 }} />
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Valor Total Estoque
                  </Typography>
                  <Typography variant="h5">
                    {formatCurrency(valorTotalEstoque)}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <CategoryIcon color="info" sx={{ mr: 2 }} />
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Categorias
                  </Typography>
                  <Typography variant="h5">
                    {categorias.length}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Alertas */}
      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError('')}>
          {error}
        </Alert>
      )}
      {success && (
        <Alert severity="success" sx={{ mb: 2 }} onClose={() => setSuccess('')}>
          {success}
        </Alert>
      )}

      <Paper sx={{ p: 2 }}>
        {/* Tabs */}
        <Tabs value={tabValue} onChange={(e, newValue) => setTabValue(newValue)} sx={{ mb: 2 }}>
          <Tab label="Materiais" />
          <Tab label="Movimentações" />
        </Tabs>

        {tabValue === 0 && (
          <>
            {/* Barra de Ações - Materiais */}
            <Box display="flex" justifyContent="space-between" alignItems="center" sx={{ mb: 2 }}>
              <Box display="flex" gap={2} alignItems="center">
                <TextField
                  size="small"
                  placeholder="Buscar materiais..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <SearchIcon />
                      </InputAdornment>
                    )
                  }}
                />
                <Button
                  variant="outlined"
                  onClick={handleSearch}
                  startIcon={<SearchIcon />}
                >
                  Buscar
                </Button>
              </Box>
              
              <Button
                variant="contained"
                startIcon={<AddIcon />}
                onClick={() => handleOpenModal('create')}
                disabled={!user?.authorities?.includes('ROLE_ADMIN')}
              >
                Novo Material
              </Button>
            </Box>

            {/* Tabela de Materiais */}
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Código</TableCell>
                    <TableCell>Nome</TableCell>
                    <TableCell>Categoria</TableCell>
                    <TableCell>Estoque Atual</TableCell>
                    <TableCell>Estoque Mínimo</TableCell>
                    <TableCell>Preço Unitário</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Ações</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {materiais.map((material) => (
                    <TableRow key={material.id}>
                      <TableCell>{material.codigo}</TableCell>
                      <TableCell>{material.nome}</TableCell>
                      <TableCell>{material.categoria}</TableCell>
                      <TableCell>
                        {material.estoqueAtual} {material.unidadeMedida}
                      </TableCell>
                      <TableCell>
                        {material.estoqueMinimo} {material.unidadeMedida}
                      </TableCell>
                      <TableCell>{formatCurrency(material.precoUnitario)}</TableCell>
                      <TableCell>
                        <Chip
                          label={getStatusText(material)}
                          color={getStatusColor(material)}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>
                        <Tooltip title="Visualizar">
                          <IconButton
                            size="small"
                            onClick={() => handleOpenModal('view', material)}
                          >
                            <VisibilityIcon />
                          </IconButton>
                        </Tooltip>
                        
                        <Tooltip title="Editar">
                          <IconButton
                            size="small"
                            onClick={() => handleOpenModal('edit', material)}
                            disabled={!user?.authorities?.includes('ROLE_ADMIN')}
                          >
                            <EditIcon />
                          </IconButton>
                        </Tooltip>
                        
                        <Tooltip title="Movimentar Estoque">
                          <IconButton
                            size="small"
                            onClick={() => handleOpenModal('stock', material)}
                            disabled={!user?.authorities?.includes('ROLE_ADMIN') && !user?.authorities?.includes('ROLE_DENTISTA')}
                          >
                            <UpdateIcon />
                          </IconButton>
                        </Tooltip>
                        
                        <Tooltip title={material.ativo ? 'Inativar' : 'Ativar'}>
                          <IconButton
                            size="small"
                            onClick={() => handleToggleStatus(material)}
                            disabled={!user?.authorities?.includes('ROLE_ADMIN')}
                          >
                            <DeleteIcon color={material.ativo ? 'error' : 'success'} />
                          </IconButton>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </>
        )}

        {tabValue === 1 && (
          <>
            {/* Tabela de Movimentações */}
            <Typography variant="h6" gutterBottom>
              Últimas Movimentações
            </Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Data</TableCell>
                    <TableCell>Material</TableCell>
                    <TableCell>Tipo</TableCell>
                    <TableCell>Quantidade</TableCell>
                    <TableCell>Estoque Anterior</TableCell>
                    <TableCell>Estoque Atual</TableCell>
                    <TableCell>Usuário</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {movimentacoes.map((mov) => (
                    <TableRow key={mov.id}>
                      <TableCell>{formatDate(mov.dataMovimentacao)}</TableCell>
                      <TableCell>{mov.materialNome}</TableCell>
                      <TableCell>
                        <Chip
                          label={mov.tipoMovimentacaoDescricao}
                          color={mov.tipoMovimentacao === 'ENTRADA' ? 'success' : 'error'}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>{mov.quantidade}</TableCell>
                      <TableCell>{mov.estoqueAnterior}</TableCell>
                      <TableCell>{mov.estoqueAtual}</TableCell>
                      <TableCell>{mov.usuarioNome}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </>
        )}
      </Paper>

      {/* Modal */}
      <Dialog open={openModal} onClose={handleCloseModal} maxWidth="md" fullWidth>
        <DialogTitle>
          {modalMode === 'create' && 'Novo Material'}
          {modalMode === 'edit' && 'Editar Material'}
          {modalMode === 'view' && 'Visualizar Material'}
          {modalMode === 'stock' && 'Movimentar Estoque'}
        </DialogTitle>
        
        <DialogContent>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}
          
          {modalMode === 'stock' ? (
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}>
                <Typography variant="body1">
                  <strong>Material:</strong> {selectedMaterial?.nome}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Estoque atual: {selectedMaterial?.estoqueAtual} {selectedMaterial?.unidadeMedida}
                </Typography>
              </Grid>
              
              <Grid item xs={12} sm={6}>
                <FormControl fullWidth>
                  <InputLabel>Tipo de Movimentação</InputLabel>
                  <Select
                    name="tipoMovimentacao"
                    value={movimentacaoData.tipoMovimentacao}
                    onChange={handleMovimentacaoChange}
                    label="Tipo de Movimentação"
                  >
                    <MenuItem value="ENTRADA">Entrada</MenuItem>
                    <MenuItem value="SAIDA">Saída</MenuItem>
                    <MenuItem value="AJUSTE">Ajuste</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Quantidade"
                  name="quantidade"
                  type="number"
                  value={movimentacaoData.quantidade}
                  onChange={handleMovimentacaoChange}
                />
              </Grid>
              
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Observações"
                  name="observacoes"
                  multiline
                  rows={3}
                  value={movimentacaoData.observacoes}
                  onChange={handleMovimentacaoChange}
                />
              </Grid>
            </Grid>
          ) : (
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Nome"
                  name="nome"
                  value={formData.nome}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  required
                />
              </Grid>
              
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Código"
                  name="codigo"
                  value={formData.codigo}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  required
                />
              </Grid>
              
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Categoria"
                  name="categoria"
                  value={formData.categoria}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  required
                />
              </Grid>
              
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Unidade de Medida"
                  name="unidadeMedida"
                  value={formData.unidadeMedida}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  required
                />
              </Grid>
              
              <Grid item xs={12} sm={4}>
                <TextField
                  fullWidth
                  label="Estoque Atual"
                  name="estoqueAtual"
                  type="number"
                  value={formData.estoqueAtual}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  required
                />
              </Grid>
              
              <Grid item xs={12} sm={4}>
                <TextField
                  fullWidth
                  label="Estoque Mínimo"
                  name="estoqueMinimo"
                  type="number"
                  value={formData.estoqueMinimo}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  required
                />
              </Grid>
              
              <Grid item xs={12} sm={4}>
                <TextField
                  fullWidth
                  label="Preço Unitário"
                  name="precoUnitario"
                  type="number"
                  step="0.01"
                  value={formData.precoUnitario}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  required
                />
              </Grid>
              
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Descrição"
                  name="descricao"
                  multiline
                  rows={3}
                  value={formData.descricao}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              {modalMode === 'view' && selectedMaterial && (
                <>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Data de Cadastro"
                      value={formatDate(selectedMaterial.dataCadastro)}
                      disabled
                    />
                  </Grid>
                  
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Última Atualização"
                      value={formatDate(selectedMaterial.dataAtualizacao)}
                      disabled
                    />
                  </Grid>
                </>
              )}
            </Grid>
          )}
        </DialogContent>
        
        <DialogActions>
          <Button onClick={handleCloseModal}>
            Cancelar
          </Button>
          {modalMode !== 'view' && (
            <Button onClick={handleSubmit} variant="contained">
              {modalMode === 'create' && 'Criar'}
              {modalMode === 'edit' && 'Salvar'}
              {modalMode === 'stock' && 'Registrar'}
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Materiais;