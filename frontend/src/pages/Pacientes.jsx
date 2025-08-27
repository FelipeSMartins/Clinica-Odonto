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
  InputAdornment
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Search as SearchIcon,
  Visibility as VisibilityIcon,
  PersonOff as PersonOffIcon,
  PersonAdd as PersonAddIcon
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import pacienteService from '../services/pacienteService';
import planoSaudeService from '../services/planoSaudeService';

const Pacientes = () => {
  const { user } = useAuth();
  const [pacientes, setPacientes] = useState([]);
  const [planosSaude, setPlanosSaude] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  
  // Estados do modal
  const [openModal, setOpenModal] = useState(false);
  const [modalMode, setModalMode] = useState('create'); // 'create', 'edit', 'view'
  const [selectedPaciente, setSelectedPaciente] = useState(null);
  
  // Estados do formulário
  const [formData, setFormData] = useState({
    nome: '',
    cpf: '',
    dataNascimento: '',
    sexo: '',
    email: '',
    telefone: '',
    celular: '',
    endereco: {
      cep: '',
      logradouro: '',
      numero: '',
      complemento: '',
      bairro: '',
      cidade: '',
      uf: ''
    },
    observacoes: '',
    planoSaudeId: ''
  });

  useEffect(() => {
    carregarPacientes();
    carregarPlanosSaude();
  }, []);

  const carregarPacientes = async () => {
    try {
      setLoading(true);
      const data = await pacienteService.listarTodos();
      setPacientes(data);
      setError('');
    } catch (err) {
      setError('Erro ao carregar pacientes');
      console.error('Erro:', err);
    } finally {
      setLoading(false);
    }
  };

  const carregarPlanosSaude = async () => {
    try {
      const data = await planoSaudeService.listarTodos();
      setPlanosSaude(data.filter(plano => plano.ativo));
    } catch (err) {
      console.error('Erro ao carregar planos de saúde:', err);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      carregarPacientes();
      return;
    }

    try {
      setLoading(true);
      const data = await pacienteService.buscarPorNome(searchTerm);
      setPacientes(data);
      setError('');
    } catch (err) {
      setError('Erro ao buscar pacientes');
      console.error('Erro:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode, paciente = null) => {
    setModalMode(mode);
    setSelectedPaciente(paciente);
    
    if (mode === 'create') {
      setFormData({
        nome: '',
        cpf: '',
        dataNascimento: '',
        sexo: '',
        email: '',
        telefone: '',
        celular: '',
        endereco: {
          cep: '',
          logradouro: '',
          numero: '',
          complemento: '',
          bairro: '',
          cidade: '',
          uf: ''
        },
        observacoes: '',
        planoSaudeId: ''
      });
    } else if (paciente) {
      setFormData({
        nome: paciente.nome || '',
        cpf: paciente.cpf || '',
        dataNascimento: paciente.dataNascimento || '',
        sexo: paciente.sexo || '',
        email: paciente.email || '',
        telefone: paciente.telefone || '',
        celular: paciente.celular || '',
        endereco: paciente.endereco || {
          cep: '',
          logradouro: '',
          numero: '',
          complemento: '',
          bairro: '',
          cidade: '',
          uf: ''
        },
        observacoes: paciente.observacoes || '',
        planoSaudeId: paciente.planoSaudeId || ''
      });
    }
    
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedPaciente(null);
    setError('');
    setSuccess('');
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    
    if (name.startsWith('endereco.')) {
      const enderecoField = name.split('.')[1];
      setFormData(prev => ({
        ...prev,
        endereco: {
          ...prev.endereco,
          [enderecoField]: value
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      if (modalMode === 'create') {
        await pacienteService.criar(formData);
        setSuccess('Paciente criado com sucesso!');
      } else if (modalMode === 'edit') {
        await pacienteService.atualizar(selectedPaciente.id, formData);
        setSuccess('Paciente atualizado com sucesso!');
      }
      
      await carregarPacientes();
      handleCloseModal();
    } catch (err) {
      setError('Erro ao salvar paciente');
      console.error('Erro:', err);
    }
  };

  const handleToggleStatus = async (paciente) => {
    try {
      if (paciente.ativo) {
        await pacienteService.inativar(paciente.id);
        setSuccess('Paciente inativado com sucesso!');
      } else {
        await pacienteService.ativar(paciente.id);
        setSuccess('Paciente ativado com sucesso!');
      }
      
      await carregarPacientes();
    } catch (err) {
      setError('Erro ao alterar status do paciente');
      console.error('Erro:', err);
    }
  };

  const formatCpf = (cpf) => {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  };

  const formatDate = (date) => {
    if (!date) return '';
    return new Date(date).toLocaleDateString('pt-BR');
  };

  const canEdit = user?.tipo === 'ADMIN' || user?.tipo === 'RECEPCIONISTA';

  if (loading && pacientes.length === 0) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Gestão de Pacientes
      </Typography>

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

      <Paper sx={{ p: 2, mb: 2 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} md={8}>
            <TextField
              fullWidth
              label="Buscar pacientes"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton onClick={handleSearch}>
                      <SearchIcon />
                    </IconButton>
                  </InputAdornment>
                )
              }}
            />
          </Grid>
          <Grid item xs={12} md={4}>
            {canEdit && (
              <Button
                variant="contained"
                startIcon={<AddIcon />}
                onClick={() => handleOpenModal('create')}
                fullWidth
              >
                Novo Paciente
              </Button>
            )}
          </Grid>
        </Grid>
      </Paper>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Nome</TableCell>
              <TableCell>CPF</TableCell>
              <TableCell>Data Nascimento</TableCell>
              <TableCell>Sexo</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Plano de Saúde</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="center">Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {pacientes.map((paciente) => (
              <TableRow key={paciente.id}>
                <TableCell>{paciente.nome}</TableCell>
                <TableCell>{formatCpf(paciente.cpf)}</TableCell>
                <TableCell>{formatDate(paciente.dataNascimento)}</TableCell>
                <TableCell>{paciente.sexo}</TableCell>
                <TableCell>{paciente.email || '-'}</TableCell>
                <TableCell>{paciente.planoSaudeNome || '-'}</TableCell>
                <TableCell>
                  <Chip
                    label={paciente.ativo ? 'Ativo' : 'Inativo'}
                    color={paciente.ativo ? 'success' : 'default'}
                    size="small"
                  />
                </TableCell>
                <TableCell align="center">
                  <Tooltip title="Visualizar">
                    <IconButton
                      size="small"
                      onClick={() => handleOpenModal('view', paciente)}
                    >
                      <VisibilityIcon />
                    </IconButton>
                  </Tooltip>
                  
                  {canEdit && (
                    <>
                      <Tooltip title="Editar">
                        <IconButton
                          size="small"
                          onClick={() => handleOpenModal('edit', paciente)}
                        >
                          <EditIcon />
                        </IconButton>
                      </Tooltip>
                      
                      <Tooltip title={paciente.ativo ? 'Inativar' : 'Ativar'}>
                        <IconButton
                          size="small"
                          onClick={() => handleToggleStatus(paciente)}
                          color={paciente.ativo ? 'error' : 'success'}
                        >
                          {paciente.ativo ? <PersonOffIcon /> : <PersonAddIcon />}
                        </IconButton>
                      </Tooltip>
                    </>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Modal para criar/editar/visualizar paciente */}
      <Dialog open={openModal} onClose={handleCloseModal} maxWidth="md" fullWidth>
        <DialogTitle>
          {modalMode === 'create' && 'Novo Paciente'}
          {modalMode === 'edit' && 'Editar Paciente'}
          {modalMode === 'view' && 'Detalhes do Paciente'}
        </DialogTitle>
        
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Grid container spacing={2}>
              {/* Dados Pessoais */}
              <Grid item xs={12}>
                <Typography variant="h6" gutterBottom>
                  Dados Pessoais
                </Typography>
              </Grid>
              
              <Grid item xs={12} md={8}>
                <TextField
                  fullWidth
                  label="Nome Completo"
                  name="nome"
                  value={formData.nome}
                  onChange={handleInputChange}
                  required
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="CPF"
                  name="cpf"
                  value={formData.cpf}
                  onChange={handleInputChange}
                  required
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={6}>
                <TextField
                  fullWidth
                  label="Data de Nascimento"
                  name="dataNascimento"
                  type="date"
                  value={formData.dataNascimento}
                  onChange={handleInputChange}
                  required
                  disabled={modalMode === 'view'}
                  InputLabelProps={{ shrink: true }}
                />
              </Grid>
              
              <Grid item xs={12} md={6}>
                <FormControl fullWidth required disabled={modalMode === 'view'}>
                  <InputLabel>Sexo</InputLabel>
                  <Select
                    name="sexo"
                    value={formData.sexo}
                    onChange={handleInputChange}
                    label="Sexo"
                  >
                    <MenuItem value="MASCULINO">Masculino</MenuItem>
                    <MenuItem value="FEMININO">Feminino</MenuItem>
                    <MenuItem value="OUTRO">Outro</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              
              {/* Contato */}
              <Grid item xs={12}>
                <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                  Contato
                </Typography>
              </Grid>
              
              <Grid item xs={12} md={6}>
                <TextField
                  fullWidth
                  label="Email"
                  name="email"
                  type="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  label="Telefone"
                  name="telefone"
                  value={formData.telefone}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  label="Celular"
                  name="celular"
                  value={formData.celular}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              {/* Plano de Saúde */}
              <Grid item xs={12}>
                <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                  Plano de Saúde
                </Typography>
              </Grid>
              
              <Grid item xs={12} md={6}>
                <FormControl fullWidth disabled={modalMode === 'view'}>
                  <InputLabel>Plano de Saúde</InputLabel>
                  <Select
                    name="planoSaudeId"
                    value={formData.planoSaudeId}
                    onChange={handleInputChange}
                    label="Plano de Saúde"
                  >
                    <MenuItem value="">
                      <em>Nenhum</em>
                    </MenuItem>
                    {planosSaude.map((plano) => (
                      <MenuItem key={plano.id} value={plano.id}>
                        {plano.nome}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
              
              {/* Endereço */}
              <Grid item xs={12}>
                <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                  Endereço
                </Typography>
              </Grid>
              
              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  label="CEP"
                  name="endereco.cep"
                  value={formData.endereco.cep}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={6}>
                <TextField
                  fullWidth
                  label="Logradouro"
                  name="endereco.logradouro"
                  value={formData.endereco.logradouro}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  label="Número"
                  name="endereco.numero"
                  value={formData.endereco.numero}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="Complemento"
                  name="endereco.complemento"
                  value={formData.endereco.complemento}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="Bairro"
                  name="endereco.bairro"
                  value={formData.endereco.bairro}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  label="Cidade"
                  name="endereco.cidade"
                  value={formData.endereco.cidade}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              <Grid item xs={12} md={1}>
                <TextField
                  fullWidth
                  label="UF"
                  name="endereco.uf"
                  value={formData.endereco.uf}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  inputProps={{ maxLength: 2 }}
                />
              </Grid>
              
              {/* Observações */}
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Observações"
                  name="observacoes"
                  value={formData.observacoes}
                  onChange={handleInputChange}
                  multiline
                  rows={3}
                  disabled={modalMode === 'view'}
                />
              </Grid>
            </Grid>
          </DialogContent>
          
          <DialogActions>
            <Button onClick={handleCloseModal}>
              {modalMode === 'view' ? 'Fechar' : 'Cancelar'}
            </Button>
            {modalMode !== 'view' && (
              <Button type="submit" variant="contained">
                {modalMode === 'create' ? 'Criar' : 'Salvar'}
              </Button>
            )}
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default Pacientes;