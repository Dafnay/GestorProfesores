import { useEffect, useState } from 'react'
import { docentesApi, departamentosApi } from '../api/client'
import { useAuth } from '../context/AuthContext'
import Button from '../components/Button'
import ConfirmModal from '../components/ConfirmModal'
import { PlusIcon, PencilIcon, TrashIcon } from '../components/Icons'

const formVacio = { nombre: '', apellidos: '', email: '', siglas: '', departamentoId: '' }

export default function Docentes() {
  const { auth } = useAuth()
  const isAdmin = auth?.role === 'ADMIN'

  const [docentes, setDocentes] = useState([])
  const [error, setError] = useState(null)
  const [mostrarFormulario, setMostrarFormulario] = useState(false)
  const [editando, setEditando] = useState(null)
  const [departamentos, setDepartamentos] = useState([])
  const [form, setForm] = useState(formVacio)
  const [guardando, setGuardando] = useState(false)
  const [formError, setFormError] = useState(null)
  const [confirmar, setConfirmar] = useState(null)

  function cargarDocentes() {
    return docentesApi.getOrdenados()
      .then(res => setDocentes(res.data))
      .catch(() => setError('No se pudieron cargar los docentes. ¿Está el backend activo?'))
  }

  useEffect(() => { cargarDocentes() }, [])

  function abrirCrear() {
    setEditando(null)
    setForm(formVacio)
    setFormError(null)
    departamentosApi.getAll()
      .then(res => {
        setDepartamentos(res.data)
        setMostrarFormulario(true)
      })
  }

  function abrirEditar(d) {
    setEditando(d)
    setForm({
      nombre: d.nombre,
      apellidos: d.apellidos,
      email: d.email,
      siglas: d.siglas,
      departamentoId: d.departamento?.id ?? ''
    })
    setFormError(null)
    departamentosApi.getAll()
      .then(res => {
        setDepartamentos(res.data)
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
      const payload = {
        ...form,
        departamentoId: form.departamentoId ? Number(form.departamentoId) : null
      }
      if (editando) {
        await docentesApi.actualizar(editando.id, payload)
      } else {
        await docentesApi.crear(payload)
      }
      await cargarDocentes()
      setMostrarFormulario(false)
    } catch {
      setFormError('Error al guardar el docente. Comprueba los datos.')
    } finally {
      setGuardando(false)
    }
  }

  async function confirmarEliminar() {
    try {
      await docentesApi.eliminar(confirmar.id)
      await cargarDocentes()
    } catch {
      setError('Error al eliminar el docente.')
    } finally {
      setConfirmar(null)
    }
  }

  return (
    <>
      <h1>Docentes</h1>
      {error && <p className="error">{error}</p>}

      {isAdmin && (
        <Button
          variant="outline-primary"
          icon={<PlusIcon size={15} />}
          onClick={abrirCrear}
          style={{ marginBottom: '1.5rem' }}
        >
          Nuevo docente
        </Button>
      )}

      {mostrarFormulario && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>{editando ? 'Editar docente' : 'Nuevo docente'}</h2>
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
              {formError && <p className="error">{formError}</p>}
              <div className="modal-actions">
                <Button type="submit" loading={guardando}>Guardar</Button>
                <Button variant="secondary" onClick={cerrarFormulario}>Cancelar</Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {confirmar && (
        <ConfirmModal
          mensaje={`Se eliminará al docente "${confirmar.nombre} ${confirmar.apellidos}". Esta acción no se puede deshacer.`}
          onConfirm={confirmarEliminar}
          onCancel={() => setConfirmar(null)}
        />
      )}

      <table>
        <thead>
          <tr>
            <th>Siglas</th>
            <th>Nombre</th>
            <th>Apellidos</th>
            <th>Email</th>
            <th>Departamento</th>
            {isAdmin && <th>Acciones</th>}
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
              {isAdmin && (
                <td style={{ display: 'flex', gap: '0.5rem' }}>
                  <Button
                    size="sm"
                    variant="outline-green"
                    icon={<PencilIcon size={14} />}
                    tooltip="Editar docente"
                    onClick={() => abrirEditar(d)}
                  />
                  <Button
                    size="sm"
                    variant="outline-red"
                    icon={<TrashIcon size={14} />}
                    tooltip="Eliminar docente"
                    onClick={() => setConfirmar(d)}
                  />
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}
