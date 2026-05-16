import { ShieldCheck } from 'lucide-react';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: 'admin@cyber.local', password: 'Admin@12345' });
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await login(form);
      navigate('/');
    } catch (err) {
      setError(err.message || 'Unable to sign in');
    }
  };

  return (
    <div className="grid min-h-screen place-items-center bg-slate-100 px-4">
      <form onSubmit={submit} className="w-full max-w-md rounded-lg border border-slate-200 bg-white p-6 shadow-soft">
        <div className="mb-6 flex items-center gap-3">
          <div className="grid h-11 w-11 place-items-center rounded-lg bg-action text-white"><ShieldCheck /></div>
          <div>
            <h1 className="text-xl font-semibold">Cyber Analytics Login</h1>
            <p className="text-sm text-slate-500">Secure complaint operations</p>
          </div>
        </div>
        {error && <p className="mb-3 rounded-md bg-red-50 px-3 py-2 text-sm text-alert">{error}</p>}
        <label className="text-sm font-medium">Email</label>
        <input className="mb-4 mt-1 w-full rounded-md border border-slate-300 px-3 py-2" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <label className="text-sm font-medium">Password</label>
        <input className="mb-5 mt-1 w-full rounded-md border border-slate-300 px-3 py-2" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        <button className="w-full rounded-md bg-action px-4 py-2 font-medium text-white">Sign in</button>
        <p className="mt-4 text-center text-sm text-slate-500">Need an account? <Link className="text-action" to="/register">Register</Link></p>
      </form>
    </div>
  );
}
