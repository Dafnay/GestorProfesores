import { useState } from 'react'
import { useSearchParams, useNavigate } from 'react-router-dom'
import { authApi } from '../api/client'

export default function SetupPassword() {
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token')
  const navigate = useNavigate()

  const [password, setPassword] = useState('')
  const [confirmar, setConfirmar] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    if (password !== confirmar) {
      setError('Las contraseñas no coinciden')
      return
    }
    setError(null)
    setLoading(true)
    try {
      await authApi.setupPassword(token, password)
      navigate('/', { replace: true })
    } catch {
      setError('El enlace no es válido o ha expirado')
    } finally {
      setLoading(false)
    }
  }

  if (!token) {
    return (
      <div className="login-container">
        <p className="error">Enlace inválido</p>
      </div>
    )
  }

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h1>Gestor de Profesores</h1>
        <h2>Configura tu contraseña</h2>

        <label>
          Nueva contraseña
          <input
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
            minLength={6}
            autoFocus
          />
        </label>

        <label>
          Confirmar contraseña
          <input
            type="password"
            value={confirmar}
            onChange={e => setConfirmar(e.target.value)}
            required
            minLength={6}
          />
        </label>

        {error && <p className="error">{error}</p>}

        <button type="submit" disabled={loading}>
          {loading ? 'Guardando...' : 'Guardar contraseña'}
        </button>
      </form>
    </div>
  )
}
