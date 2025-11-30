import { createContext, useContext } from 'react';
import toast, { Toaster } from 'react-hot-toast';

/**
 * ========================================
 * CONTEXT DE NOTIFICAÇÕES
 * ========================================
 * 
 * Fornece funções para exibir notificações toast em qualquer componente
 * 
 * Funcionalidades:
 * - success() → Toast verde de sucesso
 * - error() → Toast vermelho de erro
 * - info() → Toast azul de informação
 * - loading() → Toast com spinner (para operações assíncronas)
 * - dismiss() → Fecha um toast específico
 */
const NotificationContext = createContext({});

/**
 * ========================================
 * HOOK CUSTOMIZADO
 * ========================================
 * 
 * USO em qualquer componente:
 * 
 * import { useNotification } from '@/contexts/NotificationContext';
 * 
 * function MeuComponente() {
 *   const { success, error, info, loading } = useNotification();
 *   
 *   success('Operação realizada!');
 *   error('Algo deu errado');
 * }
 * 
 * @throws {Error} Se usado fora do NotificationProvider
 */
export function useNotification() {
  const context = useContext(NotificationContext);

  if (!context) {
    throw new Error(
      'useNotification deve ser usado dentro de NotificationProvider'
    );
  }

  return context;
}

/**
 * ========================================
 * PROVIDER DE NOTIFICAÇÕES
 * ========================================
 * 
 * Envolve a aplicação e fornece o sistema de notificações
 * 
 * USO em main.jsx:
 * <NotificationProvider>
 *   <App />
 * </NotificationProvider>
 */
export function NotificationProvider({ children }) {
  /**
   * Exibe notificação de SUCESSO (verde)
   * 
   * @param {string} message - Mensagem a ser exibida
   * @param {object} options - Opções adicionais (duração, posição, etc.)
   * 
   * @example
   * success('Setor criado com sucesso!');
   * success('Salvo!', { duration: 5000 }); // 5 segundos
   */
  const showSuccess = (message, options = {}) => {
    toast.success(message, {
      duration: 3000, // 3 segundos
      position: 'top-right',
      ...options,
    });
  };

  /**
   * Exibe notificação de ERRO (vermelho)
   * 
   * @param {string} message - Mensagem de erro
   * @param {object} options - Opções adicionais
   * 
   * @example
   * error('Falha ao criar setor');
   * error('Erro de validação', { duration: 5000 });
   */
  const showError = (message, options = {}) => {
    toast.error(message, {
      duration: 4000, // 4 segundos (erros ficam um pouco mais tempo)
      position: 'top-right',
      ...options,
    });
  };

  /**
   * Exibe notificação de INFORMAÇÃO (azul/neutra)
   * 
   * @param {string} message - Mensagem informativa
   * @param {object} options - Opções adicionais
   * 
   * @example
   * info('Salvando automaticamente...');
   * info('3 itens selecionados');
   */
  const showInfo = (message, options = {}) => {
    toast(message, {
      duration: 3000,
      position: 'top-right',
      icon: 'ℹ️',
      ...options,
    });
  };

  /**
   * Exibe notificação de LOADING (spinner animado)
   * Útil para operações assíncronas longas
   * 
   * @param {string} message - Mensagem de loading
   * @returns {string} ID do toast (para fechar depois)
   * 
   * @example
   * const toastId = loading('Criando setor...');
   * 
   * try {
   *   await setorService.criar(data);
   *   dismiss(toastId);
   *   success('Setor criado!');
   * } catch (error) {
   *   dismiss(toastId);
   *   error('Falha ao criar');
   * }
   */
  const showLoading = (message = 'Carregando...') => {
    return toast.loading(message, {
      position: 'top-right',
    });
  };

  /**
   * Fecha um toast específico
   * 
   * @param {string} toastId - ID do toast retornado por loading()
   * 
   * @example
   * const id = loading('Salvando...');
   * // ... operação ...
   * dismiss(id);
   */
  const dismiss = (toastId) => {
    toast.dismiss(toastId);
  };

  /**
   * Fecha TODOS os toasts
   * 
   * @example
   * dismissAll(); // Limpa a tela de todas as notificações
   */
  const dismissAll = () => {
    toast.dismiss();
  };

  // Valor do Context (funções disponíveis)
    const value = {
    showSuccess,    
    showError,      
    showInfo,       
    showLoading,    
    dismiss,
    dismissAll,
  };

  return (
    <NotificationContext.Provider value={value}>
      {/* 
        Toaster: Componente que RENDERIZA os toasts
        Deve estar no topo da aplicação
      */}
      <Toaster
        position="top-right"
        reverseOrder={false} // Novos toasts aparecem em cima
        gutter={8} // Espaçamento entre toasts (8px)
        
        toastOptions={{
          // ========================================
          // ESTILOS GLOBAIS DOS TOASTS
          // ========================================
          
          // Estilo base (aplicado a TODOS os toasts)
          style: {
            background: '#333',
            color: '#fff',
            padding: '16px',
            borderRadius: '8px',
            fontSize: '14px',
            maxWidth: '500px',
          },
          
          // Duração padrão (sobrescrita por cada tipo)
          duration: 3000,
          
          // ========================================
          // ESTILOS ESPECÍFICOS POR TIPO
          // ========================================
          
          // Toast de SUCESSO (verde)
          success: {
            duration: 3000,
            iconTheme: {
              primary: '#10b981', // Verde
              secondary: '#fff',
            },
            style: {
              background: '#10b981',
              color: '#fff',
            },
          },
          
          // Toast de ERRO (vermelho)
          error: {
            duration: 4000,
            iconTheme: {
              primary: '#ef4444', // Vermelho
              secondary: '#fff',
            },
            style: {
              background: '#ef4444',
              color: '#fff',
            },
          },
          
          // Toast de LOADING (azul)
          loading: {
            iconTheme: {
              primary: '#3b82f6', // Azul
              secondary: '#fff',
            },
            style: {
              background: '#3b82f6',
              color: '#fff',
            },
          },
        }}
      />
      
      {children}
    </NotificationContext.Provider>
  );
}