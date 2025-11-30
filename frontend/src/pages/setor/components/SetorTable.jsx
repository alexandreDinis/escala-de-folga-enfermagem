import { Edit, Trash2, Building2 } from 'lucide-react';
import { Button } from '../../../components/common';

/**
 * ========================================
 * TABELA DE SETORES - REDESIGN OTIMIZADO
 * ========================================
 */

export function SetorTable({ setores, loading, onEdit, onDelete }) {
  if (loading) {
    return (
      <div className="flex items-center justify-center py-16">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-gray-500">Carregando setores...</p>
        </div>
      </div>
    );
  }

  if (!setores || setores.length === 0) {
    return (
      <div className="text-center py-16">
        <Building2 className="w-16 h-16 text-gray-300 mx-auto mb-4" />
        <p className="text-gray-500 text-lg font-semibold mb-2">
          Nenhum setor encontrado
        </p>
        <p className="text-gray-400 text-sm">
          Clique em "Novo Setor" para adicionar o primeiro setor
        </p>
      </div>
    );
  }

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        {/* ========================================
            HEADER - Mais Compacto
            ======================================== */}
        <thead className="bg-gray-50">
          <tr>
            {/* Nome - 60% largura */}
            <th className="px-6 py-3.5 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider w-3/5">
              Nome do Setor
            </th>
            
            {/* Status - 20% largura */}
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/5">
              Status
            </th>
            
            {/* Ações - 20% largura */}
            <th className="px-6 py-3.5 text-right text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/5">
              Ações
            </th>
          </tr>
        </thead>

        {/* ========================================
            BODY - Design Limpo e Compacto
            ======================================== */}
        <tbody className="bg-white divide-y divide-gray-100">
          {setores.map((setor, index) => (
            <tr 
              key={setor.id} 
              className="hover:bg-gray-50 transition-colors duration-150 group"
            >
              {/* ========================================
                  COLUNA NOME - Sem ícone, mais espaço
                  ======================================== */}
              <td className="px-6 py-4">
                <div className="flex items-center gap-3">
                  {/* Badge numérico pequeno */}
                  <div className="flex-shrink-0">
                    <div className="w-8 h-8 bg-gradient-to-br from-primary to-primary-dark rounded-lg flex items-center justify-center shadow-sm">
                      <span className="text-white font-bold text-xs">
                        {index + 1}
                      </span>
                    </div>
                  </div>
                  
                  {/* Nome do setor */}
                  <div>
                    <p className="text-sm font-semibold text-gray-900 leading-tight">
                      {setor.nome}
                    </p>
                  </div>
                </div>
              </td>

              {/* ========================================
                  COLUNA STATUS - Centralizado e Compacto
                  ======================================== */}
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

              {/* ========================================
                  COLUNA AÇÕES - Botões Icônicos Compactos
                  ======================================== */}
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
