import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { authApi } from '../api/client'

export default function ForgotPassword() {
  const [email, setEmail] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  async function handleSubmit(e) {
    e.preventDefault()
    setLoading(true)
    try {
      await authApi.forgotPassword(email)
    } finally {
      setLoading(false)
      navigate('/', { replace: true })
    }
  }

  return (
    <div className="login-split">

      <div className="login-split__panel">
        <form className="login-split__form" onSubmit={handleSubmit}>
          <h2>Recuperar contraseña</h2>
          <p className="login-split__subtitle">
            Introduce tu email y te enviaremos un enlace para restablecerla.
          </p>

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

          <button type="submit" disabled={loading}>
            {loading ? 'Enviando...' : 'Enviar enlace'}
          </button>

          <Link to="/" className="login-split__forgot">
            Volver al inicio de sesión
          </Link>
        </form>
      </div>

      <div className="login-split__image">
        <div className="login-split__image-overlay">
          <h1>Gestor de Profesores</h1>
          <p>Gestión integral del equipo docente</p>
        </div>
      </div>

    </div>
  )
}
