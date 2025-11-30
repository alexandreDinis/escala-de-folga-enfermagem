import { AlertTriangle, Trash2 } from 'lucide-react';
import { Button, Modal } from '../../../components/common';

/**
 * ========================================
 * MODAL DE CONFIRMAÇÃO DE EXCLUSÃO
 * ========================================
 */

export function SetorDeleteModal({ isOpen, onClose, onConfirm, setor, loading }) {
  if (!setor) return null;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title="Confirmar Exclusão"
      size="md"
      footer={
        <>
          <Button variant="ghost" onClick={onClose} disabled={loading}>
            Cancelar
          </Button>
          <Button
            variant="danger"
            leftIcon={Trash2}
            onClick={onConfirm}
            loading={loading}
            disabled={loading}
          >
            Sim, Deletar
          </Button>
        </>
      }
    >
      <div className="space-y-4">
        <div className="flex items-start gap-4 p-4 bg-warning-light border border-warning rounded-lg">
          <AlertTriangle className="w-6 h-6 text-warning flex-shrink-0 mt-0.5" />
          <div>
            <h4 className="font-bold text-gray-900 mb-1">Atenção!</h4>
            <p className="text-sm text-gray-700">
              Esta ação não pode ser desfeita. O setor será marcado como inativo.
            </p>
          </div>
        </div>

        <div className="p-4 bg-gray-50 rounded-lg border border-gray-200">
          <p className="text-sm text-gray-600 mb-2">Você está prestes a deletar:</p>
          <p className="text-lg font-bold text-gray-900">{setor.nome}</p>
          <p className="text-sm text-gray-500 mt-1">ID: #{setor.id}</p>
        </div>

        <p className="text-sm text-gray-600">
          Tem certeza que deseja continuar?
        </p>
      </div>
    </Modal>
  );
}