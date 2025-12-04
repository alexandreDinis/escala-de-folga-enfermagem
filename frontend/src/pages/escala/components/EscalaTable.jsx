import { Edit, Trash2, Calendar } from 'lucide-react';
import { Button } from '../../../components/common';
import { SkeletonLoader, ErrorState, EmptyState, Pagination } from '../../../components/common';

const STATUS_CONFIG = {
  NOVA: {
    label: 'Nova',
    color: 'text-green-700 bg-green-50 border-green-200',
    icon: '🟢'
  },
  PARCIAL: {
    label: 'Parcial',
    color: 'text-yellow-700 bg-yellow-50 border-yellow-200',
    icon: '🟡'
  },
  PUBLICADA: {
    label: 'Publicada',
    color: 'text-blue-700 bg-blue-50 border-blue-200',
    icon: '🔵'
  },
  FECHADA: {
    label: 'Fechada',
    color: 'text-gray-700 bg-gray-50 border-gray-200',
    icon: '⚫'
  }
};

const TURNO_CONFIG = {
  MANHA: { icon: '🌅', label: 'Manhã', color: 'text-orange-700 bg-orange-50 border-orange-200' },
  TARDE: { icon: '☀️', label: 'Tarde', color: 'text-yellow-700 bg-yellow-50 border-yellow-200' },
  NOITE: { icon: '🌙', label: 'Noite', color: 'text-indigo-700 bg-indigo-50 border-indigo-200' },
};

const MESES = [
  'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
  'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
];

export function EscalaTable({ 
  escalas, 
  loading, 
  error, 
  onEdit, 
  onDelete,
  onAbrir,  // ✅ ADICIONAR
  onRetry, 
  onCreate,
  currentPage,
  totalPages,
  pageSize,
  totalItems,
  onPageChange,
  onPageSizeChange
}) {
  
  if (loading) {
    return <SkeletonLoader rows={5} />;
  }

  if (error) {
    return (
      <ErrorState 
        message={error.message || error.friendlyMessage || 'Erro ao carregar escalas'} 
        onRetry={onRetry}
      />
    );
  }

  if (!escalas || escalas.length === 0) {
    return (
      <EmptyState 
        onAction={onCreate}
        message="Nenhuma escala encontrada"
        actionLabel="Nova Escala"
        colSpan={7}  // ✅ AUMENTAR para 7 (adicionou coluna Abrir)
      />
    );
  }

  return (
    <>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3.5 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                #
              </th>
              <th className="px-6 py-3.5 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Período
              </th>
              <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Setor
              </th>
              <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Turno
              </th>
              <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Folgas
              </th>
              <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Status
              </th>
              <th className="px-6 py-3.5 text-right text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Ações
              </th>
            </tr>
          </thead>

          <tbody className="bg-white divide-y divide-gray-100">
            {escalas.map((escala, index) => {
              const statusConfig = STATUS_CONFIG[escala.status] || {};
              const turnoConfig = TURNO_CONFIG[escala.turno] || {};

              return (
                <tr 
                  key={escala.id} 
                  className="hover:bg-gray-50 transition-colors duration-150 group"
                >
                  <td className="px-6 py-4">
                    <div className="w-8 h-8 bg-gradient-to-br from-primary to-primary-dark rounded-lg flex items-center justify-center shadow-sm">
                      <span className="text-white font-bold text-xs">
                        {currentPage * pageSize + index + 1}
                      </span>
                    </div>
                  </td>

                  <td className="px-6 py-4">
                    <div>
                      <p className="text-sm font-semibold text-gray-900">
                        {MESES[escala.mes - 1]} / {escala.ano}
                      </p>
                      <p className="text-xs text-gray-500">
                        {escala.mes.toString().padStart(2, '0')}/{escala.ano}
                      </p>
                    </div>
                  </td>

                  <td className="px-6 py-4">
                    <div className="flex justify-center">
                      <span className="text-sm font-medium text-gray-700">
                        {escala.setorNome || '-'}
                      </span>
                    </div>
                  </td>

                  <td className="px-6 py-4">
                    <div className="flex justify-center">
                      <span className={`
                        inline-flex items-center gap-1.5 px-2.5 py-1 text-xs font-semibold rounded-full border
                        ${turnoConfig.color}
                      `}>
                        <span>{turnoConfig.icon}</span>
                        {turnoConfig.label}
                      </span>
                    </div>
                  </td>

                  <td className="px-6 py-4">
                    <div className="flex justify-center">
                      <span className="text-sm font-bold text-gray-900">
                        {escala.folgasPermitidas}
                      </span>
                    </div>
                  </td>

                  <td className="px-6 py-4">
                    <div className="flex justify-center">
                      <span className={`
                        inline-flex items-center gap-1.5 px-2.5 py-1 text-xs font-semibold rounded-full border
                        ${statusConfig.color}
                      `}>
                        <span>{statusConfig.icon}</span>
                        {statusConfig.label}
                      </span>
                    </div>
                  </td>

                  <td className="px-6 py-4">
                    <div className="flex items-center justify-end gap-2">
                      {/*  BOTÃO ABRIR */}
                      <button
                        onClick={() => onAbrir(escala)}
                        className="p-2 text-gray-400 hover:text-primary hover:bg-primary-light rounded-lg transition-all duration-150 group-hover:opacity-100 opacity-70"
                        title="Abrir calendário"
                      >
                        <Calendar className="w-4 h-4" />
                      </button>

                      {/* BOTÃO EDITAR */}
                      <button
                        onClick={() => onEdit(escala)}
                        className="p-2 text-gray-400 hover:text-primary hover:bg-primary-light rounded-lg transition-all duration-150 group-hover:opacity-100 opacity-70"
                        title="Editar escala"
                      >
                        <Edit className="w-4 h-4" />
                      </button>
                      
                      {/* BOTÃO DELETAR */}
                      <button
                        onClick={() => onDelete(escala)}
                        className="p-2 text-gray-400 hover:text-error hover:bg-error-light rounded-lg transition-all duration-150 group-hover:opacity-100 opacity-70"
                        title="Deletar escala"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        pageSize={pageSize}
        totalItems={totalItems}
        onPageChange={onPageChange}
        onPageSizeChange={onPageSizeChange}
      />
    </>
  );
}