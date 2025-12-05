import { useState, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { ChevronLeft, ChevronRight, Calendar, Users } from 'lucide-react';
import { Button } from '../../components/common';
import escalaService from '../../services/escala.service';
import toast from 'react-hot-toast';

/**
 * ✅ Converte string YYYY-MM-DD para Date sem perder 1 dia por timezone
 */
function parseDataSegura(dataString) {
  if (!dataString) return null;
  
  // Se já é Date, retorna
  if (dataString instanceof Date) return dataString;
  
  // Divide a string YYYY-MM-DD
  const [ano, mes, dia] = dataString.split('-').map(Number);
  
  // Cria Date com UTC para evitar timezone
  return new Date(Date.UTC(ano, mes - 1, dia));
}

/**
 * ✅ Formata data para pt-BR sem perder 1 dia
 */
function formatarDataSegura(dataString) {
  if (!dataString) return '';
  
  const data = parseDataSegura(dataString);
  return data.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
}

export default function EscalaCalendarioPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [colaboradorSelecionado, setColaboradorSelecionado] = useState(null);

  // Query do calendário com colaboradores
  const { data: calendario, isLoading, error } = useQuery({
    queryKey: ['calendario', id],
    queryFn: () => escalaService.buscarComColaboradores(id),
  });

  // Calcular dias disponíveis para folga do colaborador selecionado
 const diasDisponiveis = useMemo(() => {
  if (!colaboradorSelecionado || !calendario) return [];

  // Se não tem última folga, pode folgar em qualquer dia
  if (!colaboradorSelecionado.ultimaFolga) {
    const diasNoMes = new Date(calendario.ano, calendario.mes, 0).getDate();
    return Array.from({ length: diasNoMes }, (_, i) => i + 1);
  }

  // ✅ LÓGICA CORRIGIDA COM parseDataSegura
  const DIAS_TRABALHO_MAXIMO = calendario.configuracao?.diasTrabalhoMaximo || 6;
  
  // ✅ Usar parseDataSegura ao invés de new Date()
  const dataUltima = parseDataSegura(colaboradorSelecionado.ultimaFolga);
  
  // ✅ CRÍTICO: Próxima folga = última folga + dias trabalho + 1
  const dataProxima = new Date(dataUltima);
  dataProxima.setDate(dataProxima.getDate() + 1 + DIAS_TRABALHO_MAXIMO + 1);

  console.log('🔍 [DEBUG DIAS DISPONÍVEIS]');
  console.log('   Colaborador:', colaboradorSelecionado.nome);
  console.log('   Última folga (string):', colaboradorSelecionado.ultimaFolga);
  console.log('   Última folga (parseada):', dataUltima.toLocaleDateString('pt-BR', { timeZone: 'UTC' }));
  console.log('   Dias trabalho máximo:', DIAS_TRABALHO_MAXIMO);
  console.log('   Próxima folga (data):', dataProxima.toLocaleDateString('pt-BR', { timeZone: 'UTC' }));
  console.log('   Calendário:', `${calendario.mes}/${calendario.ano}`);

  // Dias disponíveis: do dia após a última folga até o dia máximo permitido
  const diasDisp = [];
  
  // Começar do dia após a última folga
  const dataInicio = new Date(dataUltima);
  dataInicio.setDate(dataInicio.getDate() + 1);
  
  console.log('   Data início (primeiro dia disponível):', dataInicio.toLocaleDateString('pt-BR', { timeZone: 'UTC' }));

  // Enquanto não ultrapassar o dia máximo permitido
  while (dataInicio <= dataProxima) {
    const mesAtual = dataInicio.getMonth() + 1;
    const anoAtual = dataInicio.getFullYear();
    const diaAtual = dataInicio.getDate();

    console.log(`   Verificando: ${diaAtual}/${mesAtual}/${anoAtual} vs calendário ${calendario.mes}/${calendario.ano}`);

    // ✅ CORRIGIDO: Verificar se está no mês/ano do calendário
    if (mesAtual === calendario.mes && anoAtual === calendario.ano) {
      diasDisp.push(diaAtual);
      console.log(`   ✅ Dia disponível: ${diaAtual}`);
    } else {
      console.log(`   ⚠️ Dia fora do mês: ${diaAtual}/${mesAtual}/${anoAtual}`);
    }
    
    // Próximo dia
    dataInicio.setDate(dataInicio.getDate() + 1);
  }

  console.log('   Dias disponíveis finais:', diasDisp);

  return diasDisp;
}, [colaboradorSelecionado, calendario]);

  // Gerar grid de dias do mês
  const diasMes = useMemo(() => {
    if (!calendario) return [];
    
    const ano = calendario.ano;
    const mes = calendario.mes;
    const primeiroDia = new Date(ano, mes - 1, 1);
    const ultimoDia = new Date(ano, mes, 0);
    const diasNoMes = ultimoDia.getDate();
    const diaSemanaPrimeiro = primeiroDia.getDay();

    const dias = [];
    
    // Dias vazios do mês anterior
    for (let i = 0; i < diaSemanaPrimeiro; i++) {
      dias.push(null);
    }
    
    // Dias do mês
    for (let i = 1; i <= diasNoMes; i++) {
      dias.push(i);
    }
    
    return dias;
  }, [calendario]);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-center">
          <Calendar className="w-12 h-12 text-primary mx-auto mb-4 animate-spin" />
          <p className="text-gray-600">Carregando calendário...</p>
        </div>
      </div>
    );
  }

  if (error || !calendario) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-center">
          <p className="text-red-600 mb-4">Erro ao carregar escala</p>
          <Button onClick={() => navigate('/escalas')}>Voltar</Button>
        </div>
      </div>
    );
  }

  const TURNO_CONFIG = {
    MANHA: { icon: '🌅', label: 'Manhã' },
    TARDE: { icon: '☀️', label: 'Tarde' },
    NOITE: { icon: '🌙', label: 'Noite' },
  };

  const turnoConfig = TURNO_CONFIG[calendario.turno] || {};

  return (
    <div className="space-y-6">
      {/* HEADER */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
            <Calendar className="w-8 h-8 text-primary" />
            Calendário de Folgas
          </h1>
          <p className="text-gray-600 mt-1">
            {calendario.setorNome} • {turnoConfig.icon} {turnoConfig.label} • 
            {calendario.mesNome}/{calendario.ano}
          </p>
        </div>

        <Button variant="secondary" onClick={() => navigate('/escalas')}>
          Voltar
        </Button>
      </div>

      {/* CONTAINER PRINCIPAL */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        
        {/* CALENDÁRIO */}
        <div className="lg:col-span-3 card p-8">
          {/* HEADER DO CALENDÁRIO */}
          <div className="flex items-center justify-between mb-8">
            <h2 className="text-2xl font-bold text-gray-900">
              {calendario.mesNome} / {calendario.ano}
            </h2>
            
            <div className="flex gap-2">
              <Button variant="secondary" size="sm" leftIcon={ChevronLeft}>
                Anterior
              </Button>
              <Button variant="secondary" size="sm" rightIcon={ChevronRight}>
                Próximo
              </Button>
            </div>
          </div>

          {/* DIAS DA SEMANA */}
          <div className="grid grid-cols-7 gap-2 mb-4">
            {['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab'].map((dia) => (
              <div key={dia} className="text-center font-semibold text-gray-600 py-2">
                {dia}
              </div>
            ))}
          </div>

          {/* DIAS DO MÊS */}
          <div className="grid grid-cols-7 gap-2 mb-8">
            {diasMes.map((dia, index) => {
              const podefolgar = dia && diasDisponiveis.includes(dia);
              
              return (
                <div
                  key={index}
                  className={`
                    aspect-square rounded-lg border-2 p-2 flex flex-col items-center justify-center
                    transition-all
                    ${dia === null 
                      ? 'bg-gray-50 border-gray-100' 
                      : podefolgar
                        ? 'bg-green-100 border-green-500 hover:shadow-lg hover:scale-105 cursor-pointer'
                        : 'bg-red-100 border-red-300 opacity-50'
                    }
                  `}
                >
                  {dia && (
                    <>
                      <span className="text-lg font-bold text-gray-900">{dia}</span>
                      <span className="text-xs text-gray-600 mt-1">
                        {podefolgar ? '✓' : '✗'}
                      </span>
                    </>
                  )}
                </div>
              );
            })}
          </div>

          {/* LEGENDA */}
          <div className="pt-8 border-t space-y-3">
            <h3 className="font-semibold text-gray-900">Legenda:</h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 bg-green-100 border-2 border-green-500 rounded"></div>
                <span className="text-sm text-gray-600">Pode folgar</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 bg-red-100 border-2 border-red-300 rounded"></div>
                <span className="text-sm text-gray-600">Não pode folgar</span>
              </div>
            </div>
          </div>
        </div>

        {/* LISTA DE COLABORADORES */}
        <div className="card p-6 h-fit sticky top-24">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
            <Users className="w-5 h-5 text-primary" />
            Colaboradores
          </h3>

          {/* LISTA */}
          <div className="space-y-2 max-h-96 overflow-y-auto">
            {calendario.colaboradores && calendario.colaboradores.length > 0 ? (
              calendario.colaboradores.map((colaborador) => (
                <button
                  key={colaborador.id}
                  onClick={() => setColaboradorSelecionado(colaborador)}
                  className={`
                    w-full text-left px-4 py-3 rounded-lg border-2 transition-all
                    ${colaboradorSelecionado?.id === colaborador.id
                      ? 'bg-primary text-white border-primary shadow-lg'
                      : 'bg-white text-gray-900 border-gray-200 hover:border-primary hover:bg-primary-light'
                    }
                  `}
                >
                  <p className="font-semibold text-sm">{colaborador.nome}</p>
                  <p className="text-xs opacity-75 mt-1">
                    {colaborador.turno}
                  </p>
                  {colaborador.ultimaFolga && (
                    <p className="text-xs opacity-60 mt-1">
                      {/* ✅ CORRIGIDO: Usar formatarDataSegura */}
                      Última: {formatarDataSegura(colaborador.ultimaFolga)}
                    </p>
                  )}
                </button>
              ))
            ) : (
              <p className="text-sm text-gray-500 text-center py-4">
                Nenhum colaborador encontrado
              </p>
            )}
          </div>

          {/* INFO DO COLABORADOR SELECIONADO */}
          {colaboradorSelecionado && (
            <div className="mt-6 pt-6 border-t space-y-3">
              <div className="p-3 bg-blue-50 rounded-lg border border-blue-200">
                <p className="text-xs text-blue-600 font-semibold mb-1">Selecionado</p>
                <p className="text-sm font-bold text-blue-900">{colaboradorSelecionado.nome}</p>
              </div>

              {colaboradorSelecionado.ultimaFolga ? (
                <>
                  <div className="p-3 bg-green-50 rounded-lg border border-green-200">
                    <p className="text-xs text-green-600 font-semibold mb-1">Última Folga</p>
                    {/* ✅ CORRIGIDO: Usar formatarDataSegura */}
                    <p className="text-sm font-bold text-green-900">
                      {formatarDataSegura(colaboradorSelecionado.ultimaFolga)}
                    </p>
                  </div>

                  <div className="p-3 bg-yellow-50 rounded-lg border border-yellow-200">
                    <p className="text-xs text-yellow-600 font-semibold mb-1">Data Limite</p>
                    <p className="text-sm font-bold text-yellow-900">
                      Até {diasDisponiveis.length > 0 
                        ? `${diasDisponiveis[diasDisponiveis.length - 1]} de ${calendario.mesNome}`
                        : 'Próximo mês'
                      }
                    </p>
                  </div>

                  <div className="p-3 bg-purple-50 rounded-lg border border-purple-200">
                    <p className="text-xs text-purple-600 font-semibold mb-1">Dias Disponíveis</p>
                    <p className="text-sm font-bold text-purple-900">
                      {diasDisponiveis.length} dias
                    </p>
                  </div>
                </>
              ) : (
                <div className="p-3 bg-orange-50 rounded-lg border border-orange-200">
                  <p className="text-xs text-orange-600 font-semibold mb-1">⚠️ Sem Histórico</p>
                  <p className="text-sm font-bold text-orange-900">
                    Pode folgar em qualquer dia do mês
                  </p>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}