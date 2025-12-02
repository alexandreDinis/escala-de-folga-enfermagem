import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Plus, Search, Building2 } from 'lucide-react';
import { Button, Input, Pagination, ErrorModal } from '../../components/common';
import { SetorTable } from './components/SetorTable';
import { SetorFormModal } from './components/SetorFormModal';
import { SetorDeleteModal } from './components/SetorDeleteModal';
import setorService from '../../services/setor.service';
import { useNotification } from '../../contexts/NotificationContext';

export default function SetorPage() {
  const queryClient = useQueryClient();
  //const { showSuccess, showError } = useNotification();
  const notification = useNotification();

  console.log('=================================');
  console.log('🔍 SetorPage renderizou!');
  console.log('🔍 notification:', notification);
  console.log('🔍 notification.showSuccess:', notification?.showSuccess);
  console.log('🔍 typeof showSuccess:', typeof notification?.showSuccess);
  console.log('=================================');
  
  const { showSuccess, showError } = notification;
  
  // ✅ VERIFICAR SE REALMENTE EXTRAIU
  console.log('🔍 Após destructuring:');
  console.log('🔍 showSuccess:', showSuccess);
  console.log('🔍 showError:', showError);

  

  // Estados
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [search, setSearch] = useState('');
  const [showFormModal, setShowFormModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [showReactivateModal, setShowReactivateModal] = useState(false); // ✅ NOVO
  const [errorConfig, setErrorConfig] = useState({ message: '', type: 'generic' });
  const [selectedSetor, setSelectedSetor] = useState(null);
  const [createError, setCreateError] = useState(null);
  const [reactivateData, setReactivateData] = useState(null); // ✅ NOVO: dados para reativação

  // Query: Listar setores
  const { data, isLoading } = useQuery({
    queryKey: ['setores', page, size, search],
    queryFn: () => setorService.listar({ page, size, search }),
    keepPreviousData: true,
  });

  // Mutation: Criar setor
  const createMutation = useMutation({
    mutationFn: setorService.criar,
    onSuccess: () => {
      queryClient.invalidateQueries(['setores']);
      showSuccess('Setor criado com sucesso');
      setShowFormModal(false);
      setSelectedSetor(null);
      setCreateError(null);
      setShowReactivateModal(false); // ✅ Limpar estado de reativação
      setReactivateData(null);
    },
    onError: (error) => {
      setCreateError(error);
      
      const status = error.response?.status;
      const errorType = error.errorType;
      
      const backendMessage = error.response?.data?.detail || 
                            error.response?.data?.message || 
                            error.friendlyMessage || 
                            error.message;
      
      // ✅ NOVO: Detectar se é erro de reativação
      const isReactivationError = backendMessage.includes('Deseja reativá-lo') || 
                                 backendMessage.includes('deseja reativar') ||
                                 backendMessage.includes('inativo');
      
      if (status === 400 && isReactivationError) {
        // ✅ Mostrar modal de reativação em vez de erro
        setReactivateData({
          nome: error.response?.data?.nome || reactivateData?.nome,
        });
        setShowReactivateModal(true);
        
      } else {
        // Erro normal
        const modalType = errorType === 'validation' ? 'validation' :
                         errorType === 'server' ? 'server' :
                         errorType === 'network' ? 'network' :
                         'generic';
        
        setErrorConfig({
          message: backendMessage,
          type: modalType,
        });
        
        setShowErrorModal(true);
      }
    },
  });

  // ✅ NOVO: Mutation para reativar setor
  const reactivateMutation = useMutation({
    mutationFn: (data) => setorService.criar({ ...data, confirmarReativacao: true }),
    onSuccess: () => {
      queryClient.invalidateQueries(['setores']);
      showSuccess('Setor reativado com sucesso!');
      setShowFormModal(false);
      setShowReactivateModal(false);
      setSelectedSetor(null);
      setReactivateData(null);
    },
    onError: (error) => {
      const backendMessage = error.response?.data?.detail || 
                            error.response?.data?.message || 
                            error.message;
      
      setErrorConfig({
        message: backendMessage,
        type: 'server',
      });
      
      setShowErrorModal(true);
      setShowReactivateModal(false);
    },
  });

  // Mutation: Atualizar setor
  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => setorService.atualizar(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries(['setores']);
      showSuccess('Setor atualizado com sucesso!');
      setShowFormModal(false);
      setSelectedSetor(null);
    },
    onError: (error) => {
      const backendMessage = error.response?.data?.detail || 
                            error.response?.data?.message || 
                            error.message;
      
      if (error.response?.status === 400) {
        setErrorConfig({ message: backendMessage, type: 'validation' });
        setShowErrorModal(true);
      } else {
        showError(backendMessage || 'Erro ao atualizar setor');
      }
    },
  });

  // Mutation: Deletar setor
  const deleteMutation = useMutation({
    mutationFn: setorService.deletar,
    onSuccess: () => {
      queryClient.invalidateQueries(['setores']);
      showSuccess('Setor deletado com sucesso!');
      setShowDeleteModal(false);
      setSelectedSetor(null);
    },
    onError: (error) => {
      const backendMessage = error.response?.data?.detail || 
                            error.response?.data?.message || 
                            error.message;
      
      setErrorConfig({
        message: backendMessage || 'Erro ao deletar setor',
        type: 'server',
      });
      setShowErrorModal(true);
    },
  });

  // Handlers
  const handleCreate = () => {
    setSelectedSetor(null);
    setCreateError(null);
    setReactivateData(null); // ✅ Limpar dados de reativação
    setShowReactivateModal(false);
    setShowFormModal(true);
  };

  const handleEdit = (setor) => {
    setSelectedSetor(setor);
    setShowFormModal(true);
  };

  const handleDelete = (setor) => {
    setSelectedSetor(setor);
    setShowDeleteModal(true);
  };

  const handleSubmit = (data) => {
    if (selectedSetor) {
      updateMutation.mutate({ id: selectedSetor.id, data });
    } else {
      // ✅ Guardar dados para possível reativação
      setReactivateData(data);
      createMutation.mutate(data);
    }
  };

  // ✅ NOVO: Handler para confirmar reativação
  const handleReactivate = () => {
    if (reactivateData) {
      reactivateMutation.mutate(reactivateData);
    }
  };

  const handleConfirmDelete = () => {
    if (selectedSetor) {
      deleteMutation.mutate(selectedSetor.id);
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
              <Building2 className="w-8 h-8  text-white" />
            </div>
            <div>
              <h1 className="text-3xl font-bold text-white mb-1">Setores</h1>
              <p className="text-white/80 text-lg">
                Gerencie os setores do hospital
              </p>
            </div>
          </div>

          {/* Estatísticas */}
          {data && (
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/20">
                <p className="text-sm text-white/80 mb-1">Total de Setores</p>
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
              Novo Setor
            </Button>
          </div>
        </div>
      </div>

      {/* Tabela */}
      <div className="card">
        <SetorTable
          setores={data?.content || []}
          loading={isLoading}
          onEdit={handleEdit}
          onDelete={handleDelete}
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
      <SetorFormModal
        isOpen={showFormModal}
        onClose={() => {
          setShowFormModal(false);
          setSelectedSetor(null);
          setCreateError(null);
          setShowReactivateModal(false); // ✅ Limpar estado de reativação
          setReactivateData(null);
        }}
        onSubmit={handleSubmit}
        onReactivate={handleReactivate} // ✅ NOVO
        setor={selectedSetor}
        loading={createMutation.isLoading || updateMutation.isLoading || reactivateMutation.isLoading}
        error={createError}
        showReactivate={showReactivateModal} // ✅ NOVO
        reactivateData={reactivateData} // ✅ NOVO
      />

      <SetorDeleteModal
        isOpen={showDeleteModal}
        onClose={() => {
          setShowDeleteModal(false);
          setSelectedSetor(null);
        }}
        onConfirm={handleConfirmDelete}
        setor={selectedSetor}
        loading={deleteMutation.isLoading}
      />

      {/* Modal de Erro */}
      <ErrorModal
        isOpen={showErrorModal}
        onClose={() => {
          setShowErrorModal(false);
          setCreateError(null);
        }}
        message={errorConfig.message}
        type={errorConfig.type}
      />
    </div>
  );
}