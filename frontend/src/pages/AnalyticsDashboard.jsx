import { Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis, Bar, BarChart } from 'recharts';
import StatCard from '../components/StatCard';
import { useDashboard } from '../hooks/useDashboard';

export default function AnalyticsDashboard() {
  const { data, isLoading } = useDashboard();
  if (isLoading) return <p>Loading analytics...</p>;
  return (
    <div className="space-y-5">
      <div className="grid gap-4 md:grid-cols-3">
        <StatCard label="Prediction models online" value="1" />
        <StatCard label="Hotspot regions" value={data.hotspots.length} tone="signal" />
        <StatCard label="Escalation candidates" value={data.highSeverityComplaints} tone="risk" />
      </div>
      <section className="rounded-lg border border-slate-200 bg-white p-4 shadow-soft">
        <h2 className="mb-4 font-semibold">Severity trends</h2>
        <ResponsiveContainer width="100%" height={320}>
          <LineChart data={data.severityTrends}>
            <XAxis dataKey="label" /><YAxis allowDecimals={false} /><Tooltip /><Line type="monotone" dataKey="value" stroke="#B91C1C" strokeWidth={3} />
          </LineChart>
        </ResponsiveContainer>
      </section>
      <section className="rounded-lg border border-slate-200 bg-white p-4 shadow-soft">
        <h2 className="mb-4 font-semibold">Hotspot risk score</h2>
        <ResponsiveContainer width="100%" height={320}>
          <BarChart data={data.hotspots}>
            <XAxis dataKey="label" /><YAxis /><Tooltip /><Bar dataKey="riskScore" fill="#D97706" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </section>
    </div>
  );
}
