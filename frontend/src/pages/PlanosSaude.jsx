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
  Switch,
  FormControlLabel
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Search as SearchIcon,
  Visibility as VisibilityIcon,
  ToggleOff as ToggleOffIcon,
  ToggleOn as ToggleOnIcon
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import planoSaudeService from '../services/planoSaudeService';

const PlanosSaude = () => {
  const { user } = useAuth();
  const [planos, setPlanos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  
  // Estados do modal
  const [openModal, setOpenModal] = useState(false);
  const [modalMode, setModalMode] = useState('create'); // 'create', 'edit', 'view'
  const [selectedPlano, setSelectedPlano] = useState(null);
  
  // Estados do formulário
  const [formData, setFormData] = useState({
    nome: '',
    codigoAns: '',
    descricao: '',
    ativo: true
  });
  
  const [formErrors, setFormErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    carregarPlanos();
  }, []);

  const carregarPlanos = async () => {
    try {
      setLoading(true);
      const response = await planoSaudeService.listarTodos();
      setPlanos(response.data);
      setError('');
    } catch (err) {
      console.error('Erro ao carregar planos:', err);
      setError('Erro ao carregar planos de saúde');
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode, plano = null) => {
    setModalMode(mode);
    setSelectedPlano(plano);
    
    if (mode === 'create') {
      setFormData({
        nome: '',
        codigoAns: '',
        descricao: '',
        ativo: true
      });
    } else if (plano) {
      setFormData({
        nome: plano.nome || '',
        codigoAns: plano.codigoAns || '',
        descricao: plano.descricao || '',
        ativo: plano.ativo !== undefined ? plano.ativo : true
      });
    }
    
    setFormErrors({});
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedPlano(null);
    setFormData({
      nome: '',
      codigoAns: '',
      descricao: '',
      ativo: true
    });
    setFormErrors({});
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    
    // Limpar erro do campo quando o usuário começar a digitar
    if (formErrors[name]) {
      setFormErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const errors = {};
    
    if (!formData.nome.trim()) {
      errors.nome = 'Nome é obrigatório';
    }
    
    if (!formData.codigoAns.trim()) {
      errors.codigoAns = 'Código ANS é obrigatório';
    }
    
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validateForm()) {
      return;
    }
    
    try {
      setSubmitting(true);
      
      if (modalMode === 'create') {
        await planoSaudeService.criar(formData);
        setSuccess('Plano de saúde criado com sucesso!');
      } else if (modalMode === 'edit') {
        await planoSaudeService.atualizar(selectedPlano.id, formData);
        setSuccess('Plano de saúde atualizado com sucesso!');
      }
      
      handleCloseModal();
      carregarPlanos();
      setError('');
    } catch (err) {
      console.error('Erro ao salvar plano:', err);
      setError(err.response?.data?.message || 'Erro ao salvar plano de saúde');
    } finally {
      setSubmitting(false);
    }
  };

  const handleToggleStatus = async (plano) => {
    try {
      if (plano.ativo) {
        await planoSaudeService.inativar(plano.id);
        setSuccess('Plano de saúde inativado com sucesso!');
      } else {
        await planoSaudeService.ativar(plano.id);
        setSuccess('Plano de saúde ativado com sucesso!');
      }
      
      carregarPlanos();
      setError('');
    } catch (err) {
      console.error('Erro ao alterar status:', err);
      setError('Erro ao alterar status do plano de saúde');
    }
  };

  const handleDelete = async (plano) => {
    if (window.confirm(`Tem certeza que deseja excluir o plano "${plano.nome}"?`)) {
      try {
        await planoSaudeService.excluir(plano.id);
        setSuccess('Plano de saúde excluído com sucesso!');
        carregarPlanos();
        setError('');
      } catch (err) {
        console.error('Erro ao excluir plano:', err);
        setError(err.response?.data?.message || 'Erro ao excluir plano de saúde');
      }
    }
  };

  const filteredPlanos = (planos || []).filter(plano =>
    plano.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
    plano.codigoAns.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getModalTitle = () => {
    switch (modalMode) {
      case 'create': return 'Novo Plano de Saúde';
      case 'edit': return 'Editar Plano de Saúde';
      case 'view': return 'Detalhes do Plano de Saúde';
      default: return '';
    }
  };

  const canEdit = user?.tipo === 'ADMIN';
  const canDelete = user?.tipo === 'ADMIN';

  return (
    <Box sx={{ p: 3 }}>
      <Paper sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
          <Typography variant="h4" component="h1">
            Planos de Saúde
          </Typography>
          {canEdit && (
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={() => handleOpenModal('create')}
            >
              Novo Plano
            </Button>
          )}
        </Box>

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

        <Box sx={{ mb: 3 }}>
          <TextField
            fullWidth
            placeholder="Buscar por nome ou código ANS..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />
        </Box>

        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
            <CircularProgress />
          </Box>
        ) : (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Nome</TableCell>
                  <TableCell>Código ANS</TableCell>
                  <TableCell>Descrição</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell>Pacientes</TableCell>
                  <TableCell>Dentistas</TableCell>
                  <TableCell align="center">Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {filteredPlanos.map((plano) => (
                  <TableRow key={plano.id}>
                    <TableCell>{plano.nome}</TableCell>
                    <TableCell>{plano.codigoAns}</TableCell>
                    <TableCell>
                      {plano.descricao ? (
                        plano.descricao.length > 50 ? 
                          `${plano.descricao.substring(0, 50)}...` : 
                          plano.descricao
                      ) : '-'}
                    </TableCell>
                    <TableCell>
                      <Chip
                        label={plano.ativo ? 'Ativo' : 'Inativo'}
                        color={plano.ativo ? 'success' : 'default'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>{plano.quantidadePacientes || 0}</TableCell>
                    <TableCell>{plano.quantidadeDentistas || 0}</TableCell>
                    <TableCell align="center">
                      <Tooltip title="Visualizar">
                        <IconButton
                          size="small"
                          onClick={() => handleOpenModal('view', plano)}
                        >
                          <VisibilityIcon />
                        </IconButton>
                      </Tooltip>
                      
                      {canEdit && (
                        <>
                          <Tooltip title="Editar">
                            <IconButton
                              size="small"
                              onClick={() => handleOpenModal('edit', plano)}
                            >
                              <EditIcon />
                            </IconButton>
                          </Tooltip>
                          
                          <Tooltip title={plano.ativo ? 'Inativar' : 'Ativar'}>
                            <IconButton
                              size="small"
                              onClick={() => handleToggleStatus(plano)}
                            >
                              {plano.ativo ? <ToggleOffIcon /> : <ToggleOnIcon />}
                            </IconButton>
                          </Tooltip>
                        </>
                      )}
                      
                      {canDelete && (
                        <Tooltip title="Excluir">
                          <IconButton
                            size="small"
                            color="error"
                            onClick={() => handleDelete(plano)}
                          >
                            <DeleteIcon />
                          </IconButton>
                        </Tooltip>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
                
                {filteredPlanos.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={7} align="center">
                      <Typography variant="body2" color="text.secondary">
                        {searchTerm ? 'Nenhum plano encontrado com os critérios de busca.' : 'Nenhum plano de saúde cadastrado.'}
                      </Typography>
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

      {/* Modal de Criar/Editar/Visualizar */}
      <Dialog open={openModal} onClose={handleCloseModal} maxWidth="md" fullWidth>
        <DialogTitle>{getModalTitle()}</DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Nome *"
                name="nome"
                value={formData.nome}
                onChange={handleInputChange}
                error={!!formErrors.nome}
                helperText={formErrors.nome}
                disabled={modalMode === 'view'}
              />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Código ANS *"
                name="codigoAns"
                value={formData.codigoAns}
                onChange={handleInputChange}
                error={!!formErrors.codigoAns}
                helperText={formErrors.codigoAns}
                disabled={modalMode === 'view'}
              />
            </Grid>
            
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Descrição"
                name="descricao"
                value={formData.descricao}
                onChange={handleInputChange}
                multiline
                rows={3}
                disabled={modalMode === 'view'}
              />
            </Grid>
            
            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.ativo}
                    onChange={handleInputChange}
                    name="ativo"
                    disabled={modalMode === 'view'}
                  />
                }
                label="Ativo"
              />
            </Grid>
          </Grid>
        </DialogContent>
        
        <DialogActions>
          <Button onClick={handleCloseModal}>
            {modalMode === 'view' ? 'Fechar' : 'Cancelar'}
          </Button>
          
          {modalMode !== 'view' && (
            <Button
              onClick={handleSubmit}
              variant="contained"
              disabled={submitting}
            >
              {submitting ? <CircularProgress size={20} /> : (modalMode === 'create' ? 'Criar' : 'Salvar')}
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default PlanosSaude;