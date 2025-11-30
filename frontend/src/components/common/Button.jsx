import { Loader2 } from 'lucide-react';
import { cn } from '../../utils/cn';

/**
 * ========================================
 * BUTTON PREMIUM - REDE D'OR
 * ========================================
 * 
 * Botão reutilizável com variantes e estados
 * 
 * @example
 * <Button variant="primary" size="lg" loading>
 *   Salvar
 * </Button>
 */

export function Button({
  children,
  variant = 'primary',
  size = 'md',
  loading = false,
  disabled = false,
  fullWidth = false,
  leftIcon: LeftIcon,
  rightIcon: RightIcon,
  className,
  ...props
}) {
  // Classes base
  const baseClasses = 'inline-flex items-center justify-center gap-2 font-semibold rounded-lg transition-all duration-300 focus:outline-none focus:ring-4 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed';

  // Variantes
  const variants = {
    primary: 'bg-primary text-white hover:bg-primary-dark hover:shadow-primary focus:ring-primary/30 active:scale-[0.98]',
    secondary: 'bg-secondary text-white hover:bg-secondary-dark hover:shadow-lg focus:ring-secondary/30 active:scale-[0.98]',
    outline: 'border-2 border-primary text-primary bg-white hover:bg-primary hover:text-white focus:ring-primary/30',
    ghost: 'text-gray-700 bg-transparent hover:bg-gray-100 focus:ring-gray-300',
    danger: 'bg-error text-white hover:bg-error-dark hover:shadow-lg focus:ring-error/30 active:scale-[0.98]',
    success: 'bg-success text-white hover:bg-success-dark hover:shadow-lg focus:ring-success/30 active:scale-[0.98]',
  };

  // Tamanhos
  const sizes = {
    sm: 'px-4 py-2 text-sm',
    md: 'px-6 py-3 text-base',
    lg: 'px-8 py-4 text-lg',
  };

  return (
    <button
      disabled={disabled || loading}
      className={cn(
        baseClasses,
        variants[variant],
        sizes[size],
        fullWidth && 'w-full',
        className
      )}
      {...props}
    >
      {loading ? (
        <>
          <Loader2 className="w-5 h-5 animate-spin" />
          <span>Carregando...</span>
        </>
      ) : (
        <>
          {LeftIcon && <LeftIcon className="w-5 h-5" />}
          {children}
          {RightIcon && <RightIcon className="w-5 h-5" />}
        </>
      )}
    </button>
  );
}