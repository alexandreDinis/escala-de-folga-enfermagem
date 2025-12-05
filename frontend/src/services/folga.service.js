import api from './api';

/**
 * ========================================
 * SERVICE DE FOLGA
 * ========================================
 * 
 * Funções para gerenciar folgas
 */

/**
 * Lista todas as folgas
 * @param {Object} params - Parâmetros de paginação e filtros
 * @returns {Promise} - Lista paginada de folgas
 */
export const listar = async (params = {}) => {
  const response = await api.get('/folgas', { params });
  return response.data;
};

/**
 * Busca uma folga por ID
 * @param {number} id - ID da folga
 * @returns {Promise} - Dados da folga
 */
export const buscarPorId = async (id) => {
  const response = await api.get(`/folgas/${id}`);
  return response.data;
};

/**
 * Cria nova folga
 * @param {Object} data - Dados da folga
 * @returns {Promise} - Folga criada
 */
export const criarFolga = async (data) => {
  const response = await api.post('/folgas', data);
  return response.data;
};

/**
 * Atualiza folga existente
 * @param {number} id - ID da folga
 * @param {Object} data - Dados atualizados
 * @returns {Promise} - Folga atualizada
 */
export const atualizar = async (id, data) => {
  const response = await api.put(`/folgas/${id}`, data);
  return response.data;
};

/**
 * Deleta uma folga
 * @param {number} id - ID da folga
 * @returns {Promise}
 */
export const deletarFolga = async (id) => {
  const response = await api.delete(`/folgas/${id}`);
  return response.data;
};

/**
 * Aprova uma folga
 * @param {number} id - ID da folga
 * @returns {Promise}
 */
export const aprovar = async (id) => {
  const response = await api.post(`/folgas/${id}/aprovar`);
  return response.data;
};

/**
 * Nega uma folga
 * @param {number} id - ID da folga
 * @param {string} motivo - Motivo da negação
 * @returns {Promise}
 */
export const negar = async (id, motivo) => {
  const response = await api.post(`/folgas/${id}/negar`, { motivo });
  return response.data;
};

/**
 * Cadastra histórico de última folga
 * @param {number} colaboradorId - ID do colaborador
 * @param {string} dataSolicitada - Data da última folga (YYYY-MM-DD)
 * @param {number} escalaId - ID da escala (para validação)
 * @returns {Promise}
 */
export const cadastrarHistorico = async (colaboradorId, dataSolicitada, escalaId) => {
  const response = await api.post('/folga/historico', {
    colaboradorId,
    dataSolicitada,
    escalaId,  // ✅ ADICIONAR
  });
  return response.data;
};

/**
 * ✅ Valida se a data da última folga é permitida para a escala
 * @param {Object} data - { colaboradorId, dataSolicitada, escalaId }
 * @returns {Promise} - { valido, mensagem, dataMinimPermitida, diasTrabalhoMaximo }
 */
export const validarDataUltimaFolga = async (data) => {
  console.log('📤 Enviando validação:', data);
  const response = await api.post('/folga/validar-data-ultima-folga', data);
  console.log('📥 Resposta validação:', response.data);
  return response.data;
};

// ✅ Exportação default
const folgaService = {
  listar,
  buscarPorId,
  criarFolga,
  atualizar,
  deletarFolga,
  aprovar,
  negar,
  cadastrarHistorico,
  validarDataUltimaFolga,  // ✅ ADICIONAR
};

export default folgaService;