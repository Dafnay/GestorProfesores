import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi } from '../api/client'
import PasswordInput from '../components/PasswordInput'
import logoDark from '../assets/images/icon_dark.png'

export default function Login() {
  const { login } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await authApi.login(email, password)
      login(res.data.token, res.data.username, res.data.role, res.data.nombre, res.data.apellidos)
    } catch {
      setError('Email o contraseña incorrectos')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-split">

      <div className="login-split__image">
        <div className="login-split__image-overlay">
          <h1>Gestor de Profesores</h1>
          <p>Gestión integral del equipo docente</p>
        </div>
      </div>

      <div className="login-split__panel">
        <form className="login-split__form" onSubmit={handleSubmit}>
          <img src={logoDark} alt="Logo" className="login-split__logo" />
          <h2>Iniciar sesión</h2>

          <label>
            Email
            <input
              type="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
              autoFocus
            />
          </label>

          <label>
            Contraseña
            <PasswordInput
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
            />
          </label>
         
          {error && <p className="error">{error}</p>}

          <button type="submit" disabled={loading}>
            {loading ? 'Entrando...' : 'Entrar'}
          </button>

          <Link to="/forgot-password" className="login-split__forgot">
            He olvidado mi contraseña
          </Link>
        </form>
      </div>

    </div>
  )
}
