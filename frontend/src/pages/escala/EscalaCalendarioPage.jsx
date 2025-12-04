import { useState, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { ChevronLeft, ChevronRight, Calendar } from 'lucide-react';
import { Button } from '../../components/common';
import escalaService from '../../services/escala.service';
import toast from 'react-hot-toast';

export default function EscalaCalendarioPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  // Query da escala
  const { data: escala, isLoading, error } = useQuery({
    queryKey: ['escala', id],
    queryFn: () => escalaService.buscarPorId(id),
  });

  // Gerar dias do mês
  const diasMes = useMemo(() => {
    if (!escala) return [];
    
    const ano = escala.ano;
    const mes = escala.mes;
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
  }, [escala]);

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

  if (error || !escala) {
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

  const turnoConfig = TURNO_CONFIG[escala.turno] || {};

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
            {escala.setorNome} • {turnoConfig.icon} {turnoConfig.label} • 
            {new Date(escala.ano, escala.mes - 1).toLocaleString('pt-BR', { month: 'long', year: 'numeric' })}
          </p>
        </div>

        <Button variant="secondary" onClick={() => navigate('/escalas')}>
          Voltar
        </Button>
      </div>

      {/* CARD DO CALENDÁRIO */}
      <div className="card p-8">
        {/* HEADER DO CALENDÁRIO */}
        <div className="flex items-center justify-between mb-8">
          <h2 className="text-2xl font-bold text-gray-900">
            {new Date(escala.ano, escala.mes - 1).toLocaleString('pt-BR', { month: 'long', year: 'numeric' })}
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
        <div className="grid grid-cols-7 gap-2">
          {diasMes.map((dia, index) => (
            <div
              key={index}
              className={`
                aspect-square rounded-lg border-2 p-2 flex flex-col items-center justify-center
                ${dia === null 
                  ? 'bg-gray-50 border-gray-100' 
                  : 'bg-white border-gray-200 hover:border-primary cursor-pointer transition-all'
                }
              `}
            >
              {dia && (
                <>
                  <span className="text-lg font-bold text-gray-900">{dia}</span>
                  <span className="text-xs text-gray-500 mt-1">0/3</span>
                </>
              )}
            </div>
          ))}
        </div>

        {/* LEGENDA */}
        <div className="mt-8 pt-8 border-t space-y-3">
          <h3 className="font-semibold text-gray-900">Legenda:</h3>
          <div className="grid grid-cols-3 gap-4">
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 bg-green-100 border-2 border-green-500 rounded"></div>
              <span className="text-sm text-gray-600">Disponível</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 bg-yellow-100 border-2 border-yellow-500 rounded"></div>
              <span className="text-sm text-gray-600">Parcial</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 bg-red-100 border-2 border-red-500 rounded"></div>
              <span className="text-sm text-gray-600">Completo</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}