import api from './api';

const escalaService = {
  /**
   * Lista escalas com filtros e paginação
   */
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

  /**
   * Busca uma escala por ID
   */
  buscarPorId: async (id) => {
    const response = await api.get(`/escala/${id}`);
    return response.data;
  },

  /**
   * Cria uma nova escala
   */
  criar: async (data) => {
    const response = await api.post('/escala', data);
    return response.data;
  },

  /**
   * Atualiza uma escala existente
   */
  atualizar: async (id, data) => {
    const response = await api.put(`/escala/${id}`, data);
    return response.data;
  },

  /**
   * Deleta uma escala
   */
  deletar: async (id) => {
    const response = await api.delete(`/escala/${id}`);
    return response.data;
  },

  /**
   * Verifica se há colaboradores sem histórico de folga
   */
  verificarHistorico: async (id) => {
    const response = await api.get(`/escala/${id}/verificar-historico`);
    return response.data;
  },

  /**
   * Busca escala com calendário e lista de colaboradores
   * Usado para carregar o calendário visual com disponibilidade de folgas
   */
  buscarComColaboradores: async (id) => {
    const response = await api.get(`/escala/${id}/com-colaboradores`);
    return response.data;
  },
};

export default escalaService;