import { AlertCircle } from 'lucide-react';
import { cn } from '../../utils/cn';
import { forwardRef } from 'react';

/**
 * ========================================
 * SELECT PREMIUM - REDE D'OR
 * ========================================
 * 
 * Select reutilizável com validação
 * Aceita tanto options (array) quanto children (elementos <option>)
 * 
 * @example
 * // Com options
 * <Select
 *   label="Turno"
 *   options={[
 *     { value: 'MANHA', label: 'Manhã' },
 *     { value: 'TARDE', label: 'Tarde' }
 *   ]}
 * />
 * 
 * // Com children
 * <Select label="Setor">
 *   <option value="">Selecione...</option>
 *   <option value="1">UTI</option>
 *   <option value="2">Emergência</option>
 * </Select>
 */

export const Select = forwardRef(({
  label,
  options = [],
  children,
  error,
  helper,
  required = false,
  placeholder = 'Selecione...',
  leftIcon: LeftIcon,
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
        {/* Left Icon */}
        {LeftIcon && (
          <div className="absolute left-3 top-1/2 -translate-y-1/2 pointer-events-none z-10">
            <LeftIcon className="w-5 h-5 text-gray-400" />
          </div>
        )}

        <select
          ref={ref}
          className={cn(
            'select',
            LeftIcon && 'pl-11',
            error && 'input-error',
            className
          )}
          {...props}
        >
          {/* Se tiver children, usa children */}
          {children ? (
            children
          ) : (
            /* Senão, usa options */
            <>
              <option value="" disabled>
                {placeholder}
              </option>
              {options.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </>
          )}
        </select>

        {/* Error Icon */}
        {error && (
          <div className="absolute right-12 top-1/2 -translate-y-1/2 text-error pointer-events-none">
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
