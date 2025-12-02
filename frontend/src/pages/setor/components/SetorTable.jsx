import { Edit, Trash2 } from 'lucide-react';
import { SkeletonLoader, ErrorState, EmptyState } from '../../../components/common';

/**
 * ========================================
 * TABELA DE SETORES - COM ESTADOS
 * ========================================
 */

export function SetorTable({ setores, loading, error, onEdit, onDelete, onRetry, onCreate }) {
  
  console.log('🔍 SetorTable:', { loading, error, setores });
  
  // ========================================
  // ESTADO: LOADING
  // ========================================
  if (loading) {
    console.log('✅ Mostrando LOADING');
    return <SkeletonLoader rows={5} />;
  }

  // ========================================
  // ESTADO: ERROR (PRIORIDADE!)
  // ========================================
  if (error) {
    console.log('❌ Mostrando ERROR:', error);
    return (
      <ErrorState 
        message={error.message || error.friendlyMessage || 'Erro ao carregar setores'} 
        onRetry={onRetry}
      />
    );
  }

  // ========================================
  // ESTADO: EMPTY (DEPOIS DO ERROR!)
  // ========================================
  if (!setores || setores.length === 0) {
    console.log('📋 Mostrando EMPTY');
    return <EmptyState onAction={onCreate} />;
  }

  // ========================================
  // ESTADO: SUCCESS (DADOS)
  // ========================================
  console.log('✅ Mostrando DADOS:', setores.length, 'setores');
  
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3.5 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider w-3/5">
              Nome do Setor
            </th>
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/5">
              Status
            </th>
            <th className="px-6 py-3.5 text-right text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/5">
              Ações
            </th>
          </tr>
        </thead>

        <tbody className="bg-white divide-y divide-gray-100">
          {setores.map((setor, index) => (
            <tr 
              key={setor.id} 
              className="hover:bg-gray-50 transition-colors duration-150 group"
            >
              <td className="px-6 py-4">
                <div className="flex items-center gap-3">
                  <div className="flex-shrink-0">
                    <div className="w-8 h-8 bg-gradient-to-br from-primary to-primary-dark rounded-lg flex items-center justify-center shadow-sm">
                      <span className="text-white font-bold text-xs">
                        {index + 1}
                      </span>
                    </div>
                  </div>
                  
                  <div>
                    <p className="text-sm font-semibold text-gray-900 leading-tight">
                      {setor.nome}
                    </p>
                  </div>
                </div>
              </td>

              <td className="px-6 py-4">
                <div className="flex justify-center">
                  <span className={`
                    inline-flex items-center gap-1.5 px-2.5 py-1 text-xs font-semibold rounded-full
                    ${setor.ativo 
                      ? 'bg-green-50 text-green-700 border border-green-200' 
                      : 'bg-gray-100 text-gray-600 border border-gray-300'
                    }
                  `}>
                    <span className={`w-1.5 h-1.5 rounded-full ${setor.ativo ? 'bg-green-500' : 'bg-gray-400'}`} />
                    {setor.ativo ? 'Ativo' : 'Inativo'}
                  </span>
                </div>
              </td>

              <td className="px-6 py-4">
                <div className="flex items-center justify-end gap-2">
                  <button
                    onClick={() => onEdit(setor)}
                    className="p-2 text-gray-400 hover:text-primary hover:bg-primary-light rounded-lg transition-all duration-150 group-hover:opacity-100 opacity-70"
                    title="Editar setor"
                    aria-label={`Editar ${setor.nome}`}
                  >
                    <Edit className="w-4 h-4" />
                  </button>
                  
                  <button
                    onClick={() => onDelete(setor)}
                    className="p-2 text-gray-400 hover:text-error hover:bg-error-light rounded-lg transition-all duration-150 group-hover:opacity-100 opacity-70"
                    title="Deletar setor"
                    aria-label={`Deletar ${setor.nome}`}
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
