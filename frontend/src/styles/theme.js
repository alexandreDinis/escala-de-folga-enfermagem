/**
 * 🏥 Design System - Rede D'Or São Luiz
 * 
 * Paleta de cores e tokens de design baseados no site oficial:
 * https://www.rededorsaoluiz.com.br
 */

export const theme = {
  colors: {
    // Cores Primárias
    primary: {
      main: '#003DA5',      // Azul Institucional
      dark: '#002D7A',      // Azul Escuro (hover)
      light: '#E3F2FD',     // Azul Claro (backgrounds)
      50: '#E3F2FD',
      100: '#BBDEFB',
      500: '#003DA5',
      600: '#002D7A',
      700: '#001F5C',
    },
    
    // Cores Secundárias
    secondary: {
      main: '#FF8200',      // Laranja/Dourado
      dark: '#E67300',      // Laranja Escuro (hover)
      light: '#FFF3E0',     // Laranja Claro (backgrounds)
      50: '#FFF3E0',
      100: '#FFE0B2',
      500: '#FF8200',
      600: '#E67300',
    },
    
    // Tons de Cinza
    gray: {
      50: '#FAFAFA',
      100: '#F5F5F5',
      200: '#EEEEEE',
      300: '#E0E0E0',
      400: '#BDBDBD',
      500: '#9E9E9E',
      600: '#757575',
      700: '#616161',
      800: '#424242',
      900: '#212121',
    },
    
    // Cores de Texto
    text: {
      primary: '#333333',
      secondary: '#666666',
      disabled: '#9E9E9E',
      white: '#FFFFFF',
    },
    
    // Cores de Feedback
    success: {
      main: '#4CAF50',
      light: '#E8F5E9',
      dark: '#388E3C',
    },
    error: {
      main: '#F44336',
      light: '#FFEBEE',
      dark: '#D32F2F',
    },
    warning: {
      main: '#FF9800',
      light: '#FFF3E0',
      dark: '#F57C00',
    },
    info: {
      main: '#2196F3',
      light: '#E3F2FD',
      dark: '#1976D2',
    },
    
    // Cores de Background
    background: {
      default: '#FAFAFA',
      paper: '#FFFFFF',
      hover: '#F5F5F5',
    },
    
    // Cores de Borda
    border: {
      main: '#E0E0E0',
      light: '#EEEEEE',
      dark: '#BDBDBD',
    },
  },
  
  // Sombras
  shadows: {
    sm: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
    md: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
    lg: '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)',
    xl: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
    card: '0 2px 8px rgba(0, 0, 0, 0.08)',
    hover: '0 4px 12px rgba(0, 61, 165, 0.15)',
  },
  
  // Bordas Arredondadas
  borderRadius: {
    none: '0',
    sm: '0.25rem',    // 4px
    md: '0.5rem',     // 8px
    lg: '0.75rem',    // 12px
    xl: '1rem',       // 16px
    full: '9999px',
  },
  
  // Espaçamentos
  spacing: {
    xs: '0.25rem',    // 4px
    sm: '0.5rem',     // 8px
    md: '1rem',       // 16px
    lg: '1.5rem',     // 24px
    xl: '2rem',       // 32px
    '2xl': '3rem',    // 48px
    '3xl': '4rem',    // 64px
  },
  
  // Tipografia
  typography: {
    fontFamily: {
      sans: "'Inter', 'Roboto', 'Helvetica Neue', 'Arial', sans-serif",
      mono: "'Fira Code', 'Courier New', monospace",
    },
    fontSize: {
      xs: '0.75rem',    // 12px
      sm: '0.875rem',   // 14px
      base: '1rem',     // 16px
      lg: '1.125rem',   // 18px
      xl: '1.25rem',    // 20px
      '2xl': '1.5rem',  // 24px
      '3xl': '1.875rem',// 30px
      '4xl': '2.25rem', // 36px
    },
    fontWeight: {
      normal: 400,
      medium: 500,
      semibold: 600,
      bold: 700,
    },
  },
  
  // Transições
  transitions: {
    fast: '150ms ease-in-out',
    normal: '200ms ease-in-out',
    slow: '300ms ease-in-out',
  },
  
  // Breakpoints
  breakpoints: {
    sm: '640px',
    md: '768px',
    lg: '1024px',
    xl: '1280px',
    '2xl': '1536px',
  },
};

export default theme;
