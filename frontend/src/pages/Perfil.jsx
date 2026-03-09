import { useState } from 'react'
import { authApi } from '../api/client'
import { useAuth } from '../context/AuthContext'
import PasswordInput from '../components/PasswordInput'

export default function Perfil() {
  const { auth } = useAuth()
  const [passwordActual, setPasswordActual] = useState('')
  const [passwordNuevo, setPasswordNuevo] = useState('')
  const [confirmar, setConfirmar] = useState('')
  const [error, setError] = useState(null)
  const [exito, setExito] = useState(false)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    if (passwordNuevo !== confirmar) {
      setError('Las contraseñas nuevas no coinciden')
      return
    }
    setError(null)
    setExito(false)
    setLoading(true)
    try {
      await authApi.changePassword(passwordActual, passwordNuevo)
      setExito(true)
      setPasswordActual('')
      setPasswordNuevo('')
      setConfirmar('')
    } catch {
      setError('La contraseña actual es incorrecta')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <h1>Mi perfil</h1>
      <p style={{ marginBottom: '1.5rem', color: '#666' }}>
        {auth?.nombre
          ? <><strong>{auth.nombre} {auth.apellidos}</strong></>
          : <strong>{auth?.username}</strong>}
      </p>

      <div style={{ maxWidth: '400px' }}>
        <h2 style={{ marginBottom: '1rem', fontSize: '1.1rem', color: '#2c3e50' }}>Cambiar contraseña</h2>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <label style={{ display: 'flex', flexDirection: 'column', gap: '0.3rem', fontSize: '0.9rem' }}>
            Contraseña actual
            <PasswordInput
              value={passwordActual}
              onChange={e => setPasswordActual(e.target.value)}
              required
              style={{ padding: '0.5rem 0.7rem', border: '1px solid #ccc', borderRadius: '4px', fontSize: '0.95rem' }}
            />
          </label>
          <label style={{ display: 'flex', flexDirection: 'column', gap: '0.3rem', fontSize: '0.9rem' }}>
            Nueva contraseña
            <PasswordInput
              value={passwordNuevo}
              onChange={e => setPasswordNuevo(e.target.value)}
              required
              minLength={6}
              style={{ padding: '0.5rem 0.7rem', border: '1px solid #ccc', borderRadius: '4px', fontSize: '0.95rem' }}
            />
          </label>
          <label style={{ display: 'flex', flexDirection: 'column', gap: '0.3rem', fontSize: '0.9rem' }}>
            Confirmar nueva contraseña
            <PasswordInput
              value={confirmar}
              onChange={e => setConfirmar(e.target.value)}
              required
              minLength={6}
              style={{ padding: '0.5rem 0.7rem', border: '1px solid #ccc', borderRadius: '4px', fontSize: '0.95rem' }}
            />
          </label>

          {error && <p className="error">{error}</p>}
          {exito && <p style={{ color: 'green' }}>Contraseña cambiada correctamente</p>}

          <button type="submit" disabled={loading}>
            {loading ? 'Guardando...' : 'Cambiar contraseña'}
          </button>
        </form>
      </div>
    </>
  )
}
