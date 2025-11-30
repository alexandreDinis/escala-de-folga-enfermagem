import { AlertTriangle, Check } from 'lucide-react';
import { Button, Modal } from '../../../components/common';

/**
 * ========================================
 * MODAL DE CONFIRMAÇÃO DE CRIAÇÃO
 * ========================================
 * 
 * Modal que solicita confirmação antes de criar um novo setor
 */

export function SetorConfirmModal({ 
  isOpen, 
  onClose, 
  onConfirm, 
  setorNome, 
  loading 
}) {
  if (!setorNome) return null;

  // Truncar nome se muito longo
  const nomeExibido = setorNome.length > 100 
    ? `${setorNome.substring(0, 97)}...` 
    : setorNome;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      size="md"
      showCloseButton={false}
      closeOnOverlayClick={!loading}
    >
      <div className="text-center py-4">
        {/* Ícone de Alerta */}
        <div className="w-16 h-16 bg-secondary-light rounded-full flex items-center justify-center mx-auto mb-4">
          <AlertTriangle className="w-8 h-8 text-secondary" />
        </div>

        {/* Título */}
        <h2 
          id="confirm-title"
          className="text-2xl font-bold text-gray-900 mb-4"
        >
          Confirmar Criação
        </h2>

        {/* Mensagem */}
        <div 
          id="confirm-description"
          className="space-y-2 mb-6"
        >
          <p className="text-gray-600 text-base">
            O setor{' '}
            <strong 
              className="text-primary font-semibold"
              aria-label="nome do setor"
            >
              '{nomeExibido}'
            </strong>
            {' '}será criado.
          </p>
          <p className="text-gray-900 font-medium text-base">
            Tem certeza que gostaria de criar?
          </p>
        </div>

        {/* Botões de Ação */}
        <div 
          className="flex items-center justify-between gap-3"
          role="group"
          aria-label="Ações de confirmação"
        >
          <Button
            variant="ghost"
            onClick={onClose}
            disabled={loading}
            className="flex-1"
            aria-label="Cancelar criação do setor"
          >
            Cancelar
          </Button>

          <Button
            variant="primary"
            leftIcon={Check}
            onClick={onConfirm}
            loading={loading}
            disabled={loading}
            className="flex-1"
            aria-label="Confirmar criação do setor"
            aria-busy={loading}
          >
            Sim, Criar Setor
          </Button>
        </div>
      </div>
    </Modal>
  );
}