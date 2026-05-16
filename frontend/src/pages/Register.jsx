import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: '', email: '', phone: '', password: '' });
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    try {
      await register(form);
      navigate('/');
    } catch (err) {
      setError(err.message || 'Registration failed');
    }
  };

  return (
    <div className="grid min-h-screen place-items-center bg-slate-100 px-4">
      <form onSubmit={submit} className="w-full max-w-lg rounded-lg border border-slate-200 bg-white p-6 shadow-soft">
        <h1 className="text-xl font-semibold">Citizen Registration</h1>
        <p className="mb-5 text-sm text-slate-500">Create a secure account to submit and track complaints.</p>
        {error && <p className="mb-3 rounded-md bg-red-50 px-3 py-2 text-sm text-alert">{error}</p>}
        {['fullName', 'email', 'phone', 'password'].map((field) => (
          <div className="mb-4" key={field}>
            <label className="text-sm font-medium capitalize">{field.replace('fullName', 'full name')}</label>
            <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" type={field === 'password' ? 'password' : 'text'} value={form[field]} onChange={(e) => setForm({ ...form, [field]: e.target.value })} />
          </div>
        ))}
        <button className="w-full rounded-md bg-action px-4 py-2 font-medium text-white">Create account</button>
        <p className="mt-4 text-center text-sm text-slate-500">Already registered? <Link className="text-action" to="/login">Sign in</Link></p>
      </form>
    </div>
  );
}
