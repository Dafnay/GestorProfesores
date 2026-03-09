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
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h1>Gestor de Profesores</h1>
        <h2>Recuperar contraseña</h2>

        <p style={{ fontSize: '0.85rem', color: '#666', marginBottom: '-0.3rem' }}>
          Introduce tu email y te enviaremos un enlace para restablecer tu contraseña.
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

        <Link to="/" style={{ textAlign: 'center', fontSize: '0.85rem', color: '#2c3e50' }}>
          Volver al inicio de sesión
        </Link>
      </form>
    </div>
  )
}
