import api from './api';

/**
 * ========================================
 * SERVICE DE SETOR
 * ========================================
 * 
 * Funções para gerenciar setores
 */

// Endpoint base (facilita manutenção)
const ENDPOINT = '/setor';

/**
 * Lista todos os setores
 * @param {Object} params - Parâmetros de paginação e filtros
 * @returns {Promise} - Lista paginada de setores
 */
export const listar = async (params = {}) => {
  const response = await api.get(ENDPOINT, { params });
  return response.data;
};

/**
 * Busca um setor por ID
 * @param {number} id - ID do setor
 * @returns {Promise} - Dados do setor
 */
export const buscarPorId = async (id) => {
  const response = await api.get(`${ENDPOINT}/${id}`);
  return response.data;
};

/**
 * Cria novo setor
 * @param {Object} data - Dados do setor
 * @returns {Promise} - Setor criado
 */
export const criar = async (data) => {
  const response = await api.post(ENDPOINT, data);
  return response.data;
};

/**
 * Atualiza setor existente
 * @param {number} id - ID do setor
 * @param {Object} data - Dados atualizados
 * @returns {Promise} - Setor atualizado
 */
export const atualizar = async (id, data) => {
  const response = await api.put(`${ENDPOINT}/${id}`, data);
  return response.data;
};

/**
 * Deleta um setor
 * @param {number} id - ID do setor
 * @returns {Promise}
 */
export const deletar = async (id) => {
  const response = await api.delete(`${ENDPOINT}/${id}`);
  return response.data;
};

// ========================================
// EXPORT DEFAULT
// ========================================
const setorService = {
  listar,
  buscarPorId,
  criar,
  atualizar,
  deletar,
};

export default setorService;