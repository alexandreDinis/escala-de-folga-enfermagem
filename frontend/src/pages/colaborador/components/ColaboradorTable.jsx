import { Edit, Trash2 } from 'lucide-react';
import { SkeletonLoader, ErrorState, EmptyState } from '../../../components/common';

/**
 * ========================================
 * TABELA DE COLABORADORES
 * ========================================
 */

// Mapeamento de ícones e cores por cargo
const CARGO_CONFIG = {
  ENFERMEIRO: {
    icon: '🩺',
    label: 'Enfermeiro',
    color: 'text-blue-700 bg-blue-50 border-blue-200',
  },
  TECNICO: {
    icon: '🔧',
    label: 'Técnico',
    color: 'text-purple-700 bg-purple-50 border-purple-200',
  },
};

// Mapeamento de cores por turno
const TURNO_CONFIG = {
  MANHA: {
    icon: '🌅',
    label: 'Manhã',
    color: 'text-orange-700 bg-orange-50 border-orange-200',
  },
  TARDE: {
    icon: '☀️',
    label: 'Tarde',
    color: 'text-yellow-700 bg-yellow-50 border-yellow-200',
  },
  NOITE: {
    icon: '🌙',
    label: 'Noite',
    color: 'text-indigo-700 bg-indigo-50 border-indigo-200',
  },
};

export function ColaboradorTable({ 
  colaboradores, 
  loading, 
  error, 
  onEdit, 
  onDelete, 
  onRetry, 
  onCreate 
}) {
  
  // ========================================
  // LOADING
  // ========================================
  if (loading) {
    return <SkeletonLoader rows={5} />;
  }

  // ========================================
  // ERROR
  // ========================================
  if (error) {
    return (
      <ErrorState 
        message={error.message || error.friendlyMessage || 'Erro ao carregar colaboradores'} 
        onRetry={onRetry}
      />
    );
  }

  // ========================================
  // EMPTY
  // ========================================
  if (!colaboradores || colaboradores.length === 0) {
    return (
      <EmptyState 
        onAction={onCreate}
        message="Nenhum colaborador encontrado"
        actionLabel="Novo Colaborador"
        colSpan={5}
      />
    );
  }

  // ========================================
  // SUCCESS (DADOS)
  // ========================================
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        {/* HEADER */}
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3.5 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider w-2/5">
              Nome
            </th>
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Cargo
            </th>
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Turno
            </th>
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Setor
            </th>
            <th className="px-6 py-3.5 text-right text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Ações
            </th>
          </tr>
        </thead>

        {/* BODY */}
        <tbody className="bg-white divide-y divide-gray-100">
          {colaboradores.map((colaborador, index) => {
            const cargoConfig = CARGO_CONFIG[colaborador.cargo] || {};
            const turnoConfig = TURNO_CONFIG[colaborador.turno] || {};

            return (
              <tr 
                key={colaborador.id} 
                className="hover:bg-gray-50 transition-colors duration-150 group"
              >
                {/* NOME */}
                <td className="px-6 py-4">
                  <div className="flex items-center gap-3">
                    {/* Badge numérico */}
                    <div className="flex-shrink-0">
                      <div className="w-8 h-8 bg-gradient-to-br from-primary to-primary-dark rounded-lg flex items-center justify-center shadow-sm">
                        <span className="text-white font-bold text-xs">
                          {index + 1}
                        </span>
                      </div>
                    </div>
                    
                    {/* Nome */}
                    <div>
                      <p className="text-sm font-semibold text-gray-900 leading-tight">
                        {colaborador.nome}
                      </p>
                    </div>
                  </div>
                </td>

                {/* CARGO */}
                <td className="px-6 py-4">
                  <div className="flex justify-center">
                    <span className={`
                      inline-flex items-center gap-1.5 px-2.5 py-1 text-xs font-semibold rounded-full border
                      ${cargoConfig.color}
                    `}>
                      <span>{cargoConfig.icon}</span>
                      {cargoConfig.label}
                    </span>
                  </div>
                </td>

                {/* TURNO */}
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

                {/* SETOR */}
                <td className="px-6 py-4">
                  <div className="flex justify-center">
                    <span className="text-sm text-gray-700 font-medium">
                      {colaborador.setorNome || '-'}
                    </span>
                  </div>
                </td>

                {/* AÇÕES */}
                <td className="px-6 py-4">
                  <div className="flex items-center justify-end gap-2">
                    <button
                      onClick={() => onEdit(colaborador)}
                      className="p-2 text-gray-400 hover:text-primary hover:bg-primary-light rounded-lg transition-all duration-150 group-hover:opacity-100 opacity-70"
                      title="Editar colaborador"
                      aria-label={`Editar ${colaborador.nome}`}
                    >
                      <Edit className="w-4 h-4" />
                    </button>
                    
                    <button
                      onClick={() => onDelete(colaborador)}
                      className="p-2 text-gray-400 hover:text-error hover:bg-error-light rounded-lg transition-all duration-150 group-hover:opacity-100 opacity-70"
                      title="Deletar colaborador"
                      aria-label={`Deletar ${colaborador.nome}`}
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
  );
}
