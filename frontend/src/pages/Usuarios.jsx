import React, { useState, useEffect } from 'react';
import {
  Container,
  Row,
  Col,
  Card,
  Table,
  Button,
  Modal,
  Form,
  Alert,
  Spinner,
  Badge,
  InputGroup,
  Dropdown,
  ButtonGroup
} from 'react-bootstrap';
import { FaPlus, FaEdit, FaEye, FaTrash, FaSearch, FaKey, FaToggleOn, FaToggleOff } from 'react-icons/fa';
import usuarioService from '../services/usuarioService';
import { useAuth } from '../contexts/AuthContext';

const Usuarios = () => {
  const { user } = useAuth();
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [modalMode, setModalMode] = useState('create'); // 'create', 'edit', 'view'
  const [selectedUsuario, setSelectedUsuario] = useState(null);
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    tipo: 'RECEPCIONISTA'
  });
  const [passwordData, setPasswordData] = useState({
    novaSenha: '',
    confirmarSenha: ''
  });
  const [searchTerm, setSearchTerm] = useState('');
  const [searchType, setSearchType] = useState('nome');
  const [alert, setAlert] = useState({ show: false, message: '', variant: 'success' });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    carregarUsuarios();
  }, []);

  const carregarUsuarios = async () => {
    try {
      setLoading(true);
      const data = await usuarioService.listarTodos();
      setUsuarios(data);
    } catch (error) {
      showAlert('Erro ao carregar usuários', 'danger');
    } finally {
      setLoading(false);
    }
  };

  const showAlert = (message, variant = 'success') => {
    setAlert({ show: true, message, variant });
    setTimeout(() => setAlert({ show: false, message: '', variant: 'success' }), 5000);
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      carregarUsuarios();
      return;
    }

    try {
      setLoading(true);
      let data;
      
      switch (searchType) {
        case 'nome':
          data = await usuarioService.buscarPorNome(searchTerm);
          break;
        case 'email':
          try {
            const usuario = await usuarioService.buscarPorEmail(searchTerm);
            data = usuario ? [usuario] : [];
          } catch {
            data = [];
          }
          break;
        default:
          data = await usuarioService.listarTodos();
      }
      
      setUsuarios(data);
    } catch (error) {
      showAlert('Erro ao buscar usuários', 'danger');
    } finally {
      setLoading(false);
    }
  };

  const handleModalShow = (mode, usuario = null) => {
    setModalMode(mode);
    setSelectedUsuario(usuario);
    setErrors({});
    
    if (mode === 'create') {
      setFormData({
        nome: '',
        email: '',
        senha: '',
        tipo: 'RECEPCIONISTA'
      });
    } else if (usuario) {
      setFormData({
        nome: usuario.nome,
        email: usuario.email,
        senha: '',
        tipo: usuario.tipo
      });
    }
    
    setShowModal(true);
  };

  const handleModalClose = () => {
    setShowModal(false);
    setSelectedUsuario(null);
    setFormData({
      nome: '',
      email: '',
      senha: '',
      tipo: 'RECEPCIONISTA'
    });
    setErrors({});
  };

  const handlePasswordModalShow = (usuario) => {
    setSelectedUsuario(usuario);
    setPasswordData({
      novaSenha: '',
      confirmarSenha: ''
    });
    setErrors({});
    setShowPasswordModal(true);
  };

  const handlePasswordModalClose = () => {
    setShowPasswordModal(false);
    setSelectedUsuario(null);
    setPasswordData({
      novaSenha: '',
      confirmarSenha: ''
    });
    setErrors({});
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.nome.trim()) {
      newErrors.nome = 'Nome é obrigatório';
    }
    
    if (!formData.email.trim()) {
      newErrors.email = 'Email é obrigatório';
    } else if (!usuarioService.validarEmail(formData.email)) {
      newErrors.email = 'Email inválido';
    }
    
    if (modalMode === 'create' && !formData.senha.trim()) {
      newErrors.senha = 'Senha é obrigatória';
    } else if (formData.senha && !usuarioService.validarSenha(formData.senha)) {
      newErrors.senha = 'Senha deve ter pelo menos 6 caracteres';
    }
    
    if (!formData.tipo) {
      newErrors.tipo = 'Tipo de usuário é obrigatório';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const validatePasswordForm = () => {
    const newErrors = {};
    
    if (!passwordData.novaSenha.trim()) {
      newErrors.novaSenha = 'Nova senha é obrigatória';
    } else if (!usuarioService.validarSenha(passwordData.novaSenha)) {
      newErrors.novaSenha = 'Senha deve ter pelo menos 6 caracteres';
    }
    
    if (passwordData.novaSenha !== passwordData.confirmarSenha) {
      newErrors.confirmarSenha = 'Senhas não coincidem';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    try {
      if (modalMode === 'create') {
        await usuarioService.criar(formData);
        showAlert('Usuário criado com sucesso!');
      } else if (modalMode === 'edit') {
        await usuarioService.atualizar(selectedUsuario.id, formData);
        showAlert('Usuário atualizado com sucesso!');
      }
      
      handleModalClose();
      carregarUsuarios();
    } catch (error) {
      const message = error.response?.data?.message || 'Erro ao salvar usuário';
      showAlert(message, 'danger');
    }
  };

  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    
    if (!validatePasswordForm()) {
      return;
    }
    
    try {
      await usuarioService.alterarSenha(selectedUsuario.id, passwordData.novaSenha);
      showAlert('Senha alterada com sucesso!');
      handlePasswordModalClose();
    } catch (error) {
      const message = error.response?.data?.message || 'Erro ao alterar senha';
      showAlert(message, 'danger');
    }
  };

  const handleToggleStatus = async (usuario) => {
    try {
      if (usuario.ativo) {
        await usuarioService.desativar(usuario.id);
        showAlert('Usuário desativado com sucesso!');
      } else {
        await usuarioService.ativar(usuario.id);
        showAlert('Usuário ativado com sucesso!');
      }
      carregarUsuarios();
    } catch (error) {
      const message = error.response?.data?.message || 'Erro ao alterar status do usuário';
      showAlert(message, 'danger');
    }
  };

  const handleDelete = async (usuario) => {
    if (window.confirm(`Tem certeza que deseja excluir o usuário "${usuario.nome}"?`)) {
      try {
        await usuarioService.deletar(usuario.id);
        showAlert('Usuário excluído com sucesso!');
        carregarUsuarios();
      } catch (error) {
        const message = error.response?.data?.message || 'Erro ao excluir usuário';
        showAlert(message, 'danger');
      }
    }
  };

  const canEdit = () => {
    return user?.tipo === 'ADMIN';
  };

  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '400px' }}>
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Carregando...</span>
        </Spinner>
      </Container>
    );
  }

  return (
    <Container fluid>
      <Row>
        <Col>
          <Card>
            <Card.Header>
              <Row className="align-items-center">
                <Col>
                  <h4 className="mb-0">Gestão de Usuários</h4>
                </Col>
                <Col xs="auto">
                  {canEdit() && (
                    <Button
                      variant="primary"
                      onClick={() => handleModalShow('create')}
                    >
                      <FaPlus className="me-2" />
                      Novo Usuário
                    </Button>
                  )}
                </Col>
              </Row>
            </Card.Header>
            
            <Card.Body>
              {alert.show && (
                <Alert variant={alert.variant} dismissible onClose={() => setAlert({ show: false })}>
                  {alert.message}
                </Alert>
              )}
              
              {/* Barra de Pesquisa */}
              <Row className="mb-3">
                <Col md={8}>
                  <InputGroup>
                    <Form.Control
                      type="text"
                      placeholder={`Buscar por ${searchType}...`}
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                    />
                    <Dropdown as={ButtonGroup}>
                      <Button variant="outline-secondary" onClick={handleSearch}>
                        <FaSearch />
                      </Button>
                      <Dropdown.Toggle split variant="outline-secondary" />
                      <Dropdown.Menu>
                        <Dropdown.Item onClick={() => setSearchType('nome')} active={searchType === 'nome'}>
                          Nome
                        </Dropdown.Item>
                        <Dropdown.Item onClick={() => setSearchType('email')} active={searchType === 'email'}>
                          Email
                        </Dropdown.Item>
                      </Dropdown.Menu>
                    </Dropdown>
                  </InputGroup>
                </Col>
                <Col md={4}>
                  <Button
                    variant="secondary"
                    onClick={() => {
                      setSearchTerm('');
                      carregarUsuarios();
                    }}
                    className="w-100"
                  >
                    Limpar Filtros
                  </Button>
                </Col>
              </Row>
              
              {/* Tabela de Usuários */}
              <Table striped bordered hover responsive>
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Tipo</th>
                    <th>Status</th>
                    <th>Data Criação</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {usuarios.length === 0 ? (
                    <tr>
                      <td colSpan="6" className="text-center">
                        Nenhum usuário encontrado
                      </td>
                    </tr>
                  ) : (
                    usuarios.map((usuario) => (
                      <tr key={usuario.id}>
                        <td>{usuario.nome}</td>
                        <td>{usuario.email}</td>
                        <td>
                          <Badge bg="info">
                            {usuarioService.formatarTipoUsuario(usuario.tipo)}
                          </Badge>
                        </td>
                        <td>
                          <Badge bg={usuarioService.obterCorStatus(usuario.ativo)}>
                            {usuarioService.formatarStatus(usuario.ativo)}
                          </Badge>
                        </td>
                        <td>
                          {new Date(usuario.dataCriacao).toLocaleDateString('pt-BR')}
                        </td>
                        <td>
                          <ButtonGroup size="sm">
                            <Button
                              variant="outline-info"
                              onClick={() => handleModalShow('view', usuario)}
                              title="Visualizar"
                            >
                              <FaEye />
                            </Button>
                            
                            {canEdit() && (
                              <>
                                <Button
                                  variant="outline-warning"
                                  onClick={() => handleModalShow('edit', usuario)}
                                  title="Editar"
                                >
                                  <FaEdit />
                                </Button>
                                
                                <Button
                                  variant="outline-secondary"
                                  onClick={() => handlePasswordModalShow(usuario)}
                                  title="Alterar Senha"
                                >
                                  <FaKey />
                                </Button>
                                
                                <Button
                                  variant={usuario.ativo ? "outline-danger" : "outline-success"}
                                  onClick={() => handleToggleStatus(usuario)}
                                  title={usuario.ativo ? "Desativar" : "Ativar"}
                                >
                                  {usuario.ativo ? <FaToggleOff /> : <FaToggleOn />}
                                </Button>
                                
                                <Button
                                  variant="outline-danger"
                                  onClick={() => handleDelete(usuario)}
                                  title="Excluir"
                                >
                                  <FaTrash />
                                </Button>
                              </>
                            )}
                          </ButtonGroup>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      
      {/* Modal de Usuário */}
      <Modal show={showModal} onHide={handleModalClose} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>
            {modalMode === 'create' && 'Novo Usuário'}
            {modalMode === 'edit' && 'Editar Usuário'}
            {modalMode === 'view' && 'Detalhes do Usuário'}
          </Modal.Title>
        </Modal.Header>
        
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Nome *</Form.Label>
                  <Form.Control
                    type="text"
                    value={formData.nome}
                    onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
                    isInvalid={!!errors.nome}
                    disabled={modalMode === 'view'}
                    required
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.nome}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
              
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Email *</Form.Label>
                  <Form.Control
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    isInvalid={!!errors.email}
                    disabled={modalMode === 'view'}
                    required
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.email}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>
            
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>
                    Senha {modalMode === 'create' ? '*' : '(deixe em branco para manter)'}
                  </Form.Label>
                  <Form.Control
                    type="password"
                    value={formData.senha}
                    onChange={(e) => setFormData({ ...formData, senha: e.target.value })}
                    isInvalid={!!errors.senha}
                    disabled={modalMode === 'view'}
                    required={modalMode === 'create'}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.senha}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
              
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Tipo de Usuário *</Form.Label>
                  <Form.Select
                    value={formData.tipo}
                    onChange={(e) => setFormData({ ...formData, tipo: e.target.value })}
                    isInvalid={!!errors.tipo}
                    disabled={modalMode === 'view'}
                    required
                  >
                    {usuarioService.obterTiposUsuario().map(tipo => (
                      <option key={tipo.value} value={tipo.value}>
                        {tipo.label}
                      </option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">
                    {errors.tipo}
                  </Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>
            
            {modalMode === 'view' && selectedUsuario && (
              <Row>
                <Col md={6}>
                  <Form.Group className="mb-3">
                    <Form.Label>Status</Form.Label>
                    <div>
                      <Badge bg={usuarioService.obterCorStatus(selectedUsuario.ativo)}>
                        {usuarioService.formatarStatus(selectedUsuario.ativo)}
                      </Badge>
                    </div>
                  </Form.Group>
                </Col>
                
                <Col md={6}>
                  <Form.Group className="mb-3">
                    <Form.Label>Data de Criação</Form.Label>
                    <Form.Control
                      type="text"
                      value={new Date(selectedUsuario.dataCriacao).toLocaleString('pt-BR')}
                      disabled
                    />
                  </Form.Group>
                </Col>
              </Row>
            )}
          </Modal.Body>
          
          <Modal.Footer>
            <Button variant="secondary" onClick={handleModalClose}>
              {modalMode === 'view' ? 'Fechar' : 'Cancelar'}
            </Button>
            {modalMode !== 'view' && (
              <Button variant="primary" type="submit">
                {modalMode === 'create' ? 'Criar' : 'Salvar'}
              </Button>
            )}
          </Modal.Footer>
        </Form>
      </Modal>
      
      {/* Modal de Alteração de Senha */}
      <Modal show={showPasswordModal} onHide={handlePasswordModalClose}>
        <Modal.Header closeButton>
          <Modal.Title>Alterar Senha</Modal.Title>
        </Modal.Header>
        
        <Form onSubmit={handlePasswordSubmit}>
          <Modal.Body>
            {selectedUsuario && (
              <Alert variant="info">
                Alterando senha para: <strong>{selectedUsuario.nome}</strong>
              </Alert>
            )}
            
            <Form.Group className="mb-3">
              <Form.Label>Nova Senha *</Form.Label>
              <Form.Control
                type="password"
                value={passwordData.novaSenha}
                onChange={(e) => setPasswordData({ ...passwordData, novaSenha: e.target.value })}
                isInvalid={!!errors.novaSenha}
                required
              />
              <Form.Control.Feedback type="invalid">
                {errors.novaSenha}
              </Form.Control.Feedback>
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Confirmar Nova Senha *</Form.Label>
              <Form.Control
                type="password"
                value={passwordData.confirmarSenha}
                onChange={(e) => setPasswordData({ ...passwordData, confirmarSenha: e.target.value })}
                isInvalid={!!errors.confirmarSenha}
                required
              />
              <Form.Control.Feedback type="invalid">
                {errors.confirmarSenha}
              </Form.Control.Feedback>
            </Form.Group>
          </Modal.Body>
          
          <Modal.Footer>
            <Button variant="secondary" onClick={handlePasswordModalClose}>
              Cancelar
            </Button>
            <Button variant="primary" type="submit">
              Alterar Senha
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
};

export default Usuarios;