import { AlertCircle, ChevronDown } from 'lucide-react';
import { cn } from '../../utils/cn';
import { forwardRef } from 'react';

/**
 * ========================================
 * SELECT PREMIUM - REDE D'OR
 * ========================================
 * 
 * Select reutilizável com validação
 * 
 * @example
 * <Select
 *   label="Turno"
 *   options={[
 *     { value: 'MANHA', label: 'Manhã' },
 *     { value: 'TARDE', label: 'Tarde' }
 *   ]}
 *   error="Campo obrigatório"
 * />
 */

export const Select = forwardRef(({
  label,
  options = [],
  error,
  helper,
  required = false,
  placeholder = 'Selecione...',
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

      {/* Select Container */}
      <div className="relative">
        <select
          ref={ref}
          className={cn(
            'select',
            error && 'input-error',
            className
          )}
          {...props}
        >
          <option value="" disabled>
            {placeholder}
          </option>
          {options.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>

        {/* Error Icon */}
        {error && (
          <div className="absolute right-12 top-1/2 -translate-y-1/2 text-error">
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

Select.displayName = 'Select';