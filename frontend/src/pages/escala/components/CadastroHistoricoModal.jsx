import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Calendar } from 'lucide-react';
import { Modal, Input, Button } from '../../../components/common';
import folgaService from '../../../services/folga.service';
import toast from 'react-hot-toast';

export function CadastroHistoricoModal({ isOpen, onClose, colaborador, onSuccess }) {
  const [dataSolicitada, setDataSolicitada] = useState('');

  const mutation = useMutation({
    mutationFn: () =>
      folgaService.cadastrarHistorico(colaborador?.id, dataSolicitada),
    onSuccess: () => {
      toast.success(`Histórico cadastrado para ${colaborador?.nome}!`);
      setDataSolicitada('');
      onSuccess();
      onClose();
    },
    onError: (error) => {
      const message = error.response?.data?.message || 'Erro ao cadastrar histórico';
      toast.error(message);
    },
  });

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!dataSolicitada) {
      toast.error('Selecione uma data!');
      return;
    }

    mutation.mutate();
  };

  if (!colaborador) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Cadastrar Última Folga">
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* INFO DO COLABORADOR */}
        <div className="p-4 bg-blue-50 border border-blue-200 rounded-lg">
          <p className="text-sm text-blue-600 font-medium mb-1">Colaborador</p>
          <p className="text-lg font-semibold text-blue-900">{colaborador.nome}</p>
          <div className="flex gap-4 mt-2 text-sm">
            <span className="text-blue-700">
              <strong>Cargo:</strong> {colaborador.cargo}
            </span>
            <span className="text-blue-700">
              <strong>Turno:</strong> {colaborador.turno}
            </span>
          </div>
        </div>

        {/* INPUT DE DATA */}
        <div>
          <label className="block text-sm font-semibold text-gray-700 mb-2">
            <Calendar className="w-4 h-4 inline mr-2" />
            Data da Última Folga
          </label>
          <input
            type="date"
            value={dataSolicitada}
            onChange={(e) => setDataSolicitada(e.target.value)}
            max={new Date().toISOString().split('T')[0]}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
            required
          />
          <p className="text-xs text-gray-500 mt-1">
            Selecione a data da última folga deste colaborador
          </p>
        </div>

        {/* AÇÕES */}
        <div className="flex gap-3 pt-4">
          <Button
            type="button"
            variant="secondary"
            onClick={onClose}
            disabled={mutation.isPending}
            className="flex-1"
          >
            Cancelar
          </Button>
          <Button
            type="submit"
            variant="primary"
            isLoading={mutation.isPending}
            className="flex-1"
          >
            Cadastrar
          </Button>
        </div>
      </form>
    </Modal>
  );
}