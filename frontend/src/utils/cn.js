import { clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

/**
 * ========================================
 * UTILITÁRIO DE CLASSES CSS
 * ========================================
 * 
 * Combina classes CSS com clsx e resolve conflitos do Tailwind com tailwind-merge
 * 
 * @param {...any} inputs - Classes CSS (strings, objetos, arrays, condicionais)
 * @returns {string} Classes CSS mescladas sem conflitos
 * 
 * @example
 * // Classes simples
 * cn('px-2 py-1', 'bg-blue-500')
 * // → "px-2 py-1 bg-blue-500"
 * 
 * @example
 * // Classes condicionais
 * cn('px-2 py-1', isActive && 'bg-blue-500', 'text-white')
 * // → "px-2 py-1 bg-blue-500 text-white" (se isActive = true)
 * // → "px-2 py-1 text-white" (se isActive = false)
 * 
 * @example
 * // Resolve conflitos (último prevalece)
 * cn('px-2 py-1', 'px-4')
 * // → "py-1 px-4" (px-2 foi removido automaticamente!)
 * 
 * @example
 * // Uso comum em componentes
 * function Button({ variant, className }) {
 *   return (
 *     <button className={cn(
 *       'px-4 py-2 rounded', // classes base
 *       variant === 'primary' && 'bg-blue-500 text-white',
 *       variant === 'secondary' && 'bg-gray-200 text-gray-800',
 *       className // permite sobrescrever
 *     )}>
 *       Botão
 *     </button>
 *   );
 * }
 * 
 * @example
 * // Objetos condicionais
 * cn('base-class', {
 *   'active-class': isActive,
 *   'disabled-class': isDisabled,
 * })
 * 
 * @example
 * // Arrays
 * cn(['class-1', 'class-2'], 'class-3')
 * // → "class-1 class-2 class-3"
 */
export function cn(...inputs) {
  return twMerge(clsx(inputs));
}