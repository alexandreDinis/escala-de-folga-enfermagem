import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { QueryProvider } from './providers/QueryProvider';
import { NotificationProvider } from './contexts/NotificationContext';
import { router } from './routes';
import './index.css';

/**
 * ========================================
 * PONTO DE ENTRADA DA APLICAÇÃO
 * ========================================
 * 
 * Estrutura de Providers (ordem importa!):
 * 
 * 1. StrictMode
 *    - Modo estrito do React (detecta problemas)
 *    - Executa effects 2x em desenvolvimento
 *    - Ajuda a encontrar bugs
 * 
 * 2. QueryProvider
 *    - React Query (TanStack Query)
 *    - Gerencia cache e sincronização de dados
 *    - Deve envolver tudo que faz requisições HTTP
 * 
 * 3. NotificationProvider
 *    - Sistema de notificações toast
 *    - Disponível em qualquer componente via useNotification()
 * 
 * 4. RouterProvider
 *    - React Router v6+
 *    - Gerencia navegação entre páginas
 *    - Renderiza rotas definidas em routes/index.jsx
 */

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <QueryProvider>
      <NotificationProvider>
        <RouterProvider router={router} />
      </NotificationProvider>
    </QueryProvider>
  </StrictMode>
);
