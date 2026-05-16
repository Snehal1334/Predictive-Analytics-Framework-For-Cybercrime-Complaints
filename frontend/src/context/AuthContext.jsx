import { createContext, useContext, useMemo, useState } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem('user') || 'null'));

  const saveSession = (session) => {
    localStorage.setItem('accessToken', session.accessToken);
    localStorage.setItem('refreshToken', session.refreshToken);
    localStorage.setItem('user', JSON.stringify(session.user));
    setUser(session.user);
  };

  const value = useMemo(() => ({
    user,
    isAuthenticated: Boolean(user),
    login: async (payload) => saveSession(await authService.login(payload)),
    register: async (payload) => saveSession(await authService.register(payload)),
    logout: () => {
      localStorage.clear();
      setUser(null);
    }
  }), [user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
