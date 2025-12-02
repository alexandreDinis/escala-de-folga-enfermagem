/**
 * ========================================
 * EMPTY STATE
 * ========================================
 * 
 * Componente mostrado quando não há dados
 */

export const EmptyState = ({ message, action }) => (
  <tr>
    <td colSpan="4" className="px-6 py-12 text-center">
      <div className="flex flex-col items-center gap-4">
        {/* Ícone */}
        <svg 
          className="w-16 h-16 text-gray-400" 
          fill="none" 
          stroke="currentColor" 
          viewBox="0 0 24 24"
        >
          <path 
            strokeLinecap="round" 
            strokeLinejoin="round" 
            strokeWidth={2} 
            d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" 
          />
        </svg>
        
        {/* Mensagem */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-1">
            Nenhum setor encontrado
          </h3>
          <p className="text-sm text-gray-600">
            {message || 'Comece criando um novo setor.'}
          </p>
        </div>
        
        {/* Ação */}
        {action}
      </div>
    </td>
  </tr>
);

export default EmptyState;
