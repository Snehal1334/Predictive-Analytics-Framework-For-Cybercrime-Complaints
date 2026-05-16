import { BarChart3, FilePlus2, ListChecks, LogOut, Map, Menu, Moon, ShieldCheck, Sun, UserCircle, X } from 'lucide-react';
import { useEffect, useState } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const nav = [
  ['Dashboard', '/', BarChart3],
  ['Complaints', '/complaints', ListChecks],
  ['New', '/complaints/new', FilePlus2],
  ['Analytics', '/analytics', ShieldCheck],
  ['Heatmap', '/heatmap', Map],
  ['Admin', '/admin', UserCircle],
  ['Profile', '/profile', UserCircle]
];

export default function AppLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [darkMode, setDarkMode] = useState(() => localStorage.getItem('theme') === 'dark');

  useEffect(() => {
    document.documentElement.classList.toggle('dark', darkMode);
    localStorage.setItem('theme', darkMode ? 'dark' : 'light');
  }, [darkMode]);

  const linkClass = ({ isActive }) => `flex items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium transition-colors ${
    isActive
      ? 'bg-teal-50 text-action dark:bg-teal-400/10 dark:text-teal-300'
      : 'text-slate-600 hover:bg-slate-50 hover:text-ink dark:text-slate-300 dark:hover:bg-slate-800 dark:hover:text-white'
  }`;

  const Sidebar = () => (
    <div className="flex h-full flex-col">
      <div className="mb-8 flex items-center gap-3">
        <div className="grid h-10 w-10 place-items-center rounded-lg bg-action text-white shadow-soft">
          <ShieldCheck size={22} />
        </div>
        <div>
          <p className="font-semibold leading-tight text-ink dark:text-white">Cyber Analytics</p>
          <p className="text-xs text-slate-500 dark:text-slate-400">Predictive complaints</p>
        </div>
      </div>
      <nav className="space-y-1">
        {nav.map(([label, path, Icon]) => (
          <NavLink key={path} to={path} onClick={() => setSidebarOpen(false)} className={linkClass}>
            <Icon size={18} />
            {label}
          </NavLink>
        ))}
      </nav>
      <div className="mt-auto rounded-lg border border-slate-200 bg-slate-50 p-3 text-xs text-slate-500 dark:border-slate-800 dark:bg-slate-950 dark:text-slate-400">
        <p className="font-medium text-slate-700 dark:text-slate-200">Role</p>
        <p>{user?.roles?.join(', ') || 'USER'}</p>
      </div>
    </div>
  );

  return (
    <div className="min-h-screen bg-slate-100 text-ink transition-colors dark:bg-slate-950 dark:text-slate-100">
      <aside className="fixed inset-y-0 left-0 hidden w-64 border-r border-slate-200 bg-white px-4 py-5 dark:border-slate-800 dark:bg-slate-900 lg:block">
        <Sidebar />
      </aside>
      {sidebarOpen && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <button className="absolute inset-0 bg-slate-950/50" aria-label="Close sidebar" onClick={() => setSidebarOpen(false)} />
          <aside className="relative h-full w-72 border-r border-slate-200 bg-white px-4 py-5 dark:border-slate-800 dark:bg-slate-900">
            <button aria-label="Close sidebar" className="absolute right-4 top-4 rounded-md border border-slate-200 p-2 dark:border-slate-700" onClick={() => setSidebarOpen(false)}>
              <X size={18} />
            </button>
            <Sidebar />
          </aside>
        </div>
      )}
      <main className="lg:pl-64">
        <header className="sticky top-0 z-20 flex min-h-16 items-center justify-between gap-4 border-b border-slate-200 bg-white/90 px-4 backdrop-blur dark:border-slate-800 dark:bg-slate-900/90 sm:px-5">
          <div className="flex items-center gap-3">
            <button aria-label="Open sidebar" className="rounded-md border border-slate-200 p-2 hover:bg-slate-50 dark:border-slate-700 dark:hover:bg-slate-800 lg:hidden" onClick={() => setSidebarOpen(true)}>
              <Menu size={18} />
            </button>
            <div>
              <p className="text-xs text-slate-500 dark:text-slate-400 sm:text-sm">Predictive Analytics Framework</p>
              <h1 className="text-sm font-semibold text-ink dark:text-white sm:text-base">Cybercrime Complaint Operations</h1>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <button aria-label="Toggle dark mode" className="rounded-md border border-slate-200 p-2 hover:bg-slate-50 dark:border-slate-700 dark:hover:bg-slate-800" onClick={() => setDarkMode((value) => !value)}>
              {darkMode ? <Sun size={18} /> : <Moon size={18} />}
            </button>
            <span className="hidden text-sm text-slate-600 dark:text-slate-300 sm:block">{user?.fullName}</span>
            <button aria-label="Logout" className="rounded-md border border-slate-200 p-2 hover:bg-slate-50 dark:border-slate-700 dark:hover:bg-slate-800" onClick={() => { logout(); navigate('/login'); }}>
              <LogOut size={18} />
            </button>
          </div>
        </header>
        <div className="p-4 sm:p-5">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
