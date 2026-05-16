import { api } from '../api/client';

export const analyticsService = {
  dashboard: () => api.get('/analytics/dashboard').then((r) => r.data),
  categories: () => api.get('/admin/categories').then((r) => r.data)
};
