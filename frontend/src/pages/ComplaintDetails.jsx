import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import ComplaintMap from '../components/ComplaintMap';
import { complaintService } from '../services/complaintService';

export default function ComplaintDetails() {
  const { id } = useParams();
  const { data, isLoading } = useQuery({ queryKey: ['complaint', id], queryFn: () => complaintService.get(id) });
  if (isLoading) return <p>Loading complaint...</p>;
  return (
    <div className="grid gap-5 lg:grid-cols-[1fr_420px]">
      <section className="rounded-lg border border-slate-200 bg-white p-5 shadow-soft">
        <div className="mb-4 flex items-start justify-between gap-4">
          <div>
            <h2 className="text-xl font-semibold">{data.title}</h2>
            <p className="text-sm text-slate-500">{data.category || data.prediction?.predictedCategory} · {data.status}</p>
          </div>
          <span className="rounded-md bg-amber-50 px-3 py-1 text-sm font-medium text-signal">{data.severity}</span>
        </div>
        <p className="whitespace-pre-wrap text-slate-700">{data.description}</p>
        <div className="mt-6 grid gap-3 text-sm md:grid-cols-2">
          <p><strong>Incident date:</strong> {data.incidentDate}</p>
          <p><strong>Address:</strong> {data.address}</p>
          <p><strong>Submitted by:</strong> {data.createdBy?.fullName}</p>
          <p><strong>Escalation risk:</strong> {Math.round((data.prediction?.escalationRisk || 0) * 100)}%</p>
        </div>
      </section>
      <ComplaintMap complaints={[data]} hotspots={data.prediction ? [{ label: data.prediction.hotspotLabel, latitude: data.latitude, longitude: data.longitude, count: 1, riskScore: data.prediction.escalationRisk }] : []} />
    </div>
  );
}
