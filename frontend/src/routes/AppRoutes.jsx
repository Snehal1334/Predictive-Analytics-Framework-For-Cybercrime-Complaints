import { Navigate, Route, Routes } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import AppLayout from '../layouts/AppLayout';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Dashboard from '../pages/Dashboard';
import ComplaintForm from '../pages/ComplaintForm';
import ComplaintList from '../pages/ComplaintList';
import ComplaintDetails from '../pages/ComplaintDetails';
import AnalyticsDashboard from '../pages/AnalyticsDashboard';
import AdminPanel from '../pages/AdminPanel';
import HeatmapView from '../pages/HeatmapView';
import Profile from '../pages/Profile';

function Protected({ children }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" replace />;
}

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/" element={<Protected><AppLayout /></Protected>}>
        <Route index element={<Dashboard />} />
        <Route path="complaints" element={<ComplaintList />} />
        <Route path="complaints/new" element={<ComplaintForm />} />
        <Route path="complaints/:id" element={<ComplaintDetails />} />
        <Route path="analytics" element={<AnalyticsDashboard />} />
        <Route path="admin" element={<AdminPanel />} />
        <Route path="heatmap" element={<HeatmapView />} />
        <Route path="profile" element={<Profile />} />
      </Route>
    </Routes>
  );
}
