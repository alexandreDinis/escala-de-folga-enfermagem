/**
 * URL base da API backend
 * Altere conforme seu ambiente (dev, staging, prod)
 */
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

/**
 * Enums sincronizados com o backend
 */
export const CARGO_ENUM = {
  ENFERMEIRO: 'ENFERMEIRO',
  TECNICO: 'TECNICO',
};

export const TURNO_ENUM = {
  MANHA: 'MANHA',
  TARDE: 'TARDE',
  NOITE: 'NOITE',
};

export const STATUS_ESCALA_ENUM = {
  NOVA: 'NOVA',
  PARCIAL: 'PARCIAL',
  PUBLICADA: 'PUBLICADA',
  FECHADA: 'FECHADA',
};

export const STATUS_FOLGA_ENUM = {
  PENDENTE: 'PENDENTE',
  APROVADA: 'APROVADA',
  NEGADA: 'NEGADA',
};

/**
 * Labels traduzidos para exibição na UI
 */
export const CARGO_LABELS = {
  [CARGO_ENUM.ENFERMEIRO]: 'Enfermeiro',
  [CARGO_ENUM.TECNICO]: 'Técnico',
};

export const TURNO_LABELS = {
  [TURNO_ENUM.MANHA]: 'Manhã',
  [TURNO_ENUM.TARDE]: 'Tarde',
  [TURNO_ENUM.NOITE]: 'Noite',
};

export const STATUS_FOLGA_LABELS = {
  [STATUS_FOLGA_ENUM.PENDENTE]: 'Pendente',
  [STATUS_FOLGA_ENUM.APROVADA]: 'Aprovada',
  [STATUS_FOLGA_ENUM.NEGADA]: 'Negada',
};

/**
 * Configurações de paginação
 */
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 10,
  PAGE_SIZE_OPTIONS: [10, 20, 50, 100],
};

/**
 * Mensagens de erro padrão
 */
export const ERROR_MESSAGES = {
  NETWORK_ERROR: 'Erro de conexão com o servidor. Verifique sua internet.',
  UNAUTHORIZED: 'Sessão expirada. Faça login novamente.',
  FORBIDDEN: 'Você não tem permissão para realizar esta ação.',
  NOT_FOUND: 'Recurso não encontrado.',
  SERVER_ERROR: 'Erro interno do servidor. Tente novamente mais tarde.',
  VALIDATION_ERROR: 'Verifique os campos e tente novamente.',
};