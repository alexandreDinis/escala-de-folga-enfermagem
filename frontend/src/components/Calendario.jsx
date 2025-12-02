import { useState, useEffect } from 'react';
import { getCalendario } from '../services/escala.service';
import { criarFolga, deletarFolga } from '../services/folga.service';
import { 
  Calendar, 
  Loader2, 
  AlertCircle, 
  Users,
  TrendingUp,
  Clock,
  X,
  Check,
  AlertTriangle
} from 'lucide-react';

/**
 * ========================================
 * CALENDÁRIO PREMIUM - REDE D'OR
 * ========================================
 * 
 * Componente de visualização e criação de folgas
 * com design institucional hospitalar
 */

const Calendario = ({ escalaId }) => {
  const [calendario, setCalendario] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Estados do modal
  const [showModal, setShowModal] = useState(false);
  const [selectedDia, setSelectedDia] = useState(null);
  const [selectedColaborador, setSelectedColaborador] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    carregarCalendario();
  }, [escalaId]);

  const carregarCalendario = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('🔄 Carregando calendário da escala:', escalaId);
      const data = await getCalendario(escalaId);
      console.log('✅ Calendário carregado:', data);
      setCalendario(data);
    } catch (err) {
      console.error('❌ Erro ao carregar:', err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleClickDia = (dia) => {
    if (!dia.clicavel) return;
    
    setSelectedDia(dia);
    setSelectedColaborador(null);
    setShowModal(true);
  };

  const handleCriarFolga = async () => {
    if (!selectedColaborador) {
      alert('❌ Selecione um colaborador');
      return;
    }

    try {
      setSubmitting(true);
      console.log('🔄 Criando folga:', { 
        colaborador: selectedColaborador.nome, 
        data: selectedDia.data 
      });
      
      const response = await criarFolga({
        escalaId: escalaId,
        colaboradorId: selectedColaborador.id,
        dataSolicitada: selectedDia.data,
        justificativa: ''
      });

      // Exibir alertas se houver
      if (response.alertas && response.alertas.length > 0) {
        const mensagensAlertas = response.alertas
          .map(a => `${a.icone} ${a.titulo}:\n${a.mensagem}`)
          .join('\n\n');
        
        const confirmar = window.confirm(
          `⚠️ FOLGA CRIADA COM ALERTAS:\n\n${mensagensAlertas}\n\nDeseja continuar?`
        );
        
        if (!confirmar) {
          await deletarFolga(response.id);
          await carregarCalendario();
          setShowModal(false);
          return;
        }
      } else {
        alert('✅ Folga criada com sucesso!');
      }

      await carregarCalendario();
      setShowModal(false);
      setSelectedColaborador(null);
      
    } catch (err) {
      console.error('❌ Erro ao criar folga:', err);
      alert(`❌ ERRO DE VALIDAÇÃO:\n\n${err.message}\n\n💡 AÇÃO NECESSÁRIA:\n\nCadastre a última folga de cada colaborador antes de criar a primeira escala.`);
    } finally {
      setSubmitting(false);
    }
  };

  const getCorDia = (status) => {
    const cores = {
      'ABERTO': 'bg-success-light border-success hover:shadow-lg hover:scale-105',
      'DOMINGO': 'bg-info-light border-info hover:shadow-lg hover:scale-105',
      'ALERTA': 'bg-warning-light border-warning hover:shadow-lg hover:scale-105',
      'OCUPADO': 'bg-error-light border-error cursor-not-allowed',
      'PASSADO': 'bg-gray-100 border-gray-300 opacity-50 cursor-not-allowed'
    };
    return cores[status] || 'bg-white border-gray-300';
  };

  // ========================================
  // LOADING STATE
  // ========================================
  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[600px]">
        <div className="text-center">
          <div className="spinner spinner-lg mb-6"></div>
          <h3 className="text-xl font-semibold text-gray-900 mb-2">
            Carregando calendário
          </h3>
          <p className="text-gray-500">
            Aguarde enquanto buscamos os dados...
          </p>
        </div>
      </div>
    );
  }

  // ========================================
  // ERROR STATE
  // ========================================
  if (error) {
    return (
      <div className="flex items-center justify-center min-h-[600px] p-4">
        <div className="card max-w-md w-full">
          <div className="card-body text-center">
            <div className="w-16 h-16 bg-error-light rounded-full flex items-center justify-center mx-auto mb-4">
              <AlertCircle className="w-8 h-8 text-error" />
            </div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">
              Erro ao Carregar
            </h2>
            <p className="text-gray-600 mb-6">{error}</p>
            <button onClick={carregarCalendario} className="btn btn-primary w-full">
              Tentar Novamente
            </button>
          </div>
        </div>
      </div>
    );
  }

  // ========================================
  // MAIN CONTENT
  // ========================================
  return (
    <div className="space-y-6">
      {/* ========================================
          HEADER COM ESTATÍSTICAS
          ======================================== */}
      <div className="card overflow-hidden">
        <div className="bg-gradient-to-r from-primary to-primary-dark text-white p-8">
          <div className="flex items-center gap-4 mb-6">
            <div className="w-14 h-14 bg-white/20 rounded-xl flex items-center justify-center">
              <Calendar className="w-8 h-8" />
            </div>
            <div>
              <h1 className="text-3xl font-bold mb-1">
                Dezembro {calendario.ano}
              </h1>
              <p className="text-primary-light text-lg">
                {calendario.turno} • {calendario.setorNome}
              </p>
            </div>
          </div>

          {/* Estatísticas */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/20">
              <div className="flex items-center gap-3 mb-2">
                <Check className="w-5 h-5 text-white" />
                <p className="text-sm text-white/80 font-medium">Folgas Alocadas</p>
              </div>
              <p className="text-3xl font-bold">{calendario.resumo.totalFolgasAlocadas}</p>
            </div>

            <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/20">
              <div className="flex items-center gap-3 mb-2">
                <Clock className="w-5 h-5 text-white" />
                <p className="text-sm text-white/80 font-medium">Disponíveis</p>
              </div>
              <p className="text-3xl font-bold">{calendario.resumo.totalFolgasDisponiveis}</p>
            </div>

            <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4 border border-white/20">
              <div className="flex items-center gap-3 mb-2">
                <TrendingUp className="w-5 h-5 text-white" />
                <p className="text-sm text-white/80 font-medium">Ocupação</p>
              </div>
              <p className="text-3xl font-bold">{calendario.resumo.percentualOcupacao}%</p>
            </div>
          </div>
        </div>
      </div>

      {/* ========================================
          CALENDÁRIO
          ======================================== */}
      <div className="card">
        <div className="card-body">
          {/* Dica */}
          <div className="bg-info-light border border-info rounded-lg p-4 mb-6">
            <div className="flex items-start gap-3">
              <AlertTriangle className="w-5 h-5 text-info flex-shrink-0 mt-0.5" />
              <div>
                <p className="font-semibold text-info-dark mb-1">Dica de Uso</p>
                <p className="text-sm text-gray-700">
                  Clique em um dia <span className="font-semibold">verde (aberto)</span> ou{' '}
                  <span className="font-semibold">azul (domingo)</span> para criar uma folga
                </p>
              </div>
            </div>
          </div>

          {/* Dias da semana */}
          <div className="grid grid-cols-7 gap-2 mb-3">
            {['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'].map(dia => (
              <div key={dia} className="text-center font-bold text-gray-700 text-sm py-3">
                {dia}
              </div>
            ))}
          </div>

          {/* Grid de dias */}
          <div className="grid grid-cols-7 gap-2">
            {calendario.dias.map((dia) => (
              <button
                key={dia.dia}
                onClick={() => handleClickDia(dia)}
                disabled={!dia.clicavel}
                className={`
                  ${getCorDia(dia.status)}
                  border-2 rounded-xl p-3 min-h-[100px] text-left 
                  transition-all duration-200
                  ${dia.clicavel ? 'cursor-pointer' : ''}
                `}
              >
                <div className="font-bold text-xl text-gray-900 mb-2">{dia.dia}</div>
                
                {dia.totalFolgasNoDia > 0 && (
                  <div className="text-xs text-gray-600 font-medium mb-2">
                    📋 {dia.totalFolgasNoDia} folga(s)
                  </div>
                )}

                {dia.folgasNoDia.slice(0, 2).map(folga => (
                  <div
                    key={folga.folgaId}
                    className="text-xs bg-primary text-white px-2 py-1 rounded-lg mb-1 truncate font-medium"
                  >
                    {folga.colaboradorNome.split(' ')[0]}
                  </div>
                ))}

                {dia.folgasNoDia.length > 2 && (
                  <div className="text-xs text-gray-500 font-semibold mt-1">
                    +{dia.folgasNoDia.length - 2} mais
                  </div>
                )}
              </button>
            ))}
          </div>

          {/* Legenda */}
          <div className="flex flex-wrap gap-4 mt-6 pt-6 border-t border-gray-200">
            <div className="flex items-center gap-2">
              <div className="w-5 h-5 rounded-md bg-success border-2 border-success"></div>
              <span className="text-sm font-medium text-gray-700">Aberto</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-5 h-5 rounded-md bg-info border-2 border-info"></div>
              <span className="text-sm font-medium text-gray-700">Domingo</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-5 h-5 rounded-md bg-warning border-2 border-warning"></div>
              <span className="text-sm font-medium text-gray-700">Alerta</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-5 h-5 rounded-md bg-error border-2 border-error"></div>
              <span className="text-sm font-medium text-gray-700">Ocupado</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-5 h-5 rounded-md bg-gray-300 border-2 border-gray-400"></div>
              <span className="text-sm font-medium text-gray-700">Passado</span>
            </div>
          </div>
        </div>
      </div>

      {/* ========================================
          LISTA DE COLABORADORES
          ======================================== */}
      <div className="card">
        <div className="card-header">
          <div className="flex items-center gap-3">
            <Users className="w-6 h-6 text-primary" />
            <h2 className="text-xl font-bold text-gray-900">
              Colaboradores ({calendario.colaboradores.length})
            </h2>
          </div>
        </div>
        <div className="card-body">
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {calendario.colaboradores.map(colab => (
              <div
                key={colab.id}
                className="border-2 border-gray-200 rounded-xl p-4 hover:border-primary hover:shadow-lg transition-all duration-200"
              >
                <p className="font-bold text-gray-900 mb-2 text-lg">{colab.nome}</p>
                <div className="flex items-center gap-2 mb-1">
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-primary h-2 rounded-full transition-all duration-300"
                      style={{ width: `${(colab.totalFolgasNoMes / (colab.totalFolgasNoMes + colab.folgasRestantes)) * 100}%` }}
                    ></div>
                  </div>
                </div>
                <p className="text-sm text-gray-600 mb-2">
                  <span className="font-semibold">{colab.totalFolgasNoMes}</span>/
                  {colab.totalFolgasNoMes + colab.folgasRestantes} folgas
                </p>
                <span className={`badge ${colab.podeFolgarHoje ? 'badge-success' : 'badge-warning'}`}>
                  {colab.podeFolgarHoje ? '✅ Disponível' : '⏳ Aguardando'}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ========================================
          MODAL DE SELEÇÃO DE COLABORADOR
          ======================================== */}
      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3 className="text-xl font-bold text-gray-900">
                Criar Folga - Dia {selectedDia.dia}
              </h3>
              <button
                onClick={() => setShowModal(false)}
                className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <X className="w-5 h-5 text-gray-500" />
              </button>
            </div>

            <div className="modal-body">
              <p className="text-sm text-gray-600 mb-4">
                Selecione o colaborador que folgará neste dia:
              </p>

              <div className="space-y-2 max-h-96 overflow-y-auto scrollbar-premium">
                {calendario.colaboradores.map(colab => (
                  <button
                    key={colab.id}
                    onClick={() => setSelectedColaborador(colab)}
                    className={`
                      w-full text-left p-4 rounded-xl border-2 transition-all
                      ${selectedColaborador?.id === colab.id
                        ? 'border-primary bg-primary-light'
                        : 'border-gray-200 hover:border-primary hover:bg-gray-50'
                      }
                    `}
                  >
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="font-semibold text-gray-900">{colab.nome}</p>
                        <p className="text-sm text-gray-600">
                          {colab.totalFolgasNoMes}/{colab.totalFolgasNoMes + colab.folgasRestantes} folgas
                        </p>
                      </div>
                      {selectedColaborador?.id === colab.id && (
                        <Check className="w-6 h-6 text-primary" />
                      )}
                    </div>
                  </button>
                ))}
              </div>
            </div>

            <div className="modal-footer">
              <button
                onClick={() => setShowModal(false)}
                className="btn btn-ghost"
                disabled={submitting}
              >
                Cancelar
              </button>
              <button
                onClick={handleCriarFolga}
                className="btn btn-primary"
                disabled={!selectedColaborador || submitting}
              >
                {submitting ? (
                  <>
                    <div className="spinner"></div>
                    Criando...
                  </>
                ) : (
                  <>
                    <Check className="w-5 h-5" />
                    Confirmar Folga
                  </>
                )}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Calendario;
