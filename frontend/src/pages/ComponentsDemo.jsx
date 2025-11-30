import { useState } from 'react';
import { Button, Input, Select, Modal, Table, Pagination } from '../components/common';
import { Save, Trash2, Search, Plus, Edit } from 'lucide-react';

export default function ComponentsDemo() {
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);

  // Dados de exemplo
  const data = [
    { id: 1, nome: 'João Silva', email: 'joao@hospital.com', cargo: 'Enfermeiro' },
    { id: 2, nome: 'Maria Santos', email: 'maria@hospital.com', cargo: 'Técnica' },
    { id: 3, nome: 'Pedro Costa', email: 'pedro@hospital.com', cargo: 'Enfermeiro' },
  ];

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'nome', label: 'Nome' },
    { key: 'email', label: 'Email' },
    {
      key: 'cargo',
      label: 'Cargo',
      render: (value) => (
        <span className="badge badge-primary">{value}</span>
      ),
    },
    {
      key: 'actions',
      label: 'Ações',
      render: (_, row) => (
        <div className="flex gap-2">
          <Button size="sm" variant="ghost">
            <Edit className="w-4 h-4" />
          </Button>
          <Button size="sm" variant="ghost">
            <Trash2 className="w-4 h-4" />
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900 mb-2">
          Componentes Premium
        </h1>
        <p className="text-gray-600">
          Biblioteca de componentes reutilizáveis Rede D'Or
        </p>
      </div>

      {/* Buttons */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-xl font-bold">Botões</h2>
        </div>
        <div className="card-body space-y-4">
          <div className="flex flex-wrap gap-3">
            <Button variant="primary" leftIcon={Save}>
              Primary
            </Button>
            <Button variant="secondary">Secondary</Button>
            <Button variant="outline">Outline</Button>
            <Button variant="ghost">Ghost</Button>
            <Button variant="danger" leftIcon={Trash2}>
              Danger
            </Button>
            <Button variant="success">Success</Button>
          </div>

          <div className="flex flex-wrap gap-3">
            <Button size="sm">Small</Button>
            <Button size="md">Medium</Button>
            <Button size="lg">Large</Button>
          </div>

          <div className="flex flex-wrap gap-3">
            <Button loading>Loading</Button>
            <Button disabled>Disabled</Button>
          </div>

          <Button fullWidth variant="primary">
            Full Width Button
          </Button>
        </div>
      </div>

      {/* Inputs */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-xl font-bold">Inputs</h2>
        </div>
        <div className="card-body space-y-4">
          <Input
            label="Nome Completo"
            placeholder="Digite seu nome"
            required
          />

          <Input
            label="Email"
            type="email"
            placeholder="seu@email.com"
            leftIcon={Search}
          />

          <Input
            label="Senha"
            type="password"
            error="Senha muito curta"
          />

          <Input
            label="Telefone"
            placeholder="(00) 00000-0000"
            helper="Formato: (DDD) 00000-0000"
          />
        </div>
      </div>

      {/* Select */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-xl font-bold">Select</h2>
        </div>
        <div className="card-body space-y-4">
          <Select
            label="Turno"
            options={[
              { value: 'MANHA', label: 'Manhã' },
              { value: 'TARDE', label: 'Tarde' },
              { value: 'NOITE', label: 'Noite' },
            ]}
            required
          />

          <Select
            label="Cargo"
            options={[
              { value: 'ENFERMEIRO', label: 'Enfermeiro' },
              { value: 'TECNICO', label: 'Técnico' },
            ]}
            error="Campo obrigatório"
          />
        </div>
      </div>

      {/* Modal */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-xl font-bold">Modal</h2>
        </div>
        <div className="card-body">
          <Button onClick={() => setShowModal(true)} leftIcon={Plus}>
            Abrir Modal
          </Button>

          <Modal
            isOpen={showModal}
            onClose={() => setShowModal(false)}
            title="Criar Novo Registro"
            size="md"
            footer={
              <>
                <Button variant="ghost" onClick={() => setShowModal(false)}>
                  Cancelar
                </Button>
                <Button variant="primary" leftIcon={Save}>
                  Salvar
                </Button>
              </>
            }
          >
            <div className="space-y-4">
              <Input label="Nome" placeholder="Digite o nome" required />
              <Input label="Email" type="email" placeholder="email@exemplo.com" />
              <Select
                label="Tipo"
                options={[
                  { value: '1', label: 'Opção 1' },
                  { value: '2', label: 'Opção 2' },
                ]}
              />
            </div>
          </Modal>
        </div>
      </div>

      {/* Table */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-xl font-bold">Tabela com Paginação</h2>
        </div>
        <Table
          columns={columns}
          data={data}
          onRowClick={(row) => console.log('Clicou em:', row)}
        />
        <Pagination
          currentPage={0}
          totalPages={5}
          pageSize={10}
          totalItems={50}
          onPageChange={(page) => console.log('Página:', page)}
          onPageSizeChange={(size) => console.log('Tamanho:', size)}
        />
      </div>

      {/* Badges */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-xl font-bold">Badges</h2>
        </div>
        <div className="card-body">
          <div className="flex flex-wrap gap-3">
            <span className="badge badge-primary">Primary</span>
            <span className="badge badge-success">Success</span>
            <span className="badge badge-error">Error</span>
            <span className="badge badge-warning">Warning</span>
            <span className="badge badge-info">Info</span>
          </div>
        </div>
      </div>

      {/* Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="card">
          <div className="card-body">
            <h3 className="text-lg font-bold mb-2">Card Simples</h3>
            <p className="text-gray-600">Conteúdo do card aqui</p>
          </div>
        </div>

        <div className="card">
          <div className="card-header">
            <h3 className="text-lg font-bold">Card com Header</h3>
          </div>
          <div className="card-body">
            <p className="text-gray-600">Conteúdo do card</p>
          </div>
        </div>

        <div className="card">
          <div className="card-header">
            <h3 className="text-lg font-bold">Card Completo</h3>
          </div>
          <div className="card-body">
            <p className="text-gray-600">Conteúdo do card</p>
          </div>
          <div className="card-footer">
            <Button size="sm" fullWidth>
              Ação
            </Button>
          </div>
        </div>
      </div>

      {/* Loading States */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-xl font-bold">Loading States</h2>
        </div>
        <div className="card-body">
          <div className="flex items-center gap-6">
            <div className="spinner"></div>
            <div className="spinner spinner-lg"></div>
            <Button loading>Carregando</Button>
          </div>
        </div>
      </div>
    </div>
  );
}