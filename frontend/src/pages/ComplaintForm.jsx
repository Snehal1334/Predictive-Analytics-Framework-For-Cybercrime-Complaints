import { useMutation, useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { analyticsService } from '../services/analyticsService';
import { complaintService } from '../services/complaintService';

export default function ComplaintForm() {
  const navigate = useNavigate();
  const { data: categories = [] } = useQuery({ queryKey: ['categories'], queryFn: analyticsService.categories });
  const [form, setForm] = useState({ title: '', description: '', categoryId: '', incidentDate: new Date().toISOString().slice(0, 10), address: '', city: '', state: '', postalCode: '', latitude: 28.6139, longitude: 77.209, severity: 'MEDIUM', attachments: '' });
  const mutation = useMutation({ mutationFn: complaintService.create, onSuccess: (data) => navigate(`/complaints/${data.id}`) });
  const set = (key, value) => setForm((current) => ({ ...current, [key]: value }));

  return (
    <form onSubmit={(e) => { e.preventDefault(); mutation.mutate({ ...form, categoryId: form.categoryId || null, latitude: Number(form.latitude), longitude: Number(form.longitude) }); }} className="mx-auto max-w-4xl rounded-lg border border-slate-200 bg-white p-5 shadow-soft">
      <h2 className="mb-5 text-lg font-semibold">Submit Cybercrime Complaint</h2>
      {mutation.error && <p className="mb-4 rounded-md bg-red-50 px-3 py-2 text-sm text-alert">{mutation.error.message}</p>}
      <div className="grid gap-4 md:grid-cols-2">
        <input className="rounded-md border border-slate-300 px-3 py-2 md:col-span-2" placeholder="Title" value={form.title} onChange={(e) => set('title', e.target.value)} />
        <textarea className="min-h-36 rounded-md border border-slate-300 px-3 py-2 md:col-span-2" placeholder="Description" value={form.description} onChange={(e) => set('description', e.target.value)} />
        <select className="rounded-md border border-slate-300 px-3 py-2" value={form.categoryId} onChange={(e) => set('categoryId', e.target.value)}>
          <option value="">Auto classify</option>
          {categories.map((c) => <option value={c.id} key={c.id}>{c.name}</option>)}
        </select>
        <input className="rounded-md border border-slate-300 px-3 py-2" type="date" value={form.incidentDate} onChange={(e) => set('incidentDate', e.target.value)} />
        <input className="rounded-md border border-slate-300 px-3 py-2 md:col-span-2" placeholder="Address" value={form.address} onChange={(e) => set('address', e.target.value)} />
        <input className="rounded-md border border-slate-300 px-3 py-2" placeholder="City" value={form.city} onChange={(e) => set('city', e.target.value)} />
        <input className="rounded-md border border-slate-300 px-3 py-2" placeholder="State" value={form.state} onChange={(e) => set('state', e.target.value)} />
        <input className="rounded-md border border-slate-300 px-3 py-2" placeholder="Latitude" value={form.latitude} onChange={(e) => set('latitude', e.target.value)} />
        <input className="rounded-md border border-slate-300 px-3 py-2" placeholder="Longitude" value={form.longitude} onChange={(e) => set('longitude', e.target.value)} />
      </div>
      <button className="mt-5 rounded-md bg-action px-4 py-2 font-medium text-white" disabled={mutation.isPending}>{mutation.isPending ? 'Submitting...' : 'Submit complaint'}</button>
    </form>
  );
}
