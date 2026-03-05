import { useEffect, useState } from 'react'
import { docentesApi, departamentosApi, rolesApi } from '../api/client'
import { useAuth } from '../context/AuthContext'

const formVacio = { nombre: '', apellidos: '', email: '', siglas: '', departamentoId: '', rolId: '' }

export default function Docentes() {
  const { auth } = useAuth()
  const isAdmin = auth?.role === 'ADMIN'

  const [docentes, setDocentes] = useState([])
  const [error, setError] = useState(null)
  const [mostrarFormulario, setMostrarFormulario] = useState(false)
  const [departamentos, setDepartamentos] = useState([])
  const [roles, setRoles] = useState([])
  const [form, setForm] = useState(formVacio)
  const [guardando, setGuardando] = useState(false)
  const [formError, setFormError] = useState(null)

  useEffect(() => {
    docentesApi.getOrdenados()
      .then(res => setDocentes(res.data))
      .catch(() => setError('No se pudieron cargar los docentes. ¿Está el backend activo?'))
  }, [])

  function abrirFormulario() {
    setForm(formVacio)
    setFormError(null)
    Promise.all([departamentosApi.getAll(), rolesApi.getAll()])
      .then(([dRes, rRes]) => {
        setDepartamentos(dRes.data)
        setRoles(rRes.data)
        setMostrarFormulario(true)
      })
  }

  function cerrarFormulario() {
    setMostrarFormulario(false)
  }

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setFormError(null)
    setGuardando(true)
    try {
      await docentesApi.crear({
        ...form,
        departamentoId: form.departamentoId ? Number(form.departamentoId) : null,
        rolId: form.rolId ? Number(form.rolId) : null
      })
      const lista = await docentesApi.getOrdenados()
      setDocentes(lista.data)
      setMostrarFormulario(false)
    } catch {
      setFormError('Error al crear el docente. Comprueba los datos.')
    } finally {
      setGuardando(false)
    }
  }

  return (
    <>
      <h1>Docentes</h1>
      {error && <p className="error">{error}</p>}

      {isAdmin && (
        <button onClick={abrirFormulario}>+ Nuevo docente</button>
      )}

      {mostrarFormulario && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>Nuevo docente</h2>
            <form onSubmit={handleSubmit}>
              <label>
                Nombre
                <input name="nombre" value={form.nombre} onChange={handleChange} required />
              </label>
              <label>
                Apellidos
                <input name="apellidos" value={form.apellidos} onChange={handleChange} required />
              </label>
              <label>
                Email
                <input name="email" type="email" value={form.email} onChange={handleChange} required />
              </label>
              <label>
                Siglas
                <input name="siglas" value={form.siglas} onChange={handleChange} required />
              </label>
              <label>
                Departamento
                <select name="departamentoId" value={form.departamentoId} onChange={handleChange}>
                  <option value="">— Sin departamento —</option>
                  {departamentos.map(d => (
                    <option key={d.id} value={d.id}>{d.nombre}</option>
                  ))}
                </select>
              </label>
              <label>
                Rol
                <select name="rolId" value={form.rolId} onChange={handleChange}>
                  <option value="">— Sin rol —</option>
                  {roles.map(r => (
                    <option key={r.id} value={r.id}>{r.nombre}</option>
                  ))}
                </select>
              </label>

              {formError && <p className="error">{formError}</p>}

              <div className="modal-actions">
                <button type="submit" disabled={guardando}>
                  {guardando ? 'Guardando...' : 'Guardar'}
                </button>
                <button type="button" onClick={cerrarFormulario}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <table>
        <thead>
          <tr>
            <th>Siglas</th>
            <th>Nombre</th>
            <th>Apellidos</th>
            <th>Email</th>
            <th>Departamento</th>
            <th>Rol</th>
          </tr>
        </thead>
        <tbody>
          {docentes.map(d => (
            <tr key={d.id}>
              <td>{d.siglas}</td>
              <td>{d.nombre}</td>
              <td>{d.apellidos}</td>
              <td>{d.email}</td>
              <td>{d.departamento?.nombre ?? '-'}</td>
              <td>{d.rol?.nombre ?? '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}
