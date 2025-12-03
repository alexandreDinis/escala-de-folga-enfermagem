import { AlertTriangle } from 'lucide-react';
import { Modal, Button } from '../../../components/common';

const MESES = [
  'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
  'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
];

export function EscalaDeleteModal({ isOpen, onClose, escala, onConfirm, isLoading }) {
  if (!escala) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Confirmar Exclusão">
      <div className="space-y-4">
        <div className="flex items-center gap-3 p-4 bg-error-light rounded-lg">
          <AlertTriangle className="w-6 h-6 text-error flex-shrink-0" />
          <p className="text-sm text-gray-700">
            Esta ação não pode ser desfeita. A escala será permanentemente removida.
          </p>
        </div>

        <div className="p-4 bg-gray-50 rounded-lg">
          <p className="text-sm text-gray-600 mb-2">Você está prestes a deletar:</p>
          <p className="text-lg font-semibold text-gray-900">
            {MESES[escala.mes - 1]} / {escala.ano} - {escala.setorNome}
          </p>
          <p className="text-sm text-gray-600 mt-1">
            Turno: {escala.turno} | Folgas: {escala.folgasPermitidas}
          </p>
        </div>

        <div className="flex gap-3 pt-4">
          <Button
            type="button"
            variant="secondary"
            onClick={onClose}
            disabled={isLoading}
            className="flex-1"
          >
            Cancelar
          </Button>
          <Button
            type="button"
            variant="danger"
            onClick={onConfirm}
            isLoading={isLoading}
            className="flex-1"
          >
            Deletar Escala
          </Button>
        </div>
      </div>
    </Modal>
  );
}