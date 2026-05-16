import { useAuth } from '../context/AuthContext';

export default function Profile() {
  const { user } = useAuth();
  return (
    <section className="max-w-2xl rounded-lg border border-slate-200 bg-white p-5 shadow-soft">
      <h2 className="mb-4 text-lg font-semibold">Profile</h2>
      <div className="space-y-3 text-sm">
        <p><strong>Name:</strong> {user?.fullName}</p>
        <p><strong>Email:</strong> {user?.email}</p>
        <p><strong>Phone:</strong> {user?.phone || 'Not provided'}</p>
        <p><strong>Roles:</strong> {user?.roles?.join(', ')}</p>
      </div>
    </section>
  );
}
