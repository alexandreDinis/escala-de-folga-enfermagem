/**
 * ========================================
 * SKELETON LOADER
 * ========================================
 * 
 * Componente de loading com animação
 * Usado enquanto dados estão carregando
 */

const SkeletonRow = () => (
  <tr className="animate-pulse">
    <td className="px-6 py-4">
      <div className="h-4 bg-gray-200 rounded w-8"></div>
    </td>
    <td className="px-6 py-4">
      <div className="h-4 bg-gray-200 rounded w-32"></div>
    </td>
    <td className="px-6 py-4">
      <div className="h-4 bg-gray-200 rounded w-20"></div>
    </td>
    <td className="px-6 py-4">
      <div className="flex gap-2">
        <div className="h-8 w-8 bg-gray-200 rounded"></div>
        <div className="h-8 w-8 bg-gray-200 rounded"></div>
      </div>
    </td>
  </tr>
);

export const SkeletonLoader = ({ rows = 5, columns = 4 }) => (
  <>
    {Array.from({ length: rows }).map((_, index) => (
      <SkeletonRow key={index} />
    ))}
  </>
);

export default SkeletonLoader;
