import { useQuery } from '@tanstack/react-query';
import { Activity, AlertTriangle, FileText, MapPin } from 'lucide-react';
import { Bar, BarChart, CartesianGrid, Cell, Line, LineChart, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import StatCard from '../components/StatCard';
import { complaintService } from '../services/complaintService';
import { useDashboard } from '../hooks/useDashboard';

const colors = ['#0F766E', '#D97706', '#B91C1C', '#2563EB', '#7C3AED', '#0891B2'];

function ChartCard({ title, subtitle, children }) {
  return (
    <section className="rounded-lg border border-slate-200 bg-white p-4 shadow-soft dark:border-slate-800 dark:bg-slate-900">
      <div className="mb-4">
        <h2 className="font-semibold text-ink dark:text-white">{title}</h2>
        {subtitle && <p className="text-sm text-slate-500 dark:text-slate-400">{subtitle}</p>}
      </div>
      {children}
    </section>
  );
}

function EmptyChart() {
  return <div className="grid h-[280px] place-items-center rounded-md border border-dashed border-slate-200 text-sm text-slate-500 dark:border-slate-800 dark:text-slate-400">No analytics data yet</div>;
}

const tooltipStyle = {
  borderRadius: 8,
  border: '1px solid #CBD5E1',
  boxShadow: '0 10px 30px rgba(15, 23, 42, 0.12)'
};

export default function Dashboard() {
  const { data, isLoading } = useDashboard();
  const { data: complaints } = useQuery({ queryKey: ['complaints', 'dashboard'], queryFn: () => complaintService.list({ size: 5 }) });
  if (isLoading) return <p className="text-slate-600 dark:text-slate-300">Loading dashboard...</p>;

  const hotspotCount = data.hotspots?.length || 0;
  const riskPercent = data.totalComplaints ? Math.round((data.highSeverityComplaints / data.totalComplaints) * 100) : 0;

  return (
    <div className="space-y-5">
      <section className="rounded-lg border border-slate-200 bg-white p-5 shadow-soft dark:border-slate-800 dark:bg-slate-900">
        <div className="flex flex-col justify-between gap-4 lg:flex-row lg:items-center">
          <div>
            <p className="text-sm font-medium text-action dark:text-teal-300">Command center</p>
            <h2 className="mt-1 text-2xl font-semibold text-ink dark:text-white">Cybercrime analytics dashboard</h2>
            <p className="mt-2 max-w-3xl text-sm text-slate-500 dark:text-slate-400">
              Monitor complaint inflow, predicted severity, hotspots, and escalation pressure across active cases.
            </p>
          </div>
          <div className="grid grid-cols-2 gap-3 text-sm sm:flex">
            <div className="rounded-md border border-slate-200 px-3 py-2 dark:border-slate-800">
              <p className="text-slate-500 dark:text-slate-400">Risk ratio</p>
              <p className="font-semibold text-alert dark:text-red-300">{riskPercent}%</p>
            </div>
            <div className="rounded-md border border-slate-200 px-3 py-2 dark:border-slate-800">
              <p className="text-slate-500 dark:text-slate-400">Hotspots</p>
              <p className="font-semibold text-signal dark:text-amber-300">{hotspotCount}</p>
            </div>
          </div>
        </div>
      </section>

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Total complaints" value={data.totalComplaints} icon={FileText} helper="All active reports in the system" />
        <StatCard label="High severity" value={data.highSeverityComplaints} tone="risk" icon={AlertTriangle} helper="High and critical complaints" />
        <StatCard label="Open complaints" value={data.openComplaints} tone="signal" icon={Activity} helper="Submitted or under review" />
        <StatCard label="Hotspot regions" value={hotspotCount} tone="blue" icon={MapPin} helper="Locations with repeated signals" />
      </div>

      <div className="grid gap-4 xl:grid-cols-[1fr_1fr]">
        <ChartCard title="Complaints per category" subtitle="Distribution by taxonomy or predicted category">
          {data.complaintsByCategory?.length ? (
          <ResponsiveContainer width="100%" height={280}>
            <PieChart>
              <Pie data={data.complaintsByCategory} dataKey="value" nameKey="label" innerRadius={58} outerRadius={95} paddingAngle={3}>
                {data.complaintsByCategory.map((_, index) => <Cell key={index} fill={colors[index % colors.length]} />)}
              </Pie>
              <Tooltip contentStyle={tooltipStyle} />
            </PieChart>
          </ResponsiveContainer>
          ) : <EmptyChart />}
        </ChartCard>

        <ChartCard title="Complaints by month" subtitle="Monthly complaint volume">
          {data.complaintsByMonth?.length ? (
          <ResponsiveContainer width="100%" height={280}>
            <BarChart data={data.complaintsByMonth}>
              <CartesianGrid strokeDasharray="3 3" stroke="#CBD5E1" vertical={false} />
              <XAxis dataKey="label" tickLine={false} axisLine={false} />
              <YAxis allowDecimals={false} tickLine={false} axisLine={false} />
              <Tooltip contentStyle={tooltipStyle} />
              <Bar dataKey="value" fill="#0F766E" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
          ) : <EmptyChart />}
        </ChartCard>
      </div>

      <div className="grid gap-4 xl:grid-cols-[1fr_380px]">
        <ChartCard title="Severity trend" subtitle="Complaint count by severity level">
          {data.severityTrends?.length ? (
            <ResponsiveContainer width="100%" height={280}>
              <LineChart data={data.severityTrends}>
                <CartesianGrid strokeDasharray="3 3" stroke="#CBD5E1" vertical={false} />
                <XAxis dataKey="label" tickLine={false} axisLine={false} />
                <YAxis allowDecimals={false} tickLine={false} axisLine={false} />
                <Tooltip contentStyle={tooltipStyle} />
                <Line type="monotone" dataKey="value" stroke="#B91C1C" strokeWidth={3} dot={{ r: 4 }} />
              </LineChart>
            </ResponsiveContainer>
          ) : <EmptyChart />}
        </ChartCard>

        <section className="rounded-lg border border-slate-200 bg-white p-4 shadow-soft dark:border-slate-800 dark:bg-slate-900">
          <h2 className="font-semibold text-ink dark:text-white">Hotspot regions</h2>
          <div className="mt-4 space-y-3">
            {(data.hotspots || []).slice(0, 5).map((hotspot) => (
              <div key={hotspot.label} className="rounded-md border border-slate-200 p-3 dark:border-slate-800">
                <div className="flex items-center justify-between gap-3">
                  <p className="font-medium text-ink dark:text-white">{hotspot.label}</p>
                  <p className="text-sm text-signal dark:text-amber-300">{Math.round(hotspot.riskScore * 100)}%</p>
                </div>
                <div className="mt-2 h-2 rounded-full bg-slate-100 dark:bg-slate-800">
                  <div className="h-2 rounded-full bg-signal" style={{ width: `${Math.min(100, Math.round(hotspot.riskScore * 100))}%` }} />
                </div>
                <p className="mt-2 text-xs text-slate-500 dark:text-slate-400">{hotspot.count} complaints reported</p>
              </div>
            ))}
            {!data.hotspots?.length && <p className="text-sm text-slate-500 dark:text-slate-400">No hotspot signals yet.</p>}
          </div>
        </section>
      </div>

      <section className="rounded-lg border border-slate-200 bg-white shadow-soft dark:border-slate-800 dark:bg-slate-900">
        <div className="border-b border-slate-100 p-4 dark:border-slate-800">
          <h2 className="font-semibold text-ink dark:text-white">Recent complaints</h2>
          <p className="text-sm text-slate-500 dark:text-slate-400">Latest reports with severity and workflow status.</p>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-50 text-xs uppercase text-slate-500 dark:bg-slate-950 dark:text-slate-400">
              <tr>
                <th className="p-3">Title</th>
                <th className="p-3">Category</th>
                <th className="p-3">Severity</th>
                <th className="p-3">Status</th>
              </tr>
            </thead>
            <tbody>
              {complaints?.content?.map((c) => (
                <tr className="border-b border-slate-100 dark:border-slate-800" key={c.id}>
                  <td className="p-3 font-medium text-ink dark:text-white">{c.title}</td>
                  <td className="p-3 text-slate-600 dark:text-slate-300">{c.category || c.prediction?.predictedCategory || 'Pending'}</td>
                  <td className="p-3"><span className="rounded-md bg-red-50 px-2 py-1 text-xs font-medium text-alert dark:bg-red-950/40 dark:text-red-300">{c.severity}</span></td>
                  <td className="p-3 text-slate-600 dark:text-slate-300">{c.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}
