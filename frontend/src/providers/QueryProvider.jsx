import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

/**
 * ========================================
 * CONFIGURAÇÃO GLOBAL DO REACT QUERY
 * ========================================
 * 
 * React Query gerencia:
 * - Cache de dados do backend
 * - Estados de loading/error/success
 * - Sincronização automática
 * - Refetch em background
 * - Retry automático em caso de erro
 * 
 * Documentação: https://tanstack.com/query/latest
 */
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      /**
       * Retry: Quantas vezes tentar novamente em caso de erro
       * 0 = não tenta novamente
       * 1 = tenta 1 vez (total: 2 tentativas)
       * 3 = tenta 3 vezes (total: 4 tentativas)
       */
      retry: 1,

      /**
       * Refetch on Window Focus
       * true = recarrega dados ao focar na janela
       * false = não recarrega (recomendado para evitar requests desnecessários)
       */
      refetchOnWindowFocus: false,

      /**
       * Stale Time: Tempo em que os dados são considerados "frescos"
       * Durante esse tempo, o React Query usa o cache ao invés de buscar novamente
       * 
       * 0 = sempre busca (sem cache)
       * 1000 * 60 = 1 minuto
       * 1000 * 60 * 5 = 5 minutos (recomendado)
       * Infinity = cache nunca expira
       */
      staleTime: 1000 * 60 * 5, // 5 minutos

      /**
       * Cache Time: Tempo que dados ficam no cache após não serem mais usados
       * Após esse tempo, são removidos da memória
       * 
       * Padrão: 5 minutos (não precisa alterar)
       */
      // cacheTime: 1000 * 60 * 5,
    },
    mutations: {
      /**
       * Retry para mutations (POST, PUT, DELETE)
       * Geralmente false porque mutations não devem ser tentadas múltiplas vezes
       * (pode criar registros duplicados)
       */
      retry: false,
    },
  },
});

/**
 * ========================================
 * PROVIDER DO REACT QUERY
 * ========================================
 * 
 * Envolve toda a aplicação para fornecer acesso ao React Query
 * 
 * USO em main.jsx:
 * <QueryProvider>
 *   <App />
 * </QueryProvider>
 */
export function QueryProvider({ children }) {
  return (
    <QueryClientProvider client={queryClient}>
      {children}
      
      {/* 
        DevTools do React Query
        - Botão flutuante no canto inferior direito
        - Mostra queries ativas, cache, refetch, etc.
        - Só aparece em desenvolvimento (DEV)
        - initialIsOpen={false} = começa minimizado
      */}
      {import.meta.env.DEV && (
        <ReactQueryDevtools 
          initialIsOpen={false} 
          position="bottom-right"
        />
      )}
    </QueryClientProvider>
  );
}

/**
 * ========================================
 * EXPORTAR QUERY CLIENT
 * ========================================
 * 
 * Útil para invalidar queries fora de componentes
 * (ex: em services ou utils)
 */
export { queryClient };