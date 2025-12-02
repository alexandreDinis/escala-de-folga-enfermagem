import api from './api';

/**
 * ========================================
 * SERVIÇO DE COLABORADORES
 * ========================================
 * 
 * Gerencia requisições para a API de colaboradores
 */

const colaboradorService = {
  /**
   * Listar colaboradores com paginação e filtros
   */
  listar: async ({ page = 0, size = 10, search = '', setorId = '', turno = '', cargo = '' }) => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    });

    if (search) params.append('search', search);
    if (setorId) params.append('setorId', setorId);
    if (turno) params.append('turno', turno);
    if (cargo) params.append('cargo', cargo);

    const response = await api.get(`/colaborador?${params.toString()}`);
    return response.data;
  },

  /**
   * Buscar colaborador por ID
   */
  buscarPorId: async (id) => {
    const response = await api.get(`/colaborador/${id}`);
    return response.data;
  },

  /**
   * Criar novo colaborador
   */
  criar: async (data) => {
    const response = await api.post('/colaborador', data);
    return response.data;
  },

  /**
   * Atualizar colaborador
   */
  atualizar: async (id, data) => {
    const response = await api.put(`/colaborador/${id}`, data);
    return response.data;
  },

  /**
   * Inativar colaborador (soft delete)
   */
  deletar: async (id) => {
    const response = await api.delete(`/colaborador/${id}`);
    return response.data;
  },
};

export default colaboradorService;
