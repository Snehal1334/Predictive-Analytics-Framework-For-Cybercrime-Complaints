import { useQuery } from '@tanstack/react-query';
import { analyticsService } from '../services/analyticsService';

export function useDashboard() {
  return useQuery({ queryKey: ['dashboard'], queryFn: analyticsService.dashboard });
}
