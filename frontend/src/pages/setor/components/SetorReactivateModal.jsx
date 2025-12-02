import { RefreshCw, AlertTriangle } from 'lucide-react';
import { Button, Modal } from '../../../components/common';

/**
 * ========================================
 * MODAL DE REATIVAÇÃO DE SETOR
 * ========================================
 * 
 * Pergunta ao usuário se deseja reativar um setor inativo
 */

export function SetorReactivateModal({ 
  isOpen, 
  onClose, 
  onConfirm, 
  setorNome, 
  loading 
}) {
  if (!setorNome) return null;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      size="md"
      showCloseButton={false}
      closeOnOverlayClick={!loading}
    >
      <div className="text-center py-4">
        {/* Ícone de Reativação */}
        <div className="w-16 h-16 bg-warning-light rounded-full flex items-center justify-center mx-auto mb-4">
          <RefreshCw className="w-8 h-8 text-warning" />
        </div>

        {/* Título */}
        <h2 className="text-2xl font-bold text-gray-900 mb-4">
          Setor Inativo Encontrado
        </h2>

        {/* Mensagem */}
        <div className="space-y-3 mb-6">
          <div className="bg-warning-light border border-warning rounded-lg p-4">
            <div className="flex items-start gap-3">
              <AlertTriangle className="w-5 h-5 text-warning flex-shrink-0 mt-0.5" />
              <div className="text-left">
                <p className="text-gray-900 font-semibold mb-1">
                  Já existe um setor semelhante:
                </p>
                <p className="text-primary font-bold text-lg">
                  '{setorNome}'
                </p>
              </div>
            </div>
          </div>

          <p className="text-gray-900 font-medium text-base">
            Este setor está <span className="text-error font-bold">inativo</span>. Deseja reativá-lo?
          </p>

          <div className="bg-info-light border border-info rounded-lg p-3">
            <p className="text-sm text-gray-700">
              <strong className="text-info-dark">ℹ️ Nota:</strong> Ao reativar, o setor voltará a estar disponível para uso no sistema.
            </p>
          </div>
        </div>

        {/* Botões de Ação */}
        <div className="flex items-center justify-between gap-3">
          <Button
            variant="ghost"
            onClick={onClose}
            disabled={loading}
            className="flex-1"
          >
            Não, Cancelar
          </Button>

          <Button
            variant="primary"
            leftIcon={RefreshCw}
            onClick={onConfirm}
            loading={loading}
            disabled={loading}
            className="flex-1"
            aria-busy={loading}
          >
            Sim, Reativar
          </Button>
        </div>
      </div>
    </Modal>
  );
}