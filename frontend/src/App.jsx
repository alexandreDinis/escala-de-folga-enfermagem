import { Outlet, Link, useLocation } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Building2, 
  Users, 
  Calendar, 
  Palmtree,
  Menu,
  X,
  Bell,
  Settings,
  LogOut,
  ChevronDown
} from 'lucide-react';
import { useState } from 'react';
import { Toaster } from 'react-hot-toast'; // ✅ NOVO
import { NotificationProvider } from './contexts/NotificationContext'; // ✅ NOVO
import logoEnfermagem from './assets/logo/logo_enfermagem.png';
import './App.css';

/**
 * ========================================
 * LAYOUT PRINCIPAL - ESTILO REDE D'OR
 * ========================================
 * 
 * Design Premium Hospitalar:
 * - Header fixo com logo institucional
 * - Sidebar responsiva com animações
 * - Menu com ícones Lucide React
 * - Cores institucionais Rede D'Or
 */

function App() {
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [userMenuOpen, setUserMenuOpen] = useState(false);

  /**
   * Verifica se o link está ativo
   */
  const isActive = (path) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  /**
   * Items do menu lateral
   */
  const menuItems = [
    {
      path: '/dashboard',
      icon: LayoutDashboard,
      label: 'Dashboard',
      badge: null,
    },
    {
      path: '/setores',
      icon: Building2,
      label: 'Setores',
      badge: null,
    },
    {
      path: '/colaboradores',
      icon: Users,
      label: 'Colaboradores',
      badge: null,
    },
    {
      path: '/escalas',
      icon: Calendar,
      label: 'Escalas',
      badge: '3', // Exemplo de badge
    },
    {
      path: '/folgas',
      icon: Palmtree,
      label: 'Folgas',
      badge: null,
    },
  ];

  return (
    <NotificationProvider> {/* ✅ NOVO */}
      <div className="min-h-screen bg-gray-50">
        {/* ========================================
            HEADER PREMIUM
            ======================================== */}
        <header className="fixed top-0 left-0 right-0 z-40 bg-white border-b border-gray-200 shadow-sm">
          <div className="flex items-center justify-between h-20 px-6">
            {/* Logo + Título */}
            <div className="flex items-center gap-4">
              {/* Botão Menu Mobile */}
              <button
                onClick={() => setSidebarOpen(!sidebarOpen)}
                className="lg:hidden p-2 rounded-lg hover:bg-gray-100 transition-colors"
              >
                {sidebarOpen ? (
                  <X className="w-6 h-6 text-gray-600" />
                ) : (
                  <Menu className="w-6 h-6 text-gray-600" />
                )}
              </button>

              {/* Logo */}
              <Link to="/" className="flex items-center gap-3">
                <img 
                  src={logoEnfermagem} 
                  alt="Logo Enfermagem" 
                  className="h-12 w-auto"
                />
                <div className="hidden sm:block">
                  <h1 className="text-xl font-bold text-primary leading-tight">
                    Escala de Folga
                  </h1>
                  <p className="text-xs text-gray-500 font-medium">
                    Sistema de Gestão Hospitalar
                  </p>
                </div>
              </Link>
            </div>

            {/* Actions */}
            <div className="flex items-center gap-3">
              {/* Notificações */}
              <button className="relative p-2 rounded-lg hover:bg-gray-100 transition-colors">
                <Bell className="w-5 h-5 text-gray-600" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-error rounded-full"></span>
              </button>

              {/* Configurações */}
              <button className="p-2 rounded-lg hover:bg-gray-100 transition-colors">
                <Settings className="w-5 h-5 text-gray-600" />
              </button>

              {/* Separador */}
              <div className="w-px h-8 bg-gray-200 hidden sm:block"></div>

              {/* User Menu */}
              <div className="relative">
                <button
                  onClick={() => setUserMenuOpen(!userMenuOpen)}
                  className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-gray-100 transition-colors"
                >
                  <div className="w-9 h-9 rounded-full bg-gradient-to-br from-primary to-primary-dark flex items-center justify-center text-white font-bold text-sm shadow-md">
                    AD
                  </div>
                  <div className="hidden md:block text-left">
                    <p className="text-sm font-semibold text-gray-900">
                      Administrador
                    </p>
                    <p className="text-xs text-gray-500">
                      admin@hospital.com
                    </p>
                  </div>
                  <ChevronDown className="w-4 h-4 text-gray-400 hidden md:block" />
                </button>

                {/* Dropdown Menu */}
                {userMenuOpen && (
                  <>
                    {/* Overlay */}
                    <div
                      className="fixed inset-0 z-40"
                      onClick={() => setUserMenuOpen(false)}
                    ></div>

                    {/* Menu */}
                    <div className="absolute right-0 mt-2 w-56 bg-white rounded-xl shadow-xl border border-gray-200 py-2 z-50 animate-slide-down">
                      <div className="px-4 py-3 border-b border-gray-100">
                        <p className="text-sm font-semibold text-gray-900">
                          Administrador
                        </p>
                        <p className="text-xs text-gray-500 truncate">
                          admin@hospital.com
                        </p>
                      </div>
                      
                      <button className="w-full px-4 py-2.5 text-left text-sm text-gray-700 hover:bg-gray-50 flex items-center gap-3 transition-colors">
                        <Settings className="w-4 h-4" />
                        Configurações
                      </button>
                      
                      <div className="border-t border-gray-100 mt-2 pt-2">
                        <button className="w-full px-4 py-2.5 text-left text-sm text-error hover:bg-red-50 flex items-center gap-3 transition-colors">
                          <LogOut className="w-4 h-4" />
                          Sair
                        </button>
                      </div>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>
        </header>

        {/* ========================================
            SIDEBAR PREMIUM
            ======================================== */}
        
        {/* Overlay Mobile */}
        {sidebarOpen && (
          <div
            className="fixed inset-0 bg-black/50 backdrop-blur-sm z-30 lg:hidden"
            onClick={() => setSidebarOpen(false)}
          ></div>
        )}

        {/* Sidebar */}
        <aside
          className={`
            fixed top-20 left-0 bottom-0 z-30
            w-72 bg-white border-r border-gray-200
            transition-transform duration-300 ease-in-out
            ${sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
            overflow-y-auto scrollbar-premium
          `}
        >
          <nav className="p-4 space-y-1">
            {menuItems.map((item) => {
              const Icon = item.icon;
              const active = isActive(item.path);

              return (
                <Link
                  key={item.path}
                  to={item.path}
                  onClick={() => setSidebarOpen(false)}
                  className={`
                    group flex items-center justify-between px-4 py-3.5 rounded-xl
                    font-semibold text-sm transition-all duration-200
                    ${
                      active
                        ? 'bg-gradient-to-r from-primary to-primary-dark text-white shadow-lg shadow-primary/30'
                        : 'text-gray-700 hover:bg-gray-50 hover:text-primary'
                    }
                  `}
                >
                  <div className="flex items-center gap-3">
                    <Icon
                      className={`w-5 h-5 transition-transform duration-200 ${
                        active ? 'scale-110' : 'group-hover:scale-110'
                      }`}
                    />
                    <span>{item.label}</span>
                  </div>

                  {/* Badge */}
                  {item.badge && (
                    <span
                      className={`
                        px-2 py-0.5 text-xs font-bold rounded-full
                        ${
                          active
                            ? 'bg-white/20 text-white'
                            : 'bg-error text-white'
                        }
                      `}
                    >
                      {item.badge}
                    </span>
                  )}
                </Link>
              );
            })}
          </nav>

          {/* Footer Sidebar */}
          <div className="absolute bottom-0 left-0 right-0 p-4 border-t border-gray-200 bg-gradient-to-br from-primary-light to-white">
            <div className="flex items-center gap-3 p-3 rounded-xl bg-white shadow-sm">
              <div className="w-10 h-10 rounded-full bg-gradient-to-br from-secondary to-secondary-dark flex items-center justify-center text-white">
                <Palmtree className="w-5 h-5" />
              </div>
              <div className="flex-1">
                <p className="text-xs font-semibold text-gray-900">
                  Solicitações Pendentes
                </p>
                <p className="text-xs text-gray-500">
                  12 folgas aguardando
                </p>
              </div>
            </div>
          </div>
        </aside>

        {/* ========================================
            MAIN CONTENT
            ======================================== */}
          <main
          className="pt-20 lg:pl-72 min-h-screen transition-all duration-300"
        >
          <div className="p-6 lg:p-8 max-w-[1600px] mx-auto">
            <Outlet />
          </div>
        </main>
      </div>
      
      {/* Toaster para notificações */}
      <Toaster />
    </NotificationProvider>
  );
}

export default App;