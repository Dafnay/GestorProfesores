import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => {
    const token = localStorage.getItem('token')
    const username = localStorage.getItem('username')
    const role = localStorage.getItem('role')
    const nombre = localStorage.getItem('nombre')
    const apellidos = localStorage.getItem('apellidos')
    return token ? { token, username, role, nombre, apellidos } : null
  })

  function login(token, username, role, nombre, apellidos) {
    localStorage.setItem('token', token)
    localStorage.setItem('username', username)
    localStorage.setItem('role', role)
    if (nombre) localStorage.setItem('nombre', nombre)
    if (apellidos) localStorage.setItem('apellidos', apellidos)
    setAuth({ token, username, role, nombre, apellidos })
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
    localStorage.removeItem('nombre')
    localStorage.removeItem('apellidos')
    setAuth(null)
  }

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
