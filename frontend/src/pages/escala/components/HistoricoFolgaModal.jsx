import { AlertTriangle, Users, Calendar } from 'lucide-react';
import { Modal, Button } from '../../../components/common';

const TURNO_CONFIG = {
  MANHA: { icon: '🌅', label: 'Manhã' },
  TARDE: { icon: '☀️', label: 'Tarde' },
  NOITE: { icon: '🌙', label: 'Noite' },
};

const CARGO_CONFIG = {
  ENFERMEIRO: { label: 'Enfermeiro(a)', color: 'text-blue-700 bg-blue-50' },
  TECNICO: { label: 'Técnico(a)', color: 'text-green-700 bg-green-50' },
};

export function HistoricoFolgaModal({ isOpen, onClose, dados, onSelecionarColaborador }) {
  if (!dados) return null;

  const { colaboradoresSemHistorico, totalSemHistorico, totalColaboradores } = dados;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="⚠️ Histórico de Folgas Incompleto">
      <div className="space-y-4">
        {/* AVISO */}
        <div className="flex items-start gap-3 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
          <AlertTriangle className="w-6 h-6 text-yellow-600 flex-shrink-0 mt-0.5" />
          <div>
            <h4 className="font-semibold text-yellow-900 mb-1">
              Atenção: Colaboradores sem histórico de folgas
            </h4>
            <p className="text-sm text-yellow-800">
              Clique em um colaborador abaixo para cadastrar sua última folga.
            </p>
          </div>
        </div>

        {/* ESTATÍSTICAS */}
        <div className="grid grid-cols-2 gap-4">
          <div className="p-4 bg-gray-50 rounded-lg border border-gray-200">
            <div className="flex items-center gap-2 mb-1">
              <Users className="w-4 h-4 text-gray-600" />
              <p className="text-xs text-gray-600 font-medium">Total de Colaboradores</p>
            </div>
            <p className="text-2xl font-bold text-gray-900">{totalColaboradores}</p>
          </div>

          <div className="p-4 bg-yellow-50 rounded-lg border border-yellow-200">
            <div className="flex items-center gap-2 mb-1">
              <AlertTriangle className="w-4 h-4 text-yellow-600" />
              <p className="text-xs text-yellow-700 font-medium">Sem Histórico</p>
            </div>
            <p className="text-2xl font-bold text-yellow-900">{totalSemHistorico}</p>
          </div>
        </div>

        {/* LISTA DE COLABORADORES */}
        <div>
          <h4 className="font-semibold text-gray-900 mb-3 flex items-center gap-2">
            <Calendar className="w-4 h-4" />
            Clique em um colaborador para cadastrar o histórico:
          </h4>

          {colaboradoresSemHistorico.length === 0 ? (
            <div className="p-8 text-center border border-gray-200 rounded-lg bg-green-50">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <Calendar className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-lg font-semibold text-green-900 mb-1">
                ✅ Todos os históricos cadastrados!
              </h3>
              <p className="text-sm text-green-700">
                Você pode continuar para o calendário.
              </p>
            </div>
          ) : (
            <div className="max-h-64 overflow-y-auto space-y-2 border border-gray-200 rounded-lg p-3 bg-gray-50">
              {colaboradoresSemHistorico.map((colaborador) => {
                const turnoConfig = TURNO_CONFIG[colaborador.turno] || {};
                const cargoConfig = CARGO_CONFIG[colaborador.cargo] || {};

                return (
                  <button
                    key={colaborador.id}
                    onClick={() => onSelecionarColaborador(colaborador)}
                    className="w-full flex items-center justify-between p-3 bg-white rounded-lg border-2 border-gray-200 hover:border-primary hover:bg-primary/5 transition-all cursor-pointer group"
                  >
                    <div className="flex-1 text-left">
                      <p className="font-semibold text-gray-900 group-hover:text-primary transition-colors">
                        {colaborador.nome}
                      </p>
                      <div className="flex items-center gap-2 mt-1">
                        <span className={`
                          inline-flex items-center gap-1 px-2 py-0.5 text-xs font-medium rounded-full
                          ${cargoConfig.color}
                        `}>
                          {cargoConfig.label}
                        </span>
                        <span className="text-xs text-gray-500">
                          {turnoConfig.icon} {turnoConfig.label}
                        </span>
                      </div>
                    </div>
                    
                    <div className="flex items-center gap-2 text-sm text-gray-500 group-hover:text-primary">
                      <Calendar className="w-4 h-4" />
                      <span className="font-medium">Cadastrar</span>
                    </div>
                  </button>
                );
              })}
            </div>
          )}
        </div>

        {/* AÇÕES */}
        <div className="flex gap-3 pt-4 border-t">
          <Button
            type="button"
            variant="secondary"
            onClick={onClose}
            className="flex-1"
          >
            Fechar
          </Button>
          
          {colaboradoresSemHistorico.length === 0 && (
            <Button
              type="button"
              variant="primary"
              onClick={onClose}
              className="flex-1"
            >
              Ir para Calendário
            </Button>
          )}
        </div>
      </div>
    </Modal>
  );
}