import { Routes, Route, Link, useLocation } from 'react-router-dom'
import Docentes from './pages/Docentes'
import Departamentos from './pages/Departamentos'
import AsuntosPropios from './pages/AsuntosPropios'
import Login from './pages/Login'
import SetupPassword from './pages/SetupPassword'
import Perfil from './pages/Perfil'
import { useAuth } from './context/AuthContext'

export default function App() {
  const { auth, logout } = useAuth()
  const location = useLocation()

  if (location.pathname === '/setup-password') return <SetupPassword />

  if (!auth) return <Login />

  return (
    <>
      <nav>
        <Link to="/">Docentes</Link>
        <Link to="/departamentos">Departamentos</Link>
        <Link to="/asuntos-propios">Asuntos Propios</Link>
        <span className="nav-user">{auth.username} ({auth.role})</span>
        <Link to="/perfil" className="nav-perfil">Mi perfil</Link>
        <button className="nav-logout" onClick={logout}>Cerrar sesión</button>
      </nav>
      <main>
        <Routes>
          <Route path="/" element={<Docentes />} />
          <Route path="/departamentos" element={<Departamentos />} />
          <Route path="/asuntos-propios" element={<AsuntosPropios />} />
          <Route path="/perfil" element={<Perfil />} />
        </Routes>
      </main>
    </>
  )
}
