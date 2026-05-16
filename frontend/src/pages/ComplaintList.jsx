import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { complaintService } from '../services/complaintService';

export default function ComplaintList() {
  const { data, isLoading } = useQuery({ queryKey: ['complaints'], queryFn: () => complaintService.list({ size: 25, sort: 'createdAt,desc' }) });
  if (isLoading) return <p>Loading complaints...</p>;
  return (
    <section className="rounded-lg border border-slate-200 bg-white shadow-soft">
      <div className="flex items-center justify-between border-b border-slate-100 p-4">
        <h2 className="font-semibold">Complaint Register</h2>
        <Link to="/complaints/new" className="rounded-md bg-action px-3 py-2 text-sm font-medium text-white">New complaint</Link>
      </div>
      <div className="overflow-x-auto">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-50 text-slate-500"><tr><th className="p-3">Title</th><th>Category</th><th>Severity</th><th>Status</th><th>Location</th></tr></thead>
          <tbody>
            {data?.content?.map((c) => (
              <tr className="border-t border-slate-100" key={c.id}>
                <td className="p-3 font-medium"><Link className="text-action" to={`/complaints/${c.id}`}>{c.title}</Link></td>
                <td>{c.category || c.prediction?.predictedCategory}</td>
                <td>{c.severity}</td>
                <td>{c.status}</td>
                <td>{c.city}, {c.state}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
