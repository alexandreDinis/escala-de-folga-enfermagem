import { createBrowserRouter, Navigate } from 'react-router-dom';
import App from '../App';
import ComponentsDemo from '../pages/ComponentsDemo';
import SetorPage from '../pages/setor/SetorPage';
import ColaboradorPage from '../pages/colaborador/ColaboradorPage';
import EscalaPage from '../pages/escala/EscalaPage';
import EscalaCalendarioPage from '../pages/escala/EscalaCalendarioPage';


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

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <Navigate to="/dashboard" replace />,
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
      path: 'escalas/:id/calendario',
      element: <EscalaCalendarioPage />,
      },
{
        path: 'folgas',
        element: <div className="p-8">
          <h1 className="text-3xl font-bold mb-4">Folgas</h1>
          <p className="text-gray-600">Página de gerenciamento de folgas (em desenvolvimento)</p>
        </div>,
      },
      {
        path: 'components',
        element: <ComponentsDemo />,
      },
    ],
  },
]);

export default router;


