import { api } from '../api/client';

export const complaintService = {
  list: (params = {}) => api.get('/complaints', { params }).then((r) => r.data),
  get: (id) => api.get(`/complaints/${id}`).then((r) => r.data),
  create: (payload) => api.post('/complaints', payload).then((r) => r.data),
  update: (id, payload) => api.put(`/complaints/${id}`, payload).then((r) => r.data),
  remove: (id) => api.delete(`/complaints/${id}`)
};
