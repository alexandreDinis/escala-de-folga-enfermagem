import { useEffect, useState } from 'react';
import { X, User, Briefcase, Clock, Building2 } from 'lucide-react';
import { Modal, Button, Input, Select } from '../../../components/common';
import { useQuery } from '@tanstack/react-query';
import setorService from '../../../services/setor.service';

/**
 * ========================================
 * MODAL DE FORMULÁRIO - COLABORADOR
 * ========================================
 */

export function ColaboradorFormModal({ 
  isOpen, 
  onClose, 
  onSubmit, 
  colaborador, 
  loading 
}) {
  const [formData, setFormData] = useState({
    nome: '',
    cargo: '',
    turno: '',
    setorId: '',
  });

  const [errors, setErrors] = useState({});

  // Query: Buscar setores para o select
  const { data: setoresData } = useQuery({
    queryKey: ['setores-select'],
    queryFn: () => setorService.listar({ page: 0, size: 100, search: '' }),
    enabled: isOpen, // Só busca quando modal está aberto
  });

  // Preencher formulário ao editar
  useEffect(() => {
    if (colaborador) {
      setFormData({
        nome: colaborador.nome || '',
        cargo: colaborador.cargo || '',
        turno: colaborador.turno || '',
        setorId: colaborador.setorId || '',
      });
    } else {
      setFormData({
        nome: '',
        cargo: '',
        turno: '',
        setorId: '',
      });
    }
    setErrors({});
  }, [colaborador, isOpen]);

  // Handler de mudança
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    
    // Limpar erro do campo
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  // Validação
  const validate = () => {
    const newErrors = {};

    if (!formData.nome.trim()) {
      newErrors.nome = 'Nome é obrigatório';
    }

    if (!formData.cargo) {
      newErrors.cargo = 'Cargo é obrigatório';
    }

    if (!formData.turno) {
      newErrors.turno = 'Turno é obrigatório';
    }

    if (!formData.setorId) {
      newErrors.setorId = 'Setor é obrigatório';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handler de submit
  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!validate()) {
      return;
    }

    onSubmit(formData);
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="md">
      <div className="p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-primary-light rounded-lg flex items-center justify-center">
              <User className="w-5 h-5 text-primary" />
            </div>
            <div>
              <h2 className="text-xl font-bold text-gray-900">
                {colaborador ? 'Editar Colaborador' : 'Novo Colaborador'}
              </h2>
              <p className="text-sm text-gray-500">
                {colaborador ? 'Atualize as informações do colaborador' : 'Preencha os dados do novo colaborador'}
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Nome */}
          <Input
            label="Nome Completo"
            name="nome"
            value={formData.nome}
            onChange={handleChange}
            placeholder="Ex: Maria Silva Santos"
            leftIcon={User}
            error={errors.nome}
            required
          />

          {/* Cargo */}
          <Select
            label="Cargo"
            name="cargo"
            value={formData.cargo}
            onChange={handleChange}
            leftIcon={Briefcase}
            error={errors.cargo}
            required
          >
            <option value="">Selecione um cargo</option>
            <option value="ENFERMEIRO">🩺 Enfermeiro</option>
            <option value="TECNICO">🔧 Técnico de Enfermagem</option>
          </Select>

          {/* Turno */}
          <Select
            label="Turno"
            name="turno"
            value={formData.turno}
            onChange={handleChange}
            leftIcon={Clock}
            error={errors.turno}
            required
          >
            <option value="">Selecione um turno</option>
            <option value="MANHA">🌅 Manhã</option>
            <option value="TARDE">☀️ Tarde</option>
            <option value="NOITE">🌙 Noite</option>
          </Select>

          {/* Setor */}
          <Select
            label="Setor"
            name="setorId"
            value={formData.setorId}
            onChange={handleChange}
            leftIcon={Building2}
            error={errors.setorId}
            required
          >
            <option value="">Selecione um setor</option>
            {setoresData?.content?.map(setor => (
              <option key={setor.id} value={setor.id}>
                {setor.nome}
              </option>
            ))}
          </Select>

          {/* Botões */}
          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              className="flex-1"
              disabled={loading}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              variant="primary"
              className="flex-1"
              loading={loading}
            >
              {colaborador ? 'Atualizar' : 'Cadastrar'}
            </Button>
          </div>
        </form>
      </div>
    </Modal>
  );
}
