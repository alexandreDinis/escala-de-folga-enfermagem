import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Plus, Search, Users, Briefcase, Clock, Building2 } from 'lucide-react';
import { Button, Input, Pagination, ErrorModal, Select } from '../../components/common';
import { ColaboradorTable } from './components/ColaboradorTable';
import { ColaboradorFormModal } from './components/ColaboradorFormModal';
import { ColaboradorDeleteModal } from './components/ColaboradorDeleteModal';
import colaboradorService from '../../services/colaborador.service';
import setorService from '../../services/setor.service';
import { useNotification } from '../../contexts/NotificationContext';

export default function ColaboradorPage() {
  const queryClient = useQueryClient();
  const { showSuccess, showError } = useNotification();

  // Estados
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [search, setSearch] = useState('');
  const [setorFilter, setSetorFilter] = useState('');
  const [turnoFilter, setTurnoFilter] = useState('');
  const [cargoFilter, setCargoFilter] = useState('');
  const [showFormModal, setShowFormModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorConfig, setErrorConfig] = useState({ message: '', type: 'generic' });
  const [selectedColaborador, setSelectedColaborador] = useState(null);

  // Query: Listar colaboradores
  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['colaboradores', page, size, search, setorFilter, turnoFilter, cargoFilter],
    queryFn: () => colaboradorService.listar({ 
      page, 
      size, 
      search, 
      setorId: setorFilter, 
      turno: turnoFilter, 
      cargo: cargoFilter 
    }),
    retry: 3,
    retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 10000),
  });

  // Query: Listar setores para filtro
  const { data: setoresData } = useQuery({
    queryKey: ['setores-filter'],
    queryFn: () => setorService.listar({ page: 0, size: 100, search: '' }),
  });

  // Mutation: Criar
  const createMutation = useMutation({
    mutationFn: colaboradorService.criar,
    onSuccess: () => {
      queryClient.invalidateQueries(['colaboradores']);
      showSuccess('Colaborador criado com sucesso!');
      setShowFormModal(false);
      setSelectedColaborador(null);
    },
    onError: (error) => {
      const backendMessage = error.response?.data?.detail || 
                            error.response?.data?.message || 
                            error.message;
      setErrorConfig({ message: backendMessage, type: 'validation' });
      setShowErrorModal(true);
    },
  });

  // Mutation: Atualizar
  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => colaboradorService.atualizar(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries(['colaboradores']);
      showSuccess('Colaborador atualizado com sucesso!');
      setShowFormModal(false);
      setSelectedColaborador(null);
    },
    onError: (error) => {
      const backendMessage = error.response?.data?.detail || 
                            error.response?.data?.message || 
                            error.message;
      setErrorConfig({ message: backendMessage, type: 'validation' });
      setShowErrorModal(true);
    },
  });

  // Mutation: Deletar
  const deleteMutation = useMutation({
    mutationFn: colaboradorService.deletar,
    onSuccess: () => {
      queryClient.invalidateQueries(['colaboradores']);
      showSuccess('Colaborador deletado com sucesso!');
      setShowDeleteModal(false);
      setSelectedColaborador(null);
    },
    onError: (error) => {
      const backendMessage = error.response?.data?.detail || 
                            error.response?.data?.message || 
                            error.message;
      setErrorConfig({ message: backendMessage, type: 'server' });
      setShowErrorModal(true);
    },
  });

  // Handlers
  const handleCreate = () => {
    setSelectedColaborador(null);
    setShowFormModal(true);
  };

  const handleEdit = (colaborador) => {
    setSelectedColaborador(colaborador);
    setShowFormModal(true);
  };

  const handleDelete = (colaborador) => {
    setSelectedColaborador(colaborador);
    setShowDeleteModal(true);
  };

  const handleSubmit = (data) => {
    if (selectedColaborador) {
      updateMutation.mutate({ id: selectedColaborador.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const handleConfirmDelete = () => {
    if (selectedColaborador) {
      deleteMutation.mutate(selectedColaborador.id);
    }
  };

  const handleSearch = (e) => {
    setSearch(e.target.value);
    setPage(0);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="card overflow-hidden">
        <div className="bg-gradient-to-r from-primary to-primary-dark text-white p-8">
          <div className="flex items-center gap-4 mb-4">
            <div className="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
              <Users className="w-8 h-8 text-white" />
            </div>
            <div>
              <h1 className="text-3xl font-bold text-white mb-1">Colaboradores</h1>
              <p className="text-white/80 text-lg">
                Gerencie a equipe de enfermagem
              </p>
            </div>
          </div>

          {/* Estatísticas */}
          {data && (
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/20">
                <p className="text-sm text-white/80 mb-1">Total de Colaboradores</p>
                <p className="text-2xl font-bold">{data.totalElements || 0}</p>
              </div>
              <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/20">
                <p className="text-sm text-white/80 mb-1">Página Atual</p>
                <p className="text-2xl font-bold">{page + 1} de {data.totalPages || 1}</p>
              </div>
              <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/20">
                <p className="text-sm text-white/80 mb-1">Por Página</p>
                <p className="text-2xl font-bold">{size}</p>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Filtros e Ações */}
      <div className="card">
        <div className="card-body">
          <div className="flex flex-col gap-4">
            {/* Linha 1: Busca e Botão */}
            <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between">
              <div className="flex-1 w-full sm:max-w-md">
                <Input
                  placeholder="Buscar por nome..."
                  leftIcon={Search}
                  value={search}
                  onChange={handleSearch}
                />
              </div>
              <Button
                variant="primary"
                leftIcon={Plus}
                onClick={handleCreate}
                className="w-full sm:w-auto"
              >
                Novo Colaborador
              </Button>
            </div>

            {/* Linha 2: Filtros */}
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <Select
                leftIcon={Building2}
                value={setorFilter}
                onChange={(e) => {
                  setSetorFilter(e.target.value);
                  setPage(0);
                }}
              >
                <option value="">Todos os Setores</option>
                {setoresData?.content?.map(setor => (
                  <option key={setor.id} value={setor.id}>
                    {setor.nome}
                  </option>
                ))}
              </Select>

              <Select
                leftIcon={Clock}
                value={turnoFilter}
                onChange={(e) => {
                  setTurnoFilter(e.target.value);
                  setPage(0);
                }}
              >
                <option value="">Todos os Turnos</option>
                <option value="MANHA">🌅 Manhã</option>
                <option value="TARDE">☀️ Tarde</option>
                <option value="NOITE">🌙 Noite</option>
              </Select>

              <Select
                leftIcon={Briefcase}
                value={cargoFilter}
                onChange={(e) => {
                  setCargoFilter(e.target.value);
                  setPage(0);
                }}
              >
                <option value="">Todos os Cargos</option>
                <option value="ENFERMEIRO">🩺 Enfermeiro</option>
                <option value="TECNICO">🔧 Técnico</option>
              </Select>
            </div>
          </div>
        </div>
      </div>

      {/* Tabela */}
      <div className="card">
        <ColaboradorTable
          colaboradores={data?.content || []}
          loading={isLoading}
          error={error}
          onEdit={handleEdit}
          onDelete={handleDelete}
          onRetry={refetch}
          onCreate={handleCreate}
        />

        {data && data.content && data.content.length > 0 && (
          <Pagination
            currentPage={page}
            totalPages={data.totalPages || 1}
            pageSize={size}
            totalItems={data.totalElements || 0}
            onPageChange={setPage}
            onPageSizeChange={(newSize) => {
              setSize(newSize);
              setPage(0);
            }}
          />
        )}
      </div>

      {/* Modais */}
      <ColaboradorFormModal
        isOpen={showFormModal}
        onClose={() => {
          setShowFormModal(false);
          setSelectedColaborador(null);
        }}
        onSubmit={handleSubmit}
        colaborador={selectedColaborador}
        loading={createMutation.isLoading || updateMutation.isLoading}
      />

      <ColaboradorDeleteModal
        isOpen={showDeleteModal}
        onClose={() => {
          setShowDeleteModal(false);
          setSelectedColaborador(null);
        }}
        onConfirm={handleConfirmDelete}
        colaborador={selectedColaborador}
        loading={deleteMutation.isLoading}
      />

      <ErrorModal
        isOpen={showErrorModal}
        onClose={() => setShowErrorModal(false)}
        message={errorConfig.message}
        type={errorConfig.type}
      />
    </div>
  );
}
