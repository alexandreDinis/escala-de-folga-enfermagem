import ComponentsDemo from '../pages/ComponentsDemo';
import SetorPage from '../pages/setor/SetorPage';
import { createBrowserRouter, Navigate } from 'react-router-dom';
import Calendario from '../components/Calendario';
import App from '../App';

/**
 * ========================================
 * PÁGINAS TEMPORÁRIAS (PLACEHOLDERS)
 * ========================================
 * Remover quando criar as páginas reais
 */

function Dashboard() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Dashboard</h1>
      <p className="text-gray-600">
        Bem-vindo ao sistema de Escala de Folga! 🏥
      </p>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-8">
        <div className="p-6 bg-white rounded-lg shadow">
          <h3 className="text-xl font-semibold mb-2">Setores</h3>
          <p className="text-gray-500">Gerencie os setores do hospital</p>
        </div>
        <div className="p-6 bg-white rounded-lg shadow">
          <h3 className="text-xl font-semibold mb-2">Colaboradores</h3>
          <p className="text-gray-500">Cadastre e gerencie colaboradores</p>
        </div>
        <div className="p-6 bg-white rounded-lg shadow">
          <h3 className="text-xl font-semibold mb-2">Escalas</h3>
          <p className="text-gray-500">Crie e publique escalas mensais</p>
        </div>
      </div>
    </div>
  );
}



function ColaboradorPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Colaboradores</h1>
      <p className="text-gray-600">Página de gerenciamento de colaboradores</p>
    </div>
  );
}

function EscalaPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Escalas</h1>
      <p className="text-gray-600">Página de gerenciamento de escalas</p>
    </div>
  );
}

function FolgaPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Folgas</h1>
      <p className="text-gray-600">Página de gerenciamento de folgas</p>
    </div>
  );
}

/**
 * ========================================
 * CONFIGURAÇÃO DE ROTAS
 * ========================================
 */
export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <Navigate to="/dashboard" replace />,
      },
       // Adicionar nas rotas children:
        {
        path: 'demo',
        element: <ComponentsDemo />,
},

      {
        path: 'dashboard',
        element: <Dashboard />,
      },
      {
        path: 'setores',
        element: <SetorPage />,
      },
      {
        path: 'colaboradores',
        element: <ColaboradorPage />,
      },
      {
        path: 'escalas',
        element: <EscalaPage />,
      },
      {
        path: 'folgas',
        element: <FolgaPage />,
      },
      {
        path: 'calendario',
        element: <Calendario escalaId={552} />, 
      },
    ],
  },
  {
    path: '*',
    element: (
      <div className="flex h-screen items-center justify-center bg-gray-50">
        <div className="text-center">
          <h1 className="text-9xl font-bold text-gray-800">404</h1>
          <p className="text-2xl text-gray-600 mt-4 mb-8">
            Página não encontrada
          </p>
          <p className="text-gray-500 mb-8">
            A página que você está procurando não existe.
          </p>
          <a
            href="/"
            className="px-6 py-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            Voltar para o início
          </a>
        </div>
      </div>
    ),
  },
]);