/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#003DA5',
          50: '#E3F2FD',
          100: '#BBDEFB',
          500: '#003DA5',
          600: '#002D7A',
          700: '#001F5C',
          dark: '#002D7A',
          light: '#E3F2FD',
        },
        secondary: {
          DEFAULT: '#FF8200',
          50: '#FFF3E0',
          100: '#FFE0B2',
          500: '#FF8200',
          600: '#E67300',
          dark: '#E67300',
          light: '#FFF3E0',
        },
        success: {
          DEFAULT: '#4CAF50',
          light: '#E8F5E9',
          dark: '#388E3C',
        },
        error: {
          DEFAULT: '#F44336',
          light: '#FFEBEE',
          dark: '#D32F2F',
        },
        warning: {
          DEFAULT: '#FF9800',
          light: '#FFF3E0',
          dark: '#F57C00',
        },
        info: {
          DEFAULT: '#2196F3',
          light: '#E3F2FD',
          dark: '#1976D2',
        },
      },
      fontFamily: {
        sans: ['Inter', 'Roboto', 'Helvetica Neue', 'Arial', 'sans-serif'],
      },
      boxShadow: {
        card: '0 2px 8px rgba(0, 0, 0, 0.08)',
        hover: '0 4px 12px rgba(0, 61, 165, 0.15)',
      },
    },
  },
  plugins: [],
};