import api from './api';

const escalaService = {
  listar: async ({ page = 0, size = 10, setorId = '', turno = '', status = '', mes = '', ano = '' }) => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    });

    if (setorId) params.append('setorId', setorId);
    if (turno) params.append('turno', turno);
    if (status) params.append('status', status);
    if (mes) params.append('mes', mes);
    if (ano) params.append('ano', ano);

    const response = await api.get(`/escala?${params.toString()}`);
    return response.data;
  },

  buscarPorId: async (id) => {
    const response = await api.get(`/escala/${id}`);
    return response.data;
  },

  criar: async (data) => {
    const response = await api.post('/escala', data);
    return response.data;
  },

  atualizar: async (id, data) => {
    const response = await api.put(`/escala/${id}`, data);
    return response.data;
  },

  deletar: async (id) => {
    const response = await api.delete(`/escala/${id}`);
    return response.data;
  },
};

export default escalaService;