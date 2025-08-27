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
  Chip,
  Alert,
  CircularProgress,
  Tooltip,
  InputAdornment,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Checkbox,
  ListItemText
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Search as SearchIcon,
  Visibility as VisibilityIcon,
  PersonOff as PersonOffIcon,
  PersonAdd as PersonAddIcon,
  MedicalServices as MedicalServicesIcon
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import dentistaService from '../services/dentistaService';
import planoSaudeService from '../services/planoSaudeService';

const Dentistas = () => {
  const { user } = useAuth();
  const [dentistas, setDentistas] = useState([]);
  const [planosSaude, setPlanosSaude] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [searchType, setSearchType] = useState('nome'); // 'nome', 'especialidade', 'cro'
  
  // Estados do modal
  const [openModal, setOpenModal] = useState(false);
  const [modalMode, setModalMode] = useState('create'); // 'create', 'edit', 'view'
  const [selectedDentista, setSelectedDentista] = useState(null);
  
  // Estados do formulário
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    cro: '',
    especialidade: '',
    telefone: '',
    planosAceitosIds: []
  });

  useEffect(() => {
    carregarDentistas();
    carregarPlanosSaude();
  }, []);

  const carregarDentistas = async () => {
    try {
      setLoading(true);
      const data = await dentistaService.listarTodos();
      setDentistas(data);
      setError('');
    } catch (err) {
      setError('Erro ao carregar dentistas');
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
      carregarDentistas();
      return;
    }

    try {
      setLoading(true);
      let data;
      
      switch (searchType) {
        case 'nome':
          data = await dentistaService.buscarPorNome(searchTerm);
          break;
        case 'especialidade':
          data = await dentistaService.buscarPorEspecialidade(searchTerm);
          break;
        case 'cro':
          data = [await dentistaService.buscarPorCro(searchTerm)];
          break;
        default:
          data = await dentistaService.buscarPorNome(searchTerm);
      }
      
      setDentistas(data.filter(Boolean)); // Remove null/undefined
      setError('');
    } catch (err) {
      setError('Erro ao buscar dentistas');
      console.error('Erro:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode, dentista = null) => {
    setModalMode(mode);
    setSelectedDentista(dentista);
    
    if (mode === 'create') {
      setFormData({
        nome: '',
        email: '',
        senha: '',
        cro: '',
        especialidade: '',
        telefone: '',
        planosAceitosIds: []
      });
    } else if (dentista) {
      setFormData({
        nome: dentista.nome || '',
        email: dentista.email || '',
        senha: '', // Não preencher senha na edição
        cro: dentista.cro || '',
        especialidade: dentista.especialidade || '',
        telefone: dentista.telefone || '',
        planosAceitosIds: dentista.planosAceitos ? dentista.planosAceitos.map(plano => plano.id) : []
      });
    }
    
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedDentista(null);
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

  const handlePlanosChange = (e) => {
    const { value } = e.target;
    setFormData(prev => ({
      ...prev,
      planosAceitosIds: typeof value === 'string' ? value.split(',') : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      if (modalMode === 'create') {
        await dentistaService.criar(formData);
        setSuccess('Dentista criado com sucesso!');
      } else if (modalMode === 'edit') {
        await dentistaService.atualizar(selectedDentista.id, formData);
        setSuccess('Dentista atualizado com sucesso!');
      }
      
      await carregarDentistas();
      handleCloseModal();
    } catch (err) {
      setError('Erro ao salvar dentista');
      console.error('Erro:', err);
    }
  };

  const handleToggleStatus = async (dentista) => {
    try {
      if (dentista.ativo) {
        await dentistaService.inativar(dentista.id);
        setSuccess('Dentista inativado com sucesso!');
      } else {
        await dentistaService.ativar(dentista.id);
        setSuccess('Dentista ativado com sucesso!');
      }
      
      await carregarDentistas();
    } catch (err) {
      setError('Erro ao alterar status do dentista');
      console.error('Erro:', err);
    }
  };

  const canEdit = user?.tipo === 'ADMIN' || user?.tipo === 'RECEPCIONISTA';

  if (loading && dentistas.length === 0) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        <MedicalServicesIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
        Gestão de Dentistas
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
          <Grid item xs={12} md={3}>
            <FormControl fullWidth size="small">
              <InputLabel>Buscar por</InputLabel>
              <Select
                value={searchType}
                onChange={(e) => setSearchType(e.target.value)}
                label="Buscar por"
              >
                <MenuItem value="nome">Nome</MenuItem>
                <MenuItem value="especialidade">Especialidade</MenuItem>
                <MenuItem value="cro">CRO</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={5}>
            <TextField
              fullWidth
              size="small"
              label={`Buscar por ${searchType}`}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton onClick={handleSearch} size="small">
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
                Novo Dentista
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
              <TableCell>CRO</TableCell>
              <TableCell>Especialidade</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Telefone</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="center">Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {dentistas.map((dentista) => (
              <TableRow key={dentista.id}>
                <TableCell>{dentista.nome}</TableCell>
                <TableCell>{dentista.cro}</TableCell>
                <TableCell>{dentista.especialidade || '-'}</TableCell>
                <TableCell>{dentista.email}</TableCell>
                <TableCell>{dentista.telefone || '-'}</TableCell>
                <TableCell>
                  <Chip
                    label={dentista.ativo ? 'Ativo' : 'Inativo'}
                    color={dentista.ativo ? 'success' : 'default'}
                    size="small"
                  />
                </TableCell>
                <TableCell align="center">
                  <Tooltip title="Visualizar">
                    <IconButton
                      size="small"
                      onClick={() => handleOpenModal('view', dentista)}
                    >
                      <VisibilityIcon />
                    </IconButton>
                  </Tooltip>
                  
                  {canEdit && (
                    <>
                      <Tooltip title="Editar">
                        <IconButton
                          size="small"
                          onClick={() => handleOpenModal('edit', dentista)}
                        >
                          <EditIcon />
                        </IconButton>
                      </Tooltip>
                      
                      <Tooltip title={dentista.ativo ? 'Inativar' : 'Ativar'}>
                        <IconButton
                          size="small"
                          onClick={() => handleToggleStatus(dentista)}
                          color={dentista.ativo ? 'error' : 'success'}
                        >
                          {dentista.ativo ? <PersonOffIcon /> : <PersonAddIcon />}
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

      {/* Modal para criar/editar/visualizar dentista */}
      <Dialog open={openModal} onClose={handleCloseModal} maxWidth="md" fullWidth>
        <DialogTitle>
          {modalMode === 'create' && 'Novo Dentista'}
          {modalMode === 'edit' && 'Editar Dentista'}
          {modalMode === 'view' && 'Detalhes do Dentista'}
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
              
              <Grid item xs={12} md={6}>
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
              
              <Grid item xs={12} md={6}>
                <TextField
                  fullWidth
                  label="Email"
                  name="email"
                  type="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              {modalMode !== 'view' && (
                <Grid item xs={12} md={6}>
                  <TextField
                    fullWidth
                    label={modalMode === 'create' ? 'Senha' : 'Nova Senha (deixe vazio para manter)'}
                    name="senha"
                    type="password"
                    value={formData.senha}
                    onChange={handleInputChange}
                    required={modalMode === 'create'}
                  />
                </Grid>
              )}
              
              {/* Dados Profissionais */}
              <Grid item xs={12}>
                <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                  Dados Profissionais
                </Typography>
              </Grid>
              
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="CRO"
                  name="cro"
                  value={formData.cro}
                  onChange={handleInputChange}
                  required
                  disabled={modalMode === 'view'}
                  helperText="Número do Conselho Regional de Odontologia"
                />
              </Grid>
              
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="Especialidade"
                  name="especialidade"
                  value={formData.especialidade}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                  helperText="Ex: Ortodontia, Endodontia, etc."
                />
              </Grid>
              
              <Grid item xs={12} md={4}>
                <TextField
                  fullWidth
                  label="Telefone"
                  name="telefone"
                  value={formData.telefone}
                  onChange={handleInputChange}
                  disabled={modalMode === 'view'}
                />
              </Grid>
              
              {/* Planos de Saúde Aceitos */}
              <Grid item xs={12}>
                <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                  Planos de Saúde Aceitos
                </Typography>
              </Grid>
              
              <Grid item xs={12}>
                <FormControl fullWidth disabled={modalMode === 'view'}>
                  <InputLabel>Planos de Saúde</InputLabel>
                  <Select
                    multiple
                    name="planosAceitosIds"
                    value={formData.planosAceitosIds}
                    onChange={handlePlanosChange}
                    label="Planos de Saúde"
                    renderValue={(selected) => {
                      if (selected.length === 0) return 'Nenhum plano selecionado';
                      const selectedPlanos = planosSaude.filter(plano => selected.includes(plano.id));
                      return selectedPlanos.map(plano => plano.nome).join(', ');
                    }}
                  >
                    {planosSaude.map((plano) => (
                      <MenuItem key={plano.id} value={plano.id}>
                        <Checkbox checked={formData.planosAceitosIds.indexOf(plano.id) > -1} />
                        <ListItemText primary={plano.nome} />
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
              
              {modalMode === 'view' && selectedDentista && (
                <>
                  <Grid item xs={12}>
                    <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                      Informações do Sistema
                    </Typography>
                  </Grid>
                  
                  <Grid item xs={12} md={6}>
                    <TextField
                      fullWidth
                      label="Data de Cadastro"
                      value={selectedDentista.dataCadastro ? 
                        new Date(selectedDentista.dataCadastro).toLocaleString('pt-BR') : '-'}
                      disabled
                    />
                  </Grid>
                  
                  <Grid item xs={12} md={6}>
                    <TextField
                      fullWidth
                      label="Última Atualização"
                      value={selectedDentista.dataAtualizacao ? 
                        new Date(selectedDentista.dataAtualizacao).toLocaleString('pt-BR') : '-'}
                      disabled
                    />
                  </Grid>
                </>
              )}
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

export default Dentistas;