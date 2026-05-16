export default function StatCard({ label, value, tone = 'default', icon: Icon, helper }) {
  const tones = {
    default: 'border-slate-200 bg-white text-action dark:border-slate-800 dark:bg-slate-900 dark:text-teal-300',
    risk: 'border-red-200 bg-red-50 text-alert dark:border-red-950 dark:bg-red-950/30 dark:text-red-300',
    signal: 'border-amber-200 bg-amber-50 text-signal dark:border-amber-900 dark:bg-amber-950/30 dark:text-amber-300',
    blue: 'border-blue-200 bg-blue-50 text-blue-700 dark:border-blue-950 dark:bg-blue-950/30 dark:text-blue-300'
  };
  return (
    <div className={`rounded-lg border p-4 shadow-soft transition-colors ${tones[tone]}`}>
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm text-slate-500 dark:text-slate-400">{label}</p>
          <p className="mt-2 text-3xl font-semibold text-ink dark:text-white">{value}</p>
        </div>
        {Icon && (
          <div className="grid h-10 w-10 shrink-0 place-items-center rounded-md bg-white/70 dark:bg-slate-950/50">
            <Icon size={20} />
          </div>
        )}
      </div>
      {helper && <p className="mt-3 text-xs text-slate-500 dark:text-slate-400">{helper}</p>}
    </div>
  );
}
