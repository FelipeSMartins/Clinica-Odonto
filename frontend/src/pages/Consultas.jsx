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
  TablePagination,
  TextField,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  Chip,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  Snackbar,
  Tooltip,
  Menu,
  ListItemIcon,
  ListItemText,
  Divider
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Visibility as ViewIcon,
  Delete as DeleteIcon,
  Search as SearchIcon,
  Clear as ClearIcon,
  MoreVert as MoreVertIcon,
  CheckCircle as CheckCircleIcon,
  PlayArrow as PlayArrowIcon,
  Stop as StopIcon,
  Cancel as CancelIcon,
  Inventory as InventoryIcon,
  Remove as RemoveIcon
} from '@mui/icons-material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { ptBR } from 'date-fns/locale';
import { format } from 'date-fns';
import consultaService from '../services/consultaService';
import pacienteService from '../services/pacienteService';
import dentistaService from '../services/dentistaService';
import materialService from '../services/materialService';
import materialConsultaService from '../services/materialConsultaService';
import { useAuth } from '../contexts/AuthContext';

const Consultas = () => {
  const { user } = useAuth();
  const [consultas, setConsultas] = useState([]);
  const [pacientes, setPacientes] = useState([]);
  const [dentistas, setDentistas] = useState([]);
  const [materiais, setMateriais] = useState([]);
  const [materiaisConsulta, setMateriaisConsulta] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingMateriais, setLoadingMateriais] = useState(false);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchType, setSearchType] = useState('paciente');
  const [statusFilter, setStatusFilter] = useState('');
  const [dataFilter, setDataFilter] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMode, setDialogMode] = useState('create');
  const [selectedConsulta, setSelectedConsulta] = useState(null);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
  const [anchorEl, setAnchorEl] = useState(null);
  const [menuConsulta, setMenuConsulta] = useState(null);
  const [openMaterialDialog, setOpenMaterialDialog] = useState(false);
  const [materialForm, setMaterialForm] = useState({
    materialId: '',
    quantidade: '',
    observacoes: ''
  });

  const [formData, setFormData] = useState({
    pacienteId: '',
    dentistaId: '',
    dataHora: null,
    procedimento: '',
    observacoes: '',
    valor: '',
    status: 'AGENDADA'
  });

  const statusOptions = [
    { value: 'AGENDADA', label: 'Agendada' },
    { value: 'CONFIRMADA', label: 'Confirmada' },
    { value: 'EM_ANDAMENTO', label: 'Em Andamento' },
    { value: 'CONCLUIDA', label: 'Concluída' },
    { value: 'CANCELADA', label: 'Cancelada' }
  ];

  useEffect(() => {
    carregarDados();
  }, [page, rowsPerPage]);

  useEffect(() => {
    carregarPacientesEDentistas();
  }, []);

  const carregarDados = async () => {
    try {
      setLoading(true);
      const response = await consultaService.listarComPaginacao(page, rowsPerPage, 'dataHora,desc');
      setConsultas(response.content);
      setTotalElements(response.totalElements);
    } catch (error) {
      console.error('Erro ao carregar consultas:', error);
      showSnackbar('Erro ao carregar consultas', 'error');
    } finally {
      setLoading(false);
    }
  };

  const carregarPacientesEDentistas = async () => {
    try {
      const [pacientesData, dentistasData, materiaisData] = await Promise.all([
        pacienteService.listarTodos(),
        dentistaService.listarTodos(),
        materialService.listarTodos()
      ]);
      setPacientes(pacientesData.filter(p => p.ativo));
      setDentistas(dentistasData.filter(d => d.ativo));
      setMateriais(materiaisData.filter(m => m.ativo));
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    }
  };

  const handleSearch = async () => {
    try {
      setLoading(true);
      let result = [];

      if (searchTerm) {
        switch (searchType) {
          case 'paciente':
            const paciente = pacientes.find(p => 
              p.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
              p.cpf.includes(searchTerm)
            );
            if (paciente) {
              result = await consultaService.buscarPorPaciente(paciente.id);
            }
            break;
          case 'dentista':
            const dentista = dentistas.find(d => 
              d.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
              d.cro.includes(searchTerm)
            );
            if (dentista) {
              result = await consultaService.buscarPorDentista(dentista.id);
            }
            break;
          case 'procedimento':
            const todasConsultas = await consultaService.listarTodas();
            result = todasConsultas.filter(c => 
              c.procedimento.toLowerCase().includes(searchTerm.toLowerCase())
            );
            break;
        }
      } else if (statusFilter) {
        result = await consultaService.buscarPorStatus(statusFilter);
      } else if (dataFilter) {
        const dataFormatada = format(dataFilter, 'yyyy-MM-dd');
        result = await consultaService.buscarPorData(dataFormatada);
      } else {
        carregarDados();
        return;
      }

      setConsultas(result);
      setTotalElements(result.length);
      setPage(0);
    } catch (error) {
      console.error('Erro na busca:', error);
      showSnackbar('Erro ao realizar busca', 'error');
    } finally {
      setLoading(false);
    }
  };

  const clearSearch = () => {
    setSearchTerm('');
    setStatusFilter('');
    setDataFilter(null);
    setPage(0);
    carregarDados();
  };

  const handleOpenDialog = async (mode, consulta = null) => {
    setDialogMode(mode);
    setSelectedConsulta(consulta);
    
    if (mode === 'create') {
      setFormData({
        pacienteId: '',
        dentistaId: '',
        dataHora: null,
        procedimento: '',
        observacoes: '',
        valor: '',
        status: 'AGENDADA'
      });
      setMateriaisConsulta([]);
    } else if (consulta) {
      setFormData({
        pacienteId: consulta.pacienteId,
        dentistaId: consulta.dentistaId,
        dataHora: new Date(consulta.dataHora),
        procedimento: consulta.procedimento,
        observacoes: consulta.observacoes || '',
        valor: consulta.valor?.toString() || '',
        status: consulta.status
      });
      // Carregar materiais da consulta se existir
      if (consulta.id) {
        await carregarMateriaisConsulta(consulta.id);
      }
    }
    
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedConsulta(null);
  };

  const handleSubmit = async () => {
    try {
      const consultaData = {
        ...formData,
        dataHora: formData.dataHora?.toISOString(),
        valor: formData.valor ? parseFloat(formData.valor) : null
      };

      if (dialogMode === 'create') {
        await consultaService.criar(consultaData);
        showSnackbar('Consulta agendada com sucesso!', 'success');
      } else if (dialogMode === 'edit') {
        await consultaService.atualizar(selectedConsulta.id, consultaData);
        showSnackbar('Consulta atualizada com sucesso!', 'success');
      }

      handleCloseDialog();
      carregarDados();
    } catch (error) {
      console.error('Erro ao salvar consulta:', error);
      showSnackbar('Erro ao salvar consulta', 'error');
    }
  };

  const handleStatusChange = async (consultaId, novoStatus) => {
    try {
      await consultaService.alterarStatus(consultaId, novoStatus);
      showSnackbar(`Status alterado para ${consultaService.formatarStatus(novoStatus)}`, 'success');
      carregarDados();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
      showSnackbar('Erro ao alterar status', 'error');
    }
    setAnchorEl(null);
  };

  const handleDelete = async (consultaId) => {
    if (window.confirm('Tem certeza que deseja cancelar esta consulta?')) {
      try {
        await consultaService.cancelar(consultaId);
        showSnackbar('Consulta cancelada com sucesso!', 'success');
        carregarDados();
      } catch (error) {
        console.error('Erro ao cancelar consulta:', error);
        showSnackbar('Erro ao cancelar consulta', 'error');
      }
    }
  };

  const showSnackbar = (message, severity = 'success') => {
    setSnackbar({ open: true, message, severity });
  };

  const carregarMateriaisConsulta = async (consultaId) => {
    try {
      setLoadingMateriais(true);
      const materiais = await materialConsultaService.buscarPorConsulta(consultaId);
      setMateriaisConsulta(materiais);
    } catch (error) {
      console.error('Erro ao carregar materiais da consulta:', error);
      setSnackbar({
        open: true,
        message: 'Erro ao carregar materiais da consulta',
        severity: 'error'
      });
    } finally {
      setLoadingMateriais(false);
    }
  };

  const handleOpenMaterialDialog = () => {
    setMaterialForm({
      materialId: '',
      quantidade: '',
      observacoes: ''
    });
    setOpenMaterialDialog(true);
  };

  const handleCloseMaterialDialog = () => {
    setOpenMaterialDialog(false);
    setMaterialForm({
      materialId: '',
      quantidade: '',
      observacoes: ''
    });
  };

  const handleAddMaterial = async () => {
    if (!selectedConsulta?.id || !materialForm.materialId || !materialForm.quantidade) {
      showSnackbar('Preencha todos os campos obrigatórios', 'error');
      return;
    }

    try {
      const materialData = {
        consultaId: selectedConsulta.id,
        materialId: materialForm.materialId,
        quantidadeUtilizada: parseFloat(materialForm.quantidade),
        observacoes: materialForm.observacoes
      };

      await materialConsultaService.registrar(materialData);
      await carregarMateriaisConsulta(selectedConsulta.id);
      handleCloseMaterialDialog();
      showSnackbar('Material adicionado com sucesso!');
    } catch (error) {
      console.error('Erro ao adicionar material:', error);
      showSnackbar('Erro ao adicionar material', 'error');
    }
  };

  const handleRemoveMaterial = async (materialConsultaId) => {
    try {
      await materialConsultaService.remover(materialConsultaId);
      await carregarMateriaisConsulta(selectedConsulta.id);
      showSnackbar('Material removido com sucesso!');
    } catch (error) {
      console.error('Erro ao remover material:', error);
      showSnackbar('Erro ao remover material', 'error');
    }
  };

  const canEdit = () => {
    return user?.tipo === 'ADMIN' || user?.tipo === 'RECEPCIONISTA';
  };

  const canChangeStatus = () => {
    return user?.tipo === 'ADMIN' || user?.tipo === 'DENTISTA' || user?.tipo === 'RECEPCIONISTA';
  };

  const handleMenuOpen = (event, consulta) => {
    setAnchorEl(event.currentTarget);
    setMenuConsulta(consulta);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    setMenuConsulta(null);
  };

  const formatDateTime = (dateTime) => {
    return format(new Date(dateTime), 'dd/MM/yyyy HH:mm');
  };

  const formatCurrency = (value) => {
    return value ? `R$ ${value.toFixed(2).replace('.', ',')}` : '-';
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ptBR}>
      <Box sx={{ p: 3 }}>
        <Typography variant="h4" gutterBottom>
          Gestão de Consultas
        </Typography>

        {/* Filtros e Busca */}
        <Paper sx={{ p: 2, mb: 3 }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} md={3}>
              <FormControl fullWidth size="small">
                <InputLabel>Buscar por</InputLabel>
                <Select
                  value={searchType}
                  onChange={(e) => setSearchType(e.target.value)}
                  label="Buscar por"
                >
                  <MenuItem value="paciente">Paciente</MenuItem>
                  <MenuItem value="dentista">Dentista</MenuItem>
                  <MenuItem value="procedimento">Procedimento</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                size="small"
                label="Termo de busca"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder={`Buscar por ${searchType}...`}
              />
            </Grid>
            <Grid item xs={12} md={2}>
              <FormControl fullWidth size="small">
                <InputLabel>Status</InputLabel>
                <Select
                  value={statusFilter}
                  onChange={(e) => setStatusFilter(e.target.value)}
                  label="Status"
                >
                  <MenuItem value="">Todos</MenuItem>
                  {statusOptions.map(option => (
                    <MenuItem key={option.value} value={option.value}>
                      {option.label}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={2}>
              <DateTimePicker
                label="Data"
                value={dataFilter}
                onChange={setDataFilter}
                slotProps={{
                  textField: {
                    size: 'small',
                    fullWidth: true
                  }
                }}
              />
            </Grid>
            <Grid item xs={12} md={2}>
              <Box sx={{ display: 'flex', gap: 1 }}>
                <Button
                  variant="contained"
                  onClick={handleSearch}
                  startIcon={<SearchIcon />}
                  size="small"
                >
                  Buscar
                </Button>
                <Button
                  variant="outlined"
                  onClick={clearSearch}
                  startIcon={<ClearIcon />}
                  size="small"
                >
                  Limpar
                </Button>
              </Box>
            </Grid>
          </Grid>
        </Paper>

        {/* Botão Adicionar */}
        {canEdit() && (
          <Box sx={{ mb: 2 }}>
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={() => handleOpenDialog('create')}
            >
              Agendar Consulta
            </Button>
          </Box>
        )}

        {/* Tabela */}
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Data/Hora</TableCell>
                <TableCell>Paciente</TableCell>
                <TableCell>Dentista</TableCell>
                <TableCell>Procedimento</TableCell>
                <TableCell>Valor</TableCell>
                <TableCell>Status</TableCell>
                <TableCell align="center">Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={7} align="center">
                    Carregando...
                  </TableCell>
                </TableRow>
              ) : consultas.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={7} align="center">
                    Nenhuma consulta encontrada
                  </TableCell>
                </TableRow>
              ) : (
                consultas.map((consulta) => (
                  <TableRow key={consulta.id}>
                    <TableCell>{formatDateTime(consulta.dataHora)}</TableCell>
                    <TableCell>
                      <Box>
                        <Typography variant="body2" fontWeight="bold">
                          {consulta.pacienteNome}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          CPF: {consulta.pacienteCpf}
                        </Typography>
                      </Box>
                    </TableCell>
                    <TableCell>
                      <Box>
                        <Typography variant="body2" fontWeight="bold">
                          {consulta.dentistaNome}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          CRO: {consulta.dentistaCro}
                        </Typography>
                      </Box>
                    </TableCell>
                    <TableCell>{consulta.procedimento}</TableCell>
                    <TableCell>{formatCurrency(consulta.valor)}</TableCell>
                    <TableCell>
                      <Chip
                        label={consultaService.formatarStatus(consulta.status)}
                        color={consultaService.getStatusColor(consulta.status)}
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                        <Tooltip title="Visualizar">
                          <IconButton
                            size="small"
                            onClick={() => handleOpenDialog('view', consulta)}
                          >
                            <ViewIcon />
                          </IconButton>
                        </Tooltip>
                        
                        {canEdit() && consulta.status !== 'CANCELADA' && consulta.status !== 'CONCLUIDA' && (
                          <Tooltip title="Editar">
                            <IconButton
                              size="small"
                              onClick={() => handleOpenDialog('edit', consulta)}
                            >
                              <EditIcon />
                            </IconButton>
                          </Tooltip>
                        )}
              
              {/* Seção de Materiais */}
              {selectedConsulta?.id && (
                <Grid item xs={12}>
                  <Box sx={{ mt: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
                      <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <InventoryIcon />
                        Materiais Utilizados
                      </Typography>
                      {dialogMode !== 'view' && (
                        <Button
                          variant="outlined"
                          startIcon={<AddIcon />}
                          onClick={handleOpenMaterialDialog}
                          size="small"
                        >
                          Adicionar Material
                        </Button>
                      )}
                    </Box>
                    
                    {loadingMateriais ? (
                      <Typography>Carregando materiais...</Typography>
                    ) : materiaisConsulta.length > 0 ? (
                      <TableContainer component={Paper} variant="outlined">
                        <Table size="small">
                          <TableHead>
                            <TableRow>
                              <TableCell>Material</TableCell>
                              <TableCell align="right">Quantidade</TableCell>
                              <TableCell align="right">Preço Unit.</TableCell>
                              <TableCell align="right">Total</TableCell>
                              {dialogMode !== 'view' && <TableCell align="center">Ações</TableCell>}
                            </TableRow>
                          </TableHead>
                          <TableBody>
                            {materiaisConsulta.map((item) => (
                              <TableRow key={item.id}>
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
                                {dialogMode !== 'view' && (
                                  <TableCell align="center">
                                    <IconButton
                                      size="small"
                                      color="error"
                                      onClick={() => handleRemoveMaterial(item.id)}
                                    >
                                      <RemoveIcon />
                                    </IconButton>
                                  </TableCell>
                                )}
                              </TableRow>
                            ))}
                            <TableRow>
                              <TableCell colSpan={3} sx={{ fontWeight: 'bold' }}>Total Geral:</TableCell>
                              <TableCell align="right" sx={{ fontWeight: 'bold' }}>
                                R$ {materiaisConsulta.reduce((total, item) => total + (item.valorTotal || 0), 0).toFixed(2)}
                              </TableCell>
                              {dialogMode !== 'view' && <TableCell />}
                            </TableRow>
                          </TableBody>
                        </Table>
                      </TableContainer>
                    ) : (
                      <Typography color="text.secondary" sx={{ textAlign: 'center', py: 2 }}>
                        Nenhum material utilizado nesta consulta
                      </Typography>
                    )}
                  </Box>
                </Grid>
              )}
                        
                        {canChangeStatus() && (
                          <Tooltip title="Ações">
                            <IconButton
                              size="small"
                              onClick={(e) => handleMenuOpen(e, consulta)}
                            >
                              <MoreVertIcon />
                            </IconButton>
                          </Tooltip>
                        )}
                        
                        {canEdit() && consulta.status !== 'CANCELADA' && (
                          <Tooltip title="Cancelar">
                            <IconButton
                              size="small"
                              color="error"
                              onClick={() => handleDelete(consulta.id)}
                            >
                              <DeleteIcon />
                            </IconButton>
                          </Tooltip>
                        )}
                      </Box>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
          
          <TablePagination
            component="div"
            count={totalElements}
            page={page}
            onPageChange={(e, newPage) => setPage(newPage)}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={(e) => {
              setRowsPerPage(parseInt(e.target.value, 10));
              setPage(0);
            }}
            labelRowsPerPage="Linhas por página:"
            labelDisplayedRows={({ from, to, count }) => 
              `${from}-${to} de ${count !== -1 ? count : `mais de ${to}`}`
            }
          />
        </TableContainer>

        {/* Menu de Ações */}
        <Menu
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleMenuClose}
        >
          {menuConsulta && consultaService.getProximosStatus(menuConsulta.status).map((statusOption) => (
            <MenuItem
              key={statusOption.value}
              onClick={() => handleStatusChange(menuConsulta.id, statusOption.value)}
            >
              <ListItemIcon>
                {statusOption.value === 'CONFIRMADA' && <CheckCircleIcon />}
                {statusOption.value === 'EM_ANDAMENTO' && <PlayArrowIcon />}
                {statusOption.value === 'CONCLUIDA' && <StopIcon />}
                {statusOption.value === 'CANCELADA' && <CancelIcon />}
              </ListItemIcon>
              <ListItemText>{statusOption.label}</ListItemText>
            </MenuItem>
          ))}
        </Menu>

        {/* Dialog */}
        <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
          <DialogTitle>
            {dialogMode === 'create' && 'Agendar Nova Consulta'}
            {dialogMode === 'edit' && 'Editar Consulta'}
            {dialogMode === 'view' && 'Detalhes da Consulta'}
          </DialogTitle>
          <DialogContent>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12} md={6}>
                <FormControl fullWidth disabled={dialogMode === 'view'}>
                  <InputLabel>Paciente *</InputLabel>
                  <Select
                    value={formData.pacienteId}
                    onChange={(e) => setFormData({ ...formData, pacienteId: e.target.value })}
                    label="Paciente *"
                  >
                    {pacientes.map(paciente => (
                      <MenuItem key={paciente.id} value={paciente.id}>
                        {paciente.nome} - {paciente.cpf}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} md={6}>
                <FormControl fullWidth disabled={dialogMode === 'view'}>
                  <InputLabel>Dentista *</InputLabel>
                  <Select
                    value={formData.dentistaId}
                    onChange={(e) => setFormData({ ...formData, dentistaId: e.target.value })}
                    label="Dentista *"
                  >
                    {dentistas.map(dentista => (
                      <MenuItem key={dentista.id} value={dentista.id}>
                        {dentista.nome} - {dentista.cro}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} md={6}>
                <DateTimePicker
                  label="Data e Hora *"
                  value={formData.dataHora}
                  onChange={(newValue) => setFormData({ ...formData, dataHora: newValue })}
                  disabled={dialogMode === 'view'}
                  slotProps={{
                    textField: {
                      fullWidth: true
                    }
                  }}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  fullWidth
                  label="Valor"
                  type="number"
                  value={formData.valor}
                  onChange={(e) => setFormData({ ...formData, valor: e.target.value })}
                  disabled={dialogMode === 'view'}
                  InputProps={{
                    startAdornment: 'R$'
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Procedimento *"
                  value={formData.procedimento}
                  onChange={(e) => setFormData({ ...formData, procedimento: e.target.value })}
                  disabled={dialogMode === 'view'}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Observações"
                  multiline
                  rows={3}
                  value={formData.observacoes}
                  onChange={(e) => setFormData({ ...formData, observacoes: e.target.value })}
                  disabled={dialogMode === 'view'}
                />
              </Grid>
              {dialogMode !== 'create' && (
                <Grid item xs={12} md={6}>
                  <FormControl fullWidth disabled={dialogMode === 'view'}>
                    <InputLabel>Status</InputLabel>
                    <Select
                      value={formData.status}
                      onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                      label="Status"
                    >
                      {statusOptions.map(option => (
                        <MenuItem key={option.value} value={option.value}>
                          {option.label}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
              )}
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancelar</Button>
            {dialogMode !== 'view' && (
              <Button onClick={handleSubmit} variant="contained">
                {dialogMode === 'create' ? 'Agendar' : 'Salvar'}
              </Button>
            )}
          </DialogActions>
        </Dialog>

        {/* Dialog para Adicionar Material */}
        <Dialog open={openMaterialDialog} onClose={handleCloseMaterialDialog} maxWidth="sm" fullWidth>
          <DialogTitle>Adicionar Material à Consulta</DialogTitle>
          <DialogContent>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}>
                <FormControl fullWidth>
                  <InputLabel>Material *</InputLabel>
                  <Select
                    value={materialForm.materialId}
                    onChange={(e) => setMaterialForm({ ...materialForm, materialId: e.target.value })}
                    label="Material *"
                  >
                    {materiais.map(material => (
                      <MenuItem key={material.id} value={material.id}>
                        {material.nome} - {material.codigo} (Estoque: {material.estoqueAtual} {material.unidadeMedida})
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Quantidade Utilizada *"
                  type="number"
                  value={materialForm.quantidade}
                  onChange={(e) => setMaterialForm({ ...materialForm, quantidade: e.target.value })}
                  inputProps={{ min: 0, step: 0.01 }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Observações"
                  multiline
                  rows={3}
                  value={materialForm.observacoes}
                  onChange={(e) => setMaterialForm({ ...materialForm, observacoes: e.target.value })}
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseMaterialDialog}>Cancelar</Button>
            <Button onClick={handleAddMaterial} variant="contained">
              Adicionar
            </Button>
          </DialogActions>
        </Dialog>

        {/* Snackbar */}
        <Snackbar
          open={snackbar.open}
          autoHideDuration={6000}
          onClose={() => setSnackbar({ ...snackbar, open: false })}
        >
          <Alert
            onClose={() => setSnackbar({ ...snackbar, open: false })}
            severity={snackbar.severity}
            sx={{ width: '100%' }}
          >
            {snackbar.message}
          </Alert>
        </Snackbar>
      </Box>
    </LocalizationProvider>
  );
};

export default Consultas;