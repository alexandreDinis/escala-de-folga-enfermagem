import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-react';
import { cn } from '../../utils/cn';

/**
 * ========================================
 * TABLE PREMIUM - REDE D'OR
 * ========================================
 * 
 * Tabela reutilizável com paginação
 * 
 * @example
 * <Table
 *   columns={[
 *     { key: 'nome', label: 'Nome' },
 *     { key: 'email', label: 'Email' }
 *   ]}
 *   data={usuarios}
 *   onRowClick={(row) => console.log(row)}
 * />
 */

export function Table({
  columns = [],
  data = [],
  loading = false,
  onRowClick,
  emptyMessage = 'Nenhum registro encontrado',
  className,
}) {
  if (loading) {
    return (
      <div className="table-container">
        <div className="flex items-center justify-center py-12">
          <div className="spinner spinner-lg"></div>
        </div>
      </div>
    );
  }

  if (data.length === 0) {
    return (
      <div className="table-container">
        <div className="flex flex-col items-center justify-center py-12 text-gray-500">
          <svg
            className="w-16 h-16 mb-4 text-gray-300"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
            />
          </svg>
          <p className="text-lg font-medium">{emptyMessage}</p>
        </div>
      </div>
    );
  }

  return (
    <div className={cn('table-container', className)}>
      <table className="table">
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.key} className={column.headerClassName}>
                {column.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, rowIndex) => (
            <tr
              key={rowIndex}
              onClick={() => onRowClick?.(row)}
              className={cn(onRowClick && 'cursor-pointer')}
            >
              {columns.map((column) => (
                <td key={column.key} className={column.cellClassName}>
                  {column.render
                    ? column.render(row[column.key], row, rowIndex)
                    : row[column.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

/**
 * ========================================
 * PAGINATION COMPONENT
 * ========================================
 */
export function Pagination({
  currentPage,
  totalPages,
  pageSize,
  totalItems,
  onPageChange,
  onPageSizeChange,
  pageSizeOptions = [10, 20, 50, 100],
}) {
  const canGoBack = currentPage > 0;
  const canGoForward = currentPage < totalPages - 1;

  const startItem = currentPage * pageSize + 1;
  const endItem = Math.min((currentPage + 1) * pageSize, totalItems);

  return (
    <div className="flex items-center justify-between px-6 py-4 border-t border-gray-200 bg-white rounded-b-xl">
      {/* Info */}
      <div className="flex items-center gap-4">
        <p className="text-sm text-gray-700">
          Mostrando <span className="font-semibold">{startItem}</span> até{' '}
          <span className="font-semibold">{endItem}</span> de{' '}
          <span className="font-semibold">{totalItems}</span> resultados
        </p>

        {/* Page Size Selector */}
        <select
          value={pageSize}
          onChange={(e) => onPageSizeChange(Number(e.target.value))}
          className="input py-2 px-3 text-sm w-auto"
        >
          {pageSizeOptions.map((size) => (
            <option key={size} value={size}>
              {size} por página
            </option>
          ))}
        </select>
      </div>

      {/* Buttons */}
      <div className="flex items-center gap-2">
        <button
          onClick={() => onPageChange(0)}
          disabled={!canGoBack}
          className="p-2 rounded-lg hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          aria-label="Primeira página"
        >
          <ChevronsLeft className="w-5 h-5 text-gray-600" />
        </button>

        <button
          onClick={() => onPageChange(currentPage - 1)}
          disabled={!canGoBack}
          className="p-2 rounded-lg hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          aria-label="Página anterior"
        >
          <ChevronLeft className="w-5 h-5 text-gray-600" />
        </button>

        <span className="px-4 py-2 text-sm font-medium text-gray-700">
          Página {currentPage + 1} de {totalPages}
        </span>

        <button
          onClick={() => onPageChange(currentPage + 1)}
          disabled={!canGoForward}
          className="p-2 rounded-lg hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          aria-label="Próxima página"
        >
          <ChevronRight className="w-5 h-5 text-gray-600" />
        </button>

        <button
          onClick={() => onPageChange(totalPages - 1)}
          disabled={!canGoForward}
          className="p-2 rounded-lg hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          aria-label="Última página"
        >
          <ChevronsRight className="w-5 h-5 text-gray-600" />
        </button>
      </div>
    </div>
  );
}