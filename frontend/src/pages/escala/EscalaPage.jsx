import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { Plus, Calendar } from 'lucide-react';
import { Button, Select, Input } from '../../components/common';
import { EscalaTable } from './components/EscalaTable';
import { EscalaFormModal } from './components/EscalaFormModal';
import { EscalaDeleteModal } from './components/EscalaDeleteModal';
import { HistoricoFolgaModal } from './components/HistoricoFolgaModal';
import { CadastroHistoricoModal } from './components/CadastroHistoricoModal';
import escalaService from '../../services/escala.service';
import setorService from '../../services/setor.service';
import toast from 'react-hot-toast';

export default function EscalaPage() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  // Estados de paginação e filtros
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [setorFilter, setSetorFilter] = useState('');
  const [turnoFilter, setTurnoFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [mesFilter, setMesFilter] = useState('');
  const [anoFilter, setAnoFilter] = useState('');

  // Estados de modais
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [selectedEscala, setSelectedEscala] = useState(null);
  
  // Estados de histórico
  const [isHistoricoModalOpen, setIsHistoricoModalOpen] = useState(false);
  const [historicoData, setHistoricoData] = useState(null);
  
  // Estados de cadastro de histórico
  const [isCadastroHistoricoOpen, setIsCadastroHistoricoOpen] = useState(false);
  const [colaboradorSelecionado, setColaboradorSelecionado] = useState(null);

  // Query de escalas
  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['escalas', page, size, setorFilter, turnoFilter, statusFilter, mesFilter, anoFilter],
    queryFn: () => escalaService.listar({
      page,
      size,
      setorId: setorFilter,
      turno: turnoFilter,
      status: statusFilter,
      mes: mesFilter,
      ano: anoFilter,
    }),
    staleTime: 30000,
  });

  // Query de setores
  const { data: setoresData } = useQuery({
    queryKey: ['setores-all'],
    queryFn: () => setorService.listar({ page: 0, size: 100 }),
    staleTime: 300000,
  });

  // Mutation de delete
  const deleteMutation = useMutation({
    mutationFn: escalaService.deletar,
    onSuccess: () => {
      queryClient.invalidateQueries(['escalas']);
      toast.success('Escala deletada com sucesso!');
      setIsDeleteOpen(false);
      setSelectedEscala(null);
    },
    onError: (error) => {
      const message = error.response?.data?.message || 'Erro ao deletar escala';
      toast.error(message);
    },
  });

  // Handlers
  const handleCreate = () => {
    setSelectedEscala(null);
    setIsFormOpen(true);
  };

  const handleEdit = (escala) => {
    setSelectedEscala(escala);
    setIsFormOpen(true);
  };

  const handleDelete = (escala) => {
    setSelectedEscala(escala);
    setIsDeleteOpen(true);
  };

  const handleConfirmDelete = () => {
    if (selectedEscala) {
      deleteMutation.mutate(selectedEscala.id);
    }
  };

  const handleFormSuccess = async (escalaId) => {
    queryClient.invalidateQueries(['escalas']);
    setIsFormOpen(false);
    setSelectedEscala(null);

    try {
      const historico = await escalaService.verificarHistorico(escalaId);
      
      if (historico.faltaHistorico) {
        setHistoricoData(historico);
        setIsHistoricoModalOpen(true);
        // Guardar ID da escala para recarregar depois
        setSelectedEscala({ id: escalaId });
      } else {
        toast.success('Escala criada! Redirecionando para o calendário...');
        setTimeout(() => {
          navigate(`/escalas/${escalaId}/calendario`);
        }, 1500);
      }
    } catch (error) {
      console.error('Erro ao verificar histórico:', error);
      toast.error('Escala criada, mas houve erro ao verificar histórico.');
    }
  };

  const handleSelecionarColaborador = (colaborador) => {
    setColaboradorSelecionado(colaborador);
    setIsCadastroHistoricoOpen(true);
  };

  const handleHistoricoSuccess = async () => {
    setIsCadastroHistoricoOpen(false);
    setColaboradorSelecionado(null);
    
    toast.success('Histórico cadastrado!');
    
    // Recarregar dados de histórico
    if (selectedEscala?.id) {
      try {
        const novoHistorico = await escalaService.verificarHistorico(selectedEscala.id);
        setHistoricoData(novoHistorico);
        
        // Se não falta mais histórico, redirecionar
        if (!novoHistorico.faltaHistorico) {
          setIsHistoricoModalOpen(false);
          setSelectedEscala(null);
          toast.success('Todos os históricos cadastrados! Redirecionando...');
          setTimeout(() => {
            navigate(`/escalas/${selectedEscala.id}/calendario`);
          }, 1500);
        }
      } catch (error) {
        console.error('Erro ao recarregar histórico:', error);
      }
    }
  };

  const escalas = data?.content || [];
  const totalPages = data?.totalPages || 0;
  const totalElements = data?.totalElements || 0;

  return (
    <div className="space-y-6">
      {/* HEADER */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
            <Calendar className="w-8 h-8 text-primary" />
            Escalas
          </h1>
          <p className="text-gray-600 mt-1">
            Gerencie as escalas mensais de folgas
          </p>
        </div>

        <Button variant="primary" leftIcon={Plus} onClick={handleCreate}>
          Nova Escala
        </Button>
      </div>

      {/* CARDS DE ESTATÍSTICAS */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="card p-4">
          <p className="text-sm text-gray-600">Total de Escalas</p>
          <p className="text-2xl font-bold text-gray-900">{totalElements}</p>
        </div>
        <div className="card p-4">
          <p className="text-sm text-gray-600">Novas</p>
          <p className="text-2xl font-bold text-green-600">
            {escalas.filter(e => e.status === 'NOVA').length}
          </p>
        </div>
        <div className="card p-4">
          <p className="text-sm text-gray-600">Publicadas</p>
          <p className="text-2xl font-bold text-blue-600">
            {escalas.filter(e => e.status === 'PUBLICADA').length}
          </p>
        </div>
        <div className="card p-4">
          <p className="text-sm text-gray-600">Fechadas</p>
          <p className="text-2xl font-bold text-gray-600">
            {escalas.filter(e => e.status === 'FECHADA').length}
          </p>
        </div>
      </div>

      {/* FILTROS */}
      <div className="card p-6">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Filtros</h3>
        
        <div className="grid grid-cols-1 sm:grid-cols-5 gap-4">
          <Select value={setorFilter} onChange={(e) => setSetorFilter(e.target.value)}>
            <option value="">Todos os Setores</option>
            {setoresData?.content?.map((setor) => (
              <option key={setor.id} value={setor.id}>{setor.nome}</option>
            ))}
          </Select>

          <Select value={turnoFilter} onChange={(e) => setTurnoFilter(e.target.value)}>
            <option value="">Todos os Turnos</option>
            <option value="MANHA">🌅 Manhã</option>
            <option value="TARDE">☀️ Tarde</option>
            <option value="NOITE">🌙 Noite</option>
          </Select>

          <Select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="">Todos os Status</option>
            <option value="NOVA">🟢 Nova</option>
            <option value="PARCIAL">🟡 Parcial</option>
            <option value="PUBLICADA">🔵 Publicada</option>
            <option value="FECHADA">⚫ Fechada</option>
          </Select>

          <Select value={mesFilter} onChange={(e) => setMesFilter(e.target.value)}>
            <option value="">Todos os Meses</option>
            {[...Array(12)].map((_, i) => (
              <option key={i + 1} value={i + 1}>
                {new Date(2000, i).toLocaleString('pt-BR', { month: 'long' })}
              </option>
            ))}
          </Select>

          <Input
            type="number"
            placeholder="Ano"
            value={anoFilter}
            onChange={(e) => setAnoFilter(e.target.value)}
            min="2020"
            max="2030"
          />
        </div>
      </div>

      {/* TABELA */}
      <div className="card">
        <EscalaTable
          escalas={escalas}
          loading={isLoading}
          error={error}
          onEdit={handleEdit}
          onDelete={handleDelete}
          onRetry={refetch}
          onCreate={handleCreate}
          currentPage={page}
          totalPages={totalPages}
          pageSize={size}
          totalItems={totalElements}
          onPageChange={setPage}
          onPageSizeChange={setSize}
        />
      </div>

      {/* MODAIS */}
      <EscalaFormModal
        isOpen={isFormOpen}
        onClose={() => { setIsFormOpen(false); setSelectedEscala(null); }}
        escala={selectedEscala}
        onSuccess={handleFormSuccess}
      />

      <EscalaDeleteModal
        isOpen={isDeleteOpen}
        onClose={() => { setIsDeleteOpen(false); setSelectedEscala(null); }}
        escala={selectedEscala}
        onConfirm={handleConfirmDelete}
        isLoading={deleteMutation.isPending}
      />

      <HistoricoFolgaModal
        isOpen={isHistoricoModalOpen}
        onClose={() => {
          setIsHistoricoModalOpen(false);
          setSelectedEscala(null);
        }}
        dados={historicoData}
        onSelecionarColaborador={handleSelecionarColaborador}
      />

      <CadastroHistoricoModal
        isOpen={isCadastroHistoricoOpen}
        onClose={() => {
          setIsCadastroHistoricoOpen(false);
          setColaboradorSelecionado(null);
        }}
        colaborador={colaboradorSelecionado}
        onSuccess={handleHistoricoSuccess}
      />
    </div>
  );
}