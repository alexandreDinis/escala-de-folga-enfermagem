import api from './api';

/**
 * Service de comunicação com backend - Módulo Colaborador
 */
export const colaboradorService = {
  listar: async (params = {}) => {
    const { page = 0, size = 10, sort = 'id,asc' } = params;
    const response = await api.get('/colaborador', {
      params: { page, size, sort },
    });
    return response.data;
  },

  buscarPorId: async (id) => {
    const response = await api.get(`/colaborador/${id}`);
    return response.data;
  },

  criar: async (data) => {
    const response = await api.post('/colaborador', data);
    return response.data;
  },

  atualizar: async (id, data) => {
    const response = await api.put(`/colaborador/${id}`, data);
    return response.data;
  },

  inativar: async (id) => {
    const response = await api.delete(`/colaborador/${id}`);
    return response.data;
  },
};