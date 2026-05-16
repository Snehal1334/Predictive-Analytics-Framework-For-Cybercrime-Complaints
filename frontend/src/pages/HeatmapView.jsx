import { useQuery } from '@tanstack/react-query';
import ComplaintMap from '../components/ComplaintMap';
import { complaintService } from '../services/complaintService';
import { useDashboard } from '../hooks/useDashboard';

export default function HeatmapView() {
  const { data: dashboard } = useDashboard();
  const { data: complaints } = useQuery({ queryKey: ['complaints', 'map'], queryFn: () => complaintService.list({ size: 200 }) });
  return (
    <div className="space-y-4">
      <div>
        <h2 className="text-lg font-semibold">Complaint Heatmap</h2>
        <p className="text-sm text-slate-500">Markers show complaint locations; circles show hotspot intensity.</p>
      </div>
      <ComplaintMap complaints={complaints?.content || []} hotspots={dashboard?.hotspots || []} />
    </div>
  );
}
