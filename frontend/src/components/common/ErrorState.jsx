/**
 * ========================================
 * ERROR STATE
 * ========================================
 * 
 * Componente mostrado quando há erro ao carregar dados
 */

export const ErrorState = ({ message, onRetry }) => (
  <tr>
    <td colSpan="4" className="px-6 py-12 text-center">
      <div className="flex flex-col items-center gap-4">
        {/* Ícone de erro */}
        <svg 
          className="w-16 h-16 text-red-400" 
          fill="none" 
          stroke="currentColor" 
          viewBox="0 0 24 24"
        >
          <path 
            strokeLinecap="round" 
            strokeLinejoin="round" 
            strokeWidth={2} 
            d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" 
          />
        </svg>
        
        {/* Mensagem */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-1">
            Erro ao carregar dados
          </h3>
          <p className="text-sm text-gray-600">
            {message || 'Não foi possível conectar ao servidor. Tente novamente.'}
          </p>
        </div>
        
        {/* Botão de retry */}
        {onRetry && (
          <button
            onClick={onRetry}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            Tentar novamente
          </button>
        )}
      </div>
    </td>
  </tr>
);

export default ErrorState;
