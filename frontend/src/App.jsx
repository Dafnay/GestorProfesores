import { useState } from 'react'
import { Routes, Route, NavLink, useLocation } from 'react-router-dom'
import Docentes from './pages/Docentes'
import Departamentos from './pages/Departamentos'
import AsuntosPropios from './pages/AsuntosPropios'
import Login from './pages/Login'
import SetupPassword from './pages/SetupPassword'
import ForgotPassword from './pages/ForgotPassword'
import ResetPassword from './pages/ResetPassword'
import Perfil from './pages/Perfil'
import { useAuth } from './context/AuthContext'
import logo from './assets/images/icon_light.png'

function IconDocentes() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
      <circle cx="9" cy="7" r="4"/>
      <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
      <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
    </svg>
  )
}

function IconDepartamentos() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="2" y="7" width="20" height="14" rx="2"/>
      <path d="M16 7V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v2"/>
      <line x1="12" y1="12" x2="12" y2="16"/>
      <line x1="10" y1="14" x2="14" y2="14"/>
    </svg>
  )
}

function IconAsuntos() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="4" width="18" height="18" rx="2"/>
      <line x1="16" y1="2" x2="16" y2="6"/>
      <line x1="8" y1="2" x2="8" y2="6"/>
      <line x1="3" y1="10" x2="21" y2="10"/>
    </svg>
  )
}

function IconPerfil() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
      <circle cx="12" cy="7" r="4"/>
    </svg>
  )
}

function IconLogout() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
      <polyline points="16 17 21 12 16 7"/>
      <line x1="21" y1="12" x2="9" y2="12"/>
    </svg>
  )
}

function IconCollapse({ collapsed }) {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
      style={{ transition: 'transform 0.3s', transform: collapsed ? 'rotate(180deg)' : 'rotate(0deg)' }}>
      <polyline points="15 18 9 12 15 6"/>
    </svg>
  )
}

export default function App() {
  const { auth, logout } = useAuth()
  const location = useLocation()
  const [collapsed, setCollapsed] = useState(false)

  if (location.pathname === '/setup-password') return <SetupPassword />
  if (location.pathname === '/forgot-password') return <ForgotPassword />
  if (location.pathname === '/reset-password') return <ResetPassword />

  if (!auth) return <Login />

  const displayName = auth.nombre ? `${auth.nombre} ${auth.apellidos}` : auth.username

  return (
    <div className={`app-layout${collapsed ? ' app-layout--collapsed' : ''}`}>

      <button
        className="sidebar__toggle-float"
        onClick={() => setCollapsed(c => !c)}
        title={collapsed ? 'Expandir' : 'Colapsar'}
      >
        <IconCollapse collapsed={collapsed} />
      </button>

      <aside className={`sidebar${collapsed ? ' sidebar--collapsed' : ''}`}>

        <div className="sidebar__header">
          <img src={logo} alt="Logo" className="sidebar__logo-icon" />
          {!collapsed && (
            <div className="sidebar__logo">
              <span>Gestor de</span>
              <strong>Profesores</strong>
            </div>
          )}
        </div>

        <nav className="sidebar__nav">
          <NavLink to="/" end className={({ isActive }) => 'sidebar__link' + (isActive ? ' sidebar__link--active' : '')} title="Docentes">
            <span className="sidebar__icon"><IconDocentes /></span>
            <span className="sidebar__label">Docentes</span>
          </NavLink>
          <NavLink to="/departamentos" className={({ isActive }) => 'sidebar__link' + (isActive ? ' sidebar__link--active' : '')} title="Departamentos">
            <span className="sidebar__icon"><IconDepartamentos /></span>
            <span className="sidebar__label">Departamentos</span>
          </NavLink>
          <NavLink to="/asuntos-propios" className={({ isActive }) => 'sidebar__link' + (isActive ? ' sidebar__link--active' : '')} title="Asuntos Propios">
            <span className="sidebar__icon"><IconAsuntos /></span>
            <span className="sidebar__label">Asuntos Propios</span>
          </NavLink>
        </nav>

        <div className="sidebar__footer">
          {!collapsed && (
            <div className="sidebar__user-info">
              <span className="sidebar__user-name">{displayName}</span>
              <span className="sidebar__user-role">{auth.role}</span>
            </div>
          )}
          <NavLink to="/perfil" className={({ isActive }) => 'sidebar__link' + (isActive ? ' sidebar__link--active' : '')} title="Mi perfil">
            <span className="sidebar__icon"><IconPerfil /></span>
            <span className="sidebar__label">Mi perfil</span>
          </NavLink>
          <button className="sidebar__logout" onClick={logout} title="Cerrar sesión">
            <span className="sidebar__icon"><IconLogout /></span>
            <span className="sidebar__label">Cerrar sesión</span>
          </button>
        </div>

      </aside>

      <main className="main-content">
        <Routes>
          <Route path="/" element={<Docentes />} />
          <Route path="/departamentos" element={<Departamentos />} />
          <Route path="/asuntos-propios" element={<AsuntosPropios />} />
          <Route path="/perfil" element={<Perfil />} />
        </Routes>
      </main>

    </div>
  )
}
