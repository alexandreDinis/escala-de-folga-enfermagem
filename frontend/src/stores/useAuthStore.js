import { create } from 'zustand';
import { persist } from 'zustand/middleware';

/**
 * ========================================
 * STORE DE AUTENTICAÇÃO (ZUSTAND)
 * ========================================
 * 
 * Gerencia o estado global de autenticação:
 * - Dados do usuário logado
 * - Token JWT
 * - Status de autenticação
 * 
 * Funcionalidades:
 * - Persistência automática no localStorage
 * - Login/Logout
 * - Atualização de dados do usuário
 * 
 * USO:
 * import { useAuthStore } from '@/stores/useAuthStore';
 * 
 * function Component() {
 *   const { user, token, login, logout } = useAuthStore();
 *   // ou selecione apenas o que precisa:
 *   const user = useAuthStore((state) => state.user);
 * }
 * 
 * Documentação Zustand: https://github.com/pmndrs/zustand
 */
export const useAuthStore = create(
  persist(
    (set, get) => ({
      /**
       * ========================================
       * ESTADO
       * ========================================
       */
      
      /**
       * Dados do usuário logado
       * @type {Object|null}
       * @property {number} id - ID do usuário
       * @property {string} nome - Nome completo
       * @property {string} email - Email
       * @property {string} role - Papel (ADMIN, ENFERMEIRO, etc.)
       */
      user: null,

      /**
       * Token JWT de autenticação
       * @type {string|null}
       */
      token: null,

      /**
       * Flag indicando se usuário está autenticado
       * @type {boolean}
       */
      isAuthenticated: false,

      /**
       * ========================================
       * AÇÕES
       * ========================================
       */

      /**
       * Realiza login do usuário
       * 
       * Salva os dados no store e no localStorage (automático via persist)
       * Adiciona token no localStorage para ser usado pelo Axios
       * 
       * @param {Object} userData - Dados do usuário retornados pelo backend
       * @param {string} authToken - Token JWT
       * 
       * @example
       * const { login } = useAuthStore();
       * 
       * const response = await api.post('/auth/login', { email, password });
       * login(response.data.user, response.data.token);
       */
      login: (userData, authToken) => {
        // Salva token no localStorage para o Axios usar
        // (o interceptador do Axios busca de lá)
        localStorage.setItem('auth_token', authToken);

        // Atualiza o store
        set({
          user: userData,
          token: authToken,
          isAuthenticated: true,
        });

        console.log('✅ Login realizado:', userData.nome || userData.email);
      },

      /**
       * Realiza logout do usuário
       * 
       * Remove dados do store e do localStorage
       * 
       * @example
       * const { logout } = useAuthStore();
       * 
       * logout();
       * // Redireciona para /login se necessário
       */
      logout: () => {
        // Remove token do localStorage
        localStorage.removeItem('auth_token');

        // Limpa o store
        set({
          user: null,
          token: null,
          isAuthenticated: false,
        });

        console.log('🔓 Logout realizado');
        
        // TODO: Redirecionar para /login quando implementar rotas
        // window.location.href = '/login';
      },

      /**
       * Atualiza dados do usuário
       * 
       * Útil para atualizar perfil sem fazer logout/login
       * 
       * @param {Object} userData - Novos dados do usuário (parcial)
       * 
       * @example
       * const { updateUser } = useAuthStore();
       * 
       * updateUser({ nome: 'Novo Nome' });
       */
      updateUser: (userData) => {
        set((state) => ({
          user: {
            ...state.user,
            ...userData,
          },
        }));

        console.log('📝 Dados do usuário atualizados');
      },

      /**
       * Verifica se usuário tem uma role específica
       * 
       * @param {string} role - Role a verificar (ex: 'ADMIN', 'ENFERMEIRO')
       * @returns {boolean}
       * 
       * @example
       * const { hasRole } = useAuthStore();
       * 
       * if (hasRole('ADMIN')) {
       *   // Mostra botão de admin
       * }
       */
      hasRole: (role) => {
        const { user } = get();
        return user?.role === role;
      },

      /**
       * Verifica se usuário tem pelo menos uma das roles fornecidas
       * 
       * @param {string[]} roles - Array de roles
       * @returns {boolean}
       * 
       * @example
       * const { hasAnyRole } = useAuthStore();
       * 
       * if (hasAnyRole(['ADMIN', 'ENFERMEIRO'])) {
       *   // Mostra conteúdo
       * }
       */
      hasAnyRole: (roles) => {
        const { user } = get();
        return roles.includes(user?.role);
      },

      /**
       * Retorna iniciais do nome do usuário
       * Útil para avatares
       * 
       * @returns {string} - Ex: "JS" para "João Silva"
       * 
       * @example
       * const { getInitials } = useAuthStore();
       * 
       * <Avatar>{getInitials()}</Avatar>
       */
      getInitials: () => {
        const { user } = get();
        if (!user?.nome) return '??';
        
        const names = user.nome.split(' ');
        if (names.length === 1) return names[0].substring(0, 2).toUpperCase();
        
        return (names[0][0] + names[names.length - 1][0]).toUpperCase();
      },
    }),
    {
      /**
       * ========================================
       * CONFIGURAÇÃO DO PERSIST MIDDLEWARE
       * ========================================
       */
      
      /**
       * Nome da chave no localStorage
       * Dados serão salvos em: localStorage.getItem('auth-storage')
       */
      name: 'auth-storage',

      /**
       * Seleciona quais partes do estado devem ser persistidas
       * Não persiste funções, apenas dados
       */
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
);