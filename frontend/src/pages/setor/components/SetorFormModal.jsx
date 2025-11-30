import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Save } from 'lucide-react';
import { Button, Input, Modal } from '../../../components/common';
import { SetorConfirmModal } from './SetorConfirmModal';
import { SetorReactivateModal } from './SetorReactivateModal';

/**
 * ========================================
 * MODAL DE FORMULÁRIO - CRIAR/EDITAR SETOR
 * ========================================
 */

export function SetorFormModal({ 
  isOpen, 
  onClose, 
  onSubmit, 
  onReactivate,
  setor, 
  loading,
  error,
  showReactivate,
  reactivateData, 
}) {
  const [showConfirm, setShowConfirm] = useState(false);
  const [formData, setFormData] = useState(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    defaultValues: {
      nome: setor?.nome || '',
    },
  });

  // Resetar form quando setor mudar
  useEffect(() => {
    if (setor) {
      reset({ nome: setor.nome });
    } else {
      reset({ nome: '' });
    }
  }, [setor, reset]);

  // ✅ CORREÇÃO: Fechar modal de confirmação E limpar formData quando houver erro
  useEffect(() => {
    if (error && showConfirm) {
      setShowConfirm(false); // Fecha modal de confirmação
      // ✅ NÃO limpa formData - usuário pode corrigir e tentar novamente
    }
  }, [error, showConfirm]);

  // ✅ NOVO: Resetar estados internos quando modal principal fecha
  useEffect(() => {
    if (!isOpen) {
      setShowConfirm(false);
      setFormData(null);
    }
  }, [isOpen]);

  // Handler para quando usuário clica em "Criar" no formulário
  const handleFormSubmit = (data) => {
    if (setor) {
      // Se está editando, envia direto (sem confirmação)
      onSubmit(data);
    } else {
      // Se está criando, abre modal de confirmação
      setFormData(data);
      setShowConfirm(true);
    }
  };

  // Handler para quando usuário confirma a criação
  const handleConfirmCreate = () => {
    if (formData) {
      onSubmit(formData);
      // ✅ NÃO fecha showConfirm aqui - deixa o useEffect do error fazer isso
    }
  };

  // Handler para quando usuário cancela a confirmação
  const handleCancelConfirm = () => {
    setShowConfirm(false);
    // NÃO limpa formData - preserva os dados para voltar ao formulário
  };

  const handleClose = () => {
    reset({ nome: '' });
    setFormData(null);
    setShowConfirm(false);
    onClose();
  };

  return (
    <>
      {/* Modal do Formulário */}
      <Modal
        isOpen={isOpen && !showConfirm}
        onClose={handleClose}
        title={setor ? 'Editar Setor' : 'Novo Setor'}
        size="md"
        footer={
          <>
            <Button 
              variant="ghost" 
              onClick={handleClose} 
              disabled={loading}
            >
              Cancelar
            </Button>
            <Button
              variant="primary"
              leftIcon={Save}
              onClick={handleSubmit(handleFormSubmit)}
              loading={loading}
              disabled={loading}
            >
              {setor ? 'Atualizar' : 'Criar Setor'}
            </Button>
          </>
        }
      >
        <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-4">
          <Input
            label="Nome do Setor"
            placeholder="Ex: UTI, Emergência, Pediatria"
            error={errors.nome?.message}
            required
            autoFocus // ✅ Foco automático ao abrir
            {...register('nome', {
              required: 'Nome do setor é obrigatório',
              minLength: {
                value: 3,
                message: 'Nome deve ter no mínimo 3 caracteres',
              },
              maxLength: {
                value: 100,
                message: 'Nome deve ter no máximo 100 caracteres',
              },
            })}
          />

          <div className="bg-info-light border border-info rounded-lg p-4">
            <p className="text-sm text-gray-700">
              <strong className="text-info-dark">💡 Dica:</strong> Use nomes
              descritivos e únicos para cada setor do hospital.
            </p>
          </div>
        </form>
      </Modal>

      {/* Modal de Confirmação (só aparece ao criar E quando não há erro) */}
      {!setor && (
        <SetorConfirmModal
          isOpen={showConfirm && isOpen} // ✅ Só abre se formulário também estiver aberto
          onClose={handleCancelConfirm}
          onConfirm={handleConfirmCreate}
          setorNome={formData?.nome}
          loading={loading}
        />
      )}
      {/* ✅ NOVO: Modal de Reativação */}
      {!setor && (
        <SetorReactivateModal
          isOpen={showReactivate}
          onClose={onClose}
          onConfirm={onReactivate}
          setorNome={reactivateData?.nome}
          loading={loading}
        />
      )}
    </>
  );
}