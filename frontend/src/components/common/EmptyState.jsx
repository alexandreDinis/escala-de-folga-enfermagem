import { FolderOpen } from 'lucide-react';
import { Button } from './Button';

/**
 * ========================================
 * EMPTY STATE PARA TABELAS
 * ========================================
 * Exibe mensagem quando não há dados
 */

export default function EmptyState({ 
  onAction, 
  message = 'Nenhum colaborador encontrado',
  actionLabel = 'Novo Colaborador',
  colSpan = 5 
}) {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full">
        <tbody>
          <tr>
            <td colSpan={colSpan} className="px-6 py-16">
              <div className="flex flex-col items-center justify-center text-center">
                {/* Ícone */}
                <div className="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mb-4">
                  <FolderOpen className="w-10 h-10 text-gray-400" />
                </div>

                {/* Mensagem */}
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  {message}
                </h3>
                <p className="text-sm text-gray-500 mb-6 max-w-md">
                  Não encontramos nenhum colaborador com os filtros aplicados. 
                  Tente ajustar os filtros ou crie um novo colaborador.
                </p>

                {/* Botão */}
                {onAction && (
                  <Button variant="primary" onClick={onAction}>
                    {actionLabel}
                  </Button>
                )}
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}
