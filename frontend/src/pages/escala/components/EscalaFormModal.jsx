import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery } from '@tanstack/react-query';
import { X } from 'lucide-react';
import { Modal, Input, Select, Button } from '../../../components/common';
import escalaService from '../../../services/escala.service';
import setorService from '../../../services/setor.service';
import toast from 'react-hot-toast';

export function EscalaFormModal({ isOpen, onClose, escala, onSuccess }) {
  const isEditing = !!escala;

  const { register, handleSubmit, formState: { errors }, reset, setValue } = useForm({
    defaultValues: {
      mes: '',
      ano: new Date().getFullYear(),
      folgasPermitidas: 5,
      turno: '',
      setorId: '',
    }
  });

  const { data: setoresData } = useQuery({
    queryKey: ['setores-all'],
    queryFn: () => setorService.listar({ page: 0, size: 100 }),
    enabled: isOpen,
  });

  useEffect(() => {
    if (escala) {
      setValue('mes', escala.mes);
      setValue('ano', escala.ano);
      setValue('folgasPermitidas', escala.folgasPermitidas);
      setValue('turno', escala.turno);
      setValue('setorId', escala.setorId);
    } else {
      reset({
        mes: '',
        ano: new Date().getFullYear(),
        folgasPermitidas: 5,
        turno: '',
        setorId: '',
      });
    }
  }, [escala, setValue, reset]);

  const mutation = useMutation({
  mutationFn: (data) => {
    if (isEditing) {
      return escalaService.atualizar(escala.id, data);
    }
    return escalaService.criar(data);
  },
  onSuccess: (data) => {  // ✅ Recebe a resposta da API
    toast.success(isEditing ? 'Escala atualizada com sucesso!' : 'Escala criada com sucesso!');
    onSuccess(data.id);  // ✅ Passa o ID para o parent
    reset();
  },
  onError: (error) => {
    const message = error.response?.data?.message || 'Erro ao salvar escala';
    toast.error(message);
  },
});

  const onSubmit = (data) => {
    mutation.mutate({
      ...data,
      mes: parseInt(data.mes),
      ano: parseInt(data.ano),
      folgasPermitidas: parseInt(data.folgasPermitidas),
      setorId: parseInt(data.setorId),
    });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={isEditing ? 'Editar Escala' : 'Nova Escala'}>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <Select
            label="Mês"
            required
            error={errors.mes?.message}
            {...register('mes', { required: 'Mês é obrigatório' })}
          >
            <option value="">Selecione...</option>
            {[...Array(12)].map((_, i) => (
              <option key={i + 1} value={i + 1}>
                {new Date(2000, i).toLocaleString('pt-BR', { month: 'long' })}
              </option>
            ))}
          </Select>

          <Input
            label="Ano"
            type="number"
            required
            min="2020"
            max="2030"
            error={errors.ano?.message}
            {...register('ano', { 
              required: 'Ano é obrigatório',
              min: { value: 2020, message: 'Ano mínimo: 2020' },
              max: { value: 2030, message: 'Ano máximo: 2030' }
            })}
          />
        </div>

        <Select
          label="Setor"
          required
          error={errors.setorId?.message}
          {...register('setorId', { required: 'Setor é obrigatório' })}
        >
          <option value="">Selecione...</option>
          {setoresData?.content?.map((setor) => (
            <option key={setor.id} value={setor.id}>
              {setor.nome}
            </option>
          ))}
        </Select>

        <Select
          label="Turno"
          required
          error={errors.turno?.message}
          {...register('turno', { required: 'Turno é obrigatório' })}
        >
          <option value="">Selecione...</option>
          <option value="MANHA">🌅 Manhã</option>
          <option value="TARDE">☀️ Tarde</option>
          <option value="NOITE">🌙 Noite</option>
        </Select>

        <Input
          label="Folgas Permitidas"
          type="number"
          required
          min="1"
          max="10"
          error={errors.folgasPermitidas?.message}
          {...register('folgasPermitidas', { 
            required: 'Folgas permitidas é obrigatório',
            min: { value: 1, message: 'Mínimo: 1 folga' },
            max: { value: 10, message: 'Máximo: 10 folgas' }
          })}
        />

        <div className="flex gap-3 pt-4">
          <Button
            type="button"
            variant="secondary"
            onClick={onClose}
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
            {isEditing ? 'Atualizar' : 'Cadastrar'}
          </Button>
        </div>
      </form>
    </Modal>
  );
}