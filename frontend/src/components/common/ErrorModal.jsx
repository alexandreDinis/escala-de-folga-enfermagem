import { XCircle, AlertTriangle, WifiOff, ServerCrash } from 'lucide-react';
import { Button } from './Button';
import { Modal } from './Modal';

/**
 * ========================================
 * MODAL DE ERRO GLOBAL
 * ========================================
 * 
 * Componente reutilizável para exibir erros em todo o sistema
 * 
 * Tipos de erro suportados:
 * - validation: Erro de validação (400)
 * - server: Erro de servidor (500)
 * - network: Erro de conexão
 * - generic: Erro genérico
 */

const ERROR_CONFIGS = {
  validation: {
    icon: AlertTriangle,
    iconColor: 'text-warning',
    bgColor: 'bg-warning-light',
    borderColor: 'border-warning',
    title: 'Erro de Validação',
  },
  server: {
    icon: ServerCrash,
    iconColor: 'text-error',
    bgColor: 'bg-error-light',
    borderColor: 'border-error',
    title: 'Erro no Servidor',
  },
  network: {
    icon: WifiOff,
    iconColor: 'text-secondary',
    bgColor: 'bg-secondary-light',
    borderColor: 'border-secondary',
    title: 'Erro de Conexão',
  },
  generic: {
    icon: XCircle,
    iconColor: 'text-error',
    bgColor: 'bg-error-light',
    borderColor: 'border-error',
    title: 'Erro Inesperado',
  },
};

export function ErrorModal({ 
  isOpen, 
  onClose, 
  message, 
  type = 'generic',
  title,
  onRetry,
}) {
  const config = ERROR_CONFIGS[type] || ERROR_CONFIGS.generic;
  const Icon = config.icon;
  const displayTitle = title || config.title;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      size="md"
      showCloseButton={false}
      closeOnOverlayClick={true}
    >
      <div className="text-center py-4">
        {/* Ícone de Erro */}
        <div 
          className={`w-16 h-16 ${config.bgColor} rounded-full flex items-center justify-center mx-auto mb-4`}
        >
          <Icon className={`w-8 h-8 ${config.iconColor}`} />
        </div>

        {/* Título */}
        <h2 
          className="text-2xl font-bold text-gray-900 mb-4"
          id="error-modal-title"
        >
          {displayTitle}
        </h2>

        {/* Mensagem do Backend/Sistema */}
        <div 
          className={`${config.bgColor} ${config.borderColor} border rounded-lg p-4 mb-6`}
          id="error-modal-description"
        >
          <p className="text-gray-900 text-base whitespace-pre-line">
            {message}
          </p>
        </div>

        {/* Botões de Ação */}
        <div className="flex items-center justify-center gap-3">
          {onRetry && (
            <Button
              variant="outline"
              onClick={() => {
                onClose();
                onRetry();
              }}
              className="flex-1"
            >
              Tentar Novamente
            </Button>
          )}
          
          <Button
            variant="primary"
            onClick={onClose}
            className={onRetry ? 'flex-1' : 'w-full'}
            autoFocus
          >
            Entendi
          </Button>
        </div>
      </div>
    </Modal>
  );
}