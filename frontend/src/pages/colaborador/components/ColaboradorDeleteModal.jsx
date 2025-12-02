import { AlertTriangle } from 'lucide-react';
import { Modal, Button } from '../../../components/common';

/**
 * ========================================
 * MODAL DE CONFIRMAÇÃO - DELETE
 * ========================================
 */

export function ColaboradorDeleteModal({ 
  isOpen, 
  onClose, 
  onConfirm, 
  colaborador, 
  loading 
}) {
  if (!colaborador) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="sm">
      <div className="p-6">
        {/* Ícone de Alerta */}
        <div className="flex justify-center mb-4">
          <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center">
            <AlertTriangle className="w-8 h-8 text-red-600" />
          </div>
        </div>

        {/* Título */}
        <h2 className="text-xl font-bold text-gray-900 text-center mb-2">
          Deletar Colaborador
        </h2>

        {/* Mensagem */}
        <p className="text-gray-600 text-center mb-6">
          Tem certeza que deseja deletar o colaborador <strong>{colaborador.nome}</strong>?
          Esta ação não pode ser desfeita.
        </p>

        {/* Botões */}
        <div className="flex gap-3">
          <Button
            variant="outline"
            onClick={onClose}
            className="flex-1"
            disabled={loading}
          >
            Cancelar
          </Button>
          <Button
            variant="danger"
            onClick={onConfirm}
            className="flex-1"
            loading={loading}
          >
            Deletar
          </Button>
        </div>
      </div>
    </Modal>
  );
}
