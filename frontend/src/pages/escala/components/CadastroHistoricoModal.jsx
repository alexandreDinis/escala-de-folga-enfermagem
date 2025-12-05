import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Button } from '../../../components/common';
import folgaService from '../../../services/folga.service';
import toast from 'react-hot-toast';

export function CadastroHistoricoModal({ 
  isOpen, 
  onClose, 
  colaborador, 
  escalaId,
  onSuccess 
}) {
  const [dataSolicitada, setDataSolicitada] = useState('');
  const [erroData, setErroData] = useState('');
  const [dataMinima, setDataMinima] = useState(null);

  // ✅ Mutation para validar data
  const validacaoMutation = useMutation({
    mutationFn: (data) => folgaService.validarDataUltimaFolga({
      colaboradorId: colaborador?.id,
      dataSolicitada: data,
      escalaId: escalaId
    }),
    onSuccess: (response) => {
      console.log('🔍 Resposta validação:', response);
      
      if (!response.valido) {
        setErroData(response.mensagem);
        setDataMinima(response.dataMinimPermitida);
      } else {
        setErroData('');
        setDataMinima(null);
      }
    },
    onError: (error) => {
      console.error('❌ Erro validação:', error);
      setErroData('Erro ao validar data');
    },
  });

  // Mutation para cadastrar
  const mutation = useMutation({
    mutationFn: () =>
      folgaService.cadastrarHistorico(
        colaborador?.id, 
        dataSolicitada,
        escalaId
      ),
    onSuccess: () => {
      toast.success(`✅ Histórico cadastrado para ${colaborador?.nome}!`);
      setDataSolicitada('');
      setErroData('');
      onSuccess();
      onClose();
    },
    onError: (error) => {
      const message = error.response?.data?.[0]?.mensagem 
        || error.response?.data?.message 
        || error.response?.data?.erro
        || 'Erro ao cadastrar histórico';
      toast.error(`❌ ${message}`);
    },
  });

  const handleDataChange = (e) => {
    const data = e.target.value;
    setDataSolicitada(data);
    
    console.log('📅 Data selecionada:', data);
    
    // ✅ Validar data quando mudar
    if (data && escalaId) {
      console.log('📤 Enviando para validação:', { 
        colaboradorId: colaborador?.id, 
        dataSolicitada: data, 
        escalaId 
      });
      validacaoMutation.mutate(data);
    }
  };

  const handleSubmit = () => {
    // ✅ Validação 1: Data selecionada
    if (!dataSolicitada) {
      toast.error('❌ Selecione uma data');
      return;
    }
    
    // ✅ Validação 2: Erro de intervalo
    if (erroData) {
      toast.error(`❌ ${erroData}`);
      return;
    }
    
    mutation.mutate();
  };

  if (!isOpen) return null;

  return (
    <>
      {/* Overlay */}
      <div
        className="fixed inset-0 bg-black/50 z-40"
        onClick={onClose}
      ></div>

      {/* Modal */}
      <div className="fixed inset-0 flex items-center justify-center z-50 p-4">
        <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6 space-y-4">
          <h2 className="text-xl font-bold text-gray-900">
            Cadastrar Última Folga
          </h2>
          
          <p className="text-gray-600">
            Colaborador: <strong>{colaborador?.nome}</strong>
          </p>

          {/* Input de Data */}
          <div className="space-y-2">
            <label className="block text-sm font-semibold text-gray-700">
              Data da Última Folga
            </label>
            <input
              type="date"
              value={dataSolicitada}
              onChange={handleDataChange}
              max={new Date().toISOString().split('T')[0]}  // ✅ Não permitir futuras
              className={`
                w-full px-4 py-2 border-2 rounded-lg transition-all
                ${erroData 
                  ? 'border-red-500 bg-red-50 focus:outline-none focus:ring-2 focus:ring-red-300' 
                  : 'border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary'
                }
              `}
            />
            
            {/* ✅ Data Mínima Permitida com Explicação */}
            {dataMinima && !erroData && (
              <div className="p-2 bg-blue-50 border border-blue-200 rounded-lg">
                <p className="text-xs text-blue-700 font-semibold">
                  📅 Data mínima permitida: {new Date(dataMinima).toLocaleDateString('pt-BR')}
                </p>
                <p className="text-xs text-blue-600 mt-1">
                  O colaborador precisa de 6 dias de trabalho antes do início da escala.
                </p>
              </div>
            )}
            
            {/* ✅ Erro com Explicação Detalhada */}
            {erroData && (
              <div className="p-3 bg-red-50 border border-red-200 rounded-lg space-y-1">
                <p className="text-xs text-red-700 font-semibold">
                  ❌ {erroData}
                </p>
                {dataMinima && (
                  <p className="text-xs text-red-600">
                    💡 Data mínima permitida: {new Date(dataMinima).toLocaleDateString('pt-BR')}
                  </p>
                )}
              </div>
            )}

            {/* ✅ Sucesso */}
            {!erroData && dataSolicitada && (
              <div className="p-2 bg-green-50 border border-green-200 rounded-lg">
                <p className="text-xs text-green-700 font-semibold">
                  ✅ Data válida - Pronto para cadastrar
                </p>
              </div>
            )}
          </div>

          {/* Botões */}
          <div className="flex gap-2 pt-4">
            <Button
              variant="secondary"
              onClick={onClose}
              className="flex-1"
            >
              Cancelar
            </Button>
            <Button
              variant="primary"
              onClick={handleSubmit}
              disabled={!dataSolicitada || erroData || mutation.isPending || validacaoMutation.isPending}
              className="flex-1"
            >
              {mutation.isPending ? 'Cadastrando...' : 'Cadastrar'}
            </Button>
          </div>
        </div>
      </div>
    </>
  );
}