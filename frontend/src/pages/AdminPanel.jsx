import { useQuery } from '@tanstack/react-query';
import { analyticsService } from '../services/analyticsService';

export default function AdminPanel() {
  const { data = [] } = useQuery({ queryKey: ['categories'], queryFn: analyticsService.categories });
  return (
    <section className="rounded-lg border border-slate-200 bg-white shadow-soft">
      <div className="border-b border-slate-100 p-4">
        <h2 className="font-semibold">Admin Panel</h2>
        <p className="text-sm text-slate-500">Seeded roles, categories, and operating taxonomy.</p>
      </div>
      <table className="w-full text-left text-sm">
        <thead className="bg-slate-50 text-slate-500"><tr><th className="p-3">Category</th><th>Description</th></tr></thead>
        <tbody>{data.map((c) => <tr key={c.id} className="border-t border-slate-100"><td className="p-3 font-medium">{c.name}</td><td>{c.description}</td></tr>)}</tbody>
      </table>
    </section>
  );
}
