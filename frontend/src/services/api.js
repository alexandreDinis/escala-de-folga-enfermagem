import axios from 'axios';

/**
 * ========================================
 * CONFIGURAÇÃO DO AXIOS
 * ========================================
 */

/**
 * URL base da API
 */
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

/**
 * Instância configurada do Axios
 */
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * ========================================
 * INTERCEPTADOR DE REQUISIÇÃO
 * ========================================
 */
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth_token');
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * ========================================
 * INTERCEPTADOR DE RESPOSTA
 * ========================================
 */
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // ✅ Manter o erro original do Axios
    // Apenas adicionar propriedades extras para facilitar o uso
    
    if (!error.response) {
      // Erro de rede (sem resposta do servidor)
      error.friendlyMessage = 'Erro de conexão com o servidor. Verifique sua internet.';
      error.errorType = 'network';
      return Promise.reject(error);
    }

    const { status, data } = error.response;

    // ✅ Adicionar mensagem amigável ao erro original
    // (sem destruir a estrutura original do Axios)
    
    if (status === 401) {
      error.friendlyMessage = 'Sessão expirada. Faça login novamente.';
      error.errorType = 'auth';
      localStorage.removeItem('auth_token');
      // window.location.href = '/login';
      
    } else if (status === 403) {
      error.friendlyMessage = 'Você não tem permissão para realizar esta ação.';
      error.errorType = 'forbidden';
      
    } else if (status === 404) {
      error.friendlyMessage = 'Recurso não encontrado.';
      error.errorType = 'not_found';
      
    } else if (status === 400 || status === 422) {
      // Erro de validação - usar mensagem do backend
      error.friendlyMessage = data?.detail || data?.message || 'Verifique os campos e tente novamente.';
      error.errorType = 'validation';
      
    } else if (status >= 500) {
      error.friendlyMessage = 'Erro interno do servidor. Tente novamente mais tarde.';
      error.errorType = 'server';
      
    } else {
      error.friendlyMessage = data?.message || data?.detail || 'Erro desconhecido';
      error.errorType = 'generic';
    }

    // ✅ Retornar o erro original do Axios (com propriedades extras)
    return Promise.reject(error);
  }
);

export default api;