/**
 * ========================================
 * SKELETON LOADER - TABELA
 * ========================================
 */

const SkeletonRow = () => (
  <tr className="animate-pulse">
    {/* Coluna Nome */}
    <td className="px-6 py-4">
      <div className="flex items-center gap-3">
        <div className="w-8 h-8 bg-gray-200 rounded-lg"></div>
        <div className="h-4 bg-gray-200 rounded w-40"></div>
      </div>
    </td>
    
    {/* Coluna Cargo */}
    <td className="px-6 py-4">
      <div className="flex justify-center">
        <div className="h-6 bg-gray-200 rounded-full w-24"></div>
      </div>
    </td>

    {/* Coluna Turno */}
    <td className="px-6 py-4">
      <div className="flex justify-center">
        <div className="h-6 bg-gray-200 rounded-full w-20"></div>
      </div>
    </td>
    
    {/* Coluna Setor */}
    <td className="px-6 py-4">
      <div className="flex justify-center">
        <div className="h-4 bg-gray-200 rounded w-16"></div>
      </div>
    </td>
    
    {/* Coluna Ações */}
    <td className="px-6 py-4">
      <div className="flex items-center justify-end gap-2">
        <div className="w-8 h-8 bg-gray-200 rounded-lg"></div>
        <div className="w-8 h-8 bg-gray-200 rounded-lg"></div>
      </div>
    </td>
  </tr>
);

export default function SkeletonLoader({ rows = 5 }) {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3.5 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider w-2/5">
              Nome
            </th>
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Cargo
            </th>
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Turno
            </th>
            <th className="px-6 py-3.5 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Setor
            </th>
            <th className="px-6 py-3.5 text-right text-xs font-semibold text-gray-700 uppercase tracking-wider w-1/6">
              Ações
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-100">
          {Array.from({ length: rows }).map((_, index) => (
            <SkeletonRow key={index} />
          ))}
        </tbody>
      </table>
    </div>
  );
}
