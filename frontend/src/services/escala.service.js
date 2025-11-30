import api from './api';

/**
 * ========================================
 * SERVICE DE ESCALA
 * ========================================
 * 
 * Funções para gerenciar escalas e calendários
 */

/**
 * Busca calendário de uma escala específica
 * @param {number} escalaId - ID da escala
 * @returns {Promise} - Dados do calendário
 */
export const getCalendario = async (escalaId) => {
  const response = await api.get(`/escalas/${escalaId}/calendario`);
  return response.data;
};

/**
 * Lista todas as escalas
 * @param {Object} params - Parâmetros de paginação e filtros
 * @returns {Promise} - Lista paginada de escalas
 */
export const listar = async (params = {}) => {
  const response = await api.get('/escalas', { params });
  return response.data;
};

/**
 * Busca uma escala por ID
 * @param {number} id - ID da escala
 * @returns {Promise} - Dados da escala
 */
export const buscarPorId = async (id) => {
  const response = await api.get(`/escalas/${id}`);
  return response.data;
};

/**
 * Cria nova escala
 * @param {Object} data - Dados da escala
 * @returns {Promise} - Escala criada
 */
export const criar = async (data) => {
  const response = await api.post('/escalas', data);
  return response.data;
};

/**
 * Atualiza escala existente
 * @param {number} id - ID da escala
 * @param {Object} data - Dados atualizados
 * @returns {Promise} - Escala atualizada
 */
export const atualizar = async (id, data) => {
  const response = await api.put(`/escalas/${id}`, data);
  return response.data;
};

/**
 * Deleta uma escala
 * @param {number} id - ID da escala
 * @returns {Promise}
 */
export const deletar = async (id) => {
  const response = await api.delete(`/escalas/${id}`);
  return response.data;
};

/**
 * Publica uma escala
 * @param {number} id - ID da escala
 * @returns {Promise}
 */
export const publicar = async (id) => {
  const response = await api.post(`/escalas/${id}/publicar`);
  return response.data;
};

// Exportação default
const escalaService = {
  getCalendario,
  listar,
  buscarPorId,
  criar,
  atualizar,
  deletar,
  publicar,
};

export default escalaService;