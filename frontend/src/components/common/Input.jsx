import { AlertCircle } from 'lucide-react';
import { cn } from '../../utils/cn';
import { forwardRef } from 'react';

/**
 * ========================================
 * INPUT PREMIUM - REDE D'OR
 * ========================================
 * 
 * Input reutilizável com validação e estados
 * 
 * @example
 * <Input
 *   label="Nome"
 *   placeholder="Digite seu nome"
 *   error="Campo obrigatório"
 *   required
 * />
 */

export const Input = forwardRef(({
  label,
  error,
  helper,
  required = false,
  leftIcon: LeftIcon,
  rightIcon: RightIcon,
  className,
  containerClassName,
  ...props
}, ref) => {
  return (
    <div className={cn('input-group', containerClassName)}>
      {/* Label */}
      {label && (
        <label className="label">
          {label}
          {required && <span className="text-error ml-1">*</span>}
        </label>
      )}

      {/* Input Container */}
      <div className="relative">
        {/* Left Icon */}
        {LeftIcon && (
          <div className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
            <LeftIcon className="w-5 h-5" />
          </div>
        )}

        {/* Input */}
        <input
          ref={ref}
          className={cn(
            'input',
            LeftIcon && 'pl-12',
            RightIcon && 'pr-12',
            error && 'input-error',
            className
          )}
          {...props}
        />

        {/* Right Icon */}
        {RightIcon && (
          <div className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400">
            <RightIcon className="w-5 h-5" />
          </div>
        )}

        {/* Error Icon */}
        {error && !RightIcon && (
          <div className="absolute right-4 top-1/2 -translate-y-1/2 text-error">
            <AlertCircle className="w-5 h-5" />
          </div>
        )}
      </div>

      {/* Helper/Error Text */}
      {error ? (
        <p className="text-sm text-error mt-1 flex items-center gap-1">
          <AlertCircle className="w-4 h-4" />
          {error}
        </p>
      ) : helper ? (
        <p className="text-sm text-gray-500 mt-1">{helper}</p>
      ) : null}
    </div>
  );
});

Input.displayName = 'Input';