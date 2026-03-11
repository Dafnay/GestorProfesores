import { useEffect, useState } from 'react'
import { departamentosApi } from '../api/client'
import { useAuth } from '../context/AuthContext'
import Button from '../components/Button'
import ConfirmModal from '../components/ConfirmModal'
import { PlusIcon, PencilIcon, TrashIcon } from '../components/Icons'

const formVacio = { nombre: '', codigo: '', telefono: '' }

export default function Departamentos() {
  const { auth } = useAuth()
  const isAdmin = auth?.role === 'ADMIN'

  const [departamentos, setDepartamentos] = useState([])
  const [conteos, setConteos] = useState({})
  const [error, setError] = useState(null)
  const [mostrarFormulario, setMostrarFormulario] = useState(false)
  const [editando, setEditando] = useState(null)
  const [form, setForm] = useState(formVacio)
  const [guardando, setGuardando] = useState(false)
  const [formError, setFormError] = useState(null)
  const [confirmar, setConfirmar] = useState(null)

  function cargarDepartamentos() {
    return departamentosApi.getAll()
      .then(res => {
        const lista = res.data
        setDepartamentos(lista)
        return Promise.all(
          lista.map(d =>
            departamentosApi.contarPorCodigo(d.codigo)
              .then(r => ({ codigo: d.codigo, count: r.data }))
          )
        )
      })
      .then(resultados => {
        const map = {}
        resultados.forEach(({ codigo, count }) => { map[codigo] = count })
        setConteos(map)
      })
      .catch(() => setError('No se pudieron cargar los departamentos. ¿Está el backend activo?'))
  }

  useEffect(() => { cargarDepartamentos() }, [])

  function abrirCrear() {
    setEditando(null)
    setForm(formVacio)
    setFormError(null)
    setMostrarFormulario(true)
  }

  function abrirEditar(d) {
    setEditando(d)
    setForm({ nombre: d.nombre, codigo: d.codigo, telefono: d.telefono ?? '' })
    setFormError(null)
    setMostrarFormulario(true)
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
      if (editando) {
        await departamentosApi.actualizar(editando.id, form)
      } else {
        await departamentosApi.crear(form)
      }
      await cargarDepartamentos()
      setMostrarFormulario(false)
    } catch {
      setFormError('Error al guardar el departamento. Comprueba los datos.')
    } finally {
      setGuardando(false)
    }
  }

  async function confirmarEliminar() {
    try {
      await departamentosApi.eliminar(confirmar.id)
      await cargarDepartamentos()
    } catch (err) {
      setError(
        err.response?.status === 409
          ? 'No se puede eliminar: el departamento tiene docentes asignados.'
          : 'Error al eliminar el departamento.'
      )
    } finally {
      setConfirmar(null)
    }
  }

  return (
    <>
      <h1>Departamentos</h1>
      {error && <p className="error">{error}</p>}

      {isAdmin && (
        <Button
          variant="outline-primary"
          icon={<PlusIcon size={15} />}
          onClick={abrirCrear}
          style={{ marginBottom: '1.5rem' }}
        >
          Nuevo departamento
        </Button>
      )}

      {mostrarFormulario && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>{editando ? 'Editar departamento' : 'Nuevo departamento'}</h2>
            <form onSubmit={handleSubmit}>
              <label>
                Nombre
                <input name="nombre" value={form.nombre} onChange={handleChange} required />
              </label>
              <label>
                Código
                <input name="codigo" value={form.codigo} onChange={handleChange} required />
              </label>
              <label>
                Teléfono
                <input name="telefono" value={form.telefono} onChange={handleChange} />
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
          mensaje={`Se eliminará el departamento "${confirmar.nombre}". Esta acción no se puede deshacer.`}
          onConfirm={confirmarEliminar}
          onCancel={() => setConfirmar(null)}
        />
      )}

      <table>
        <thead>
          <tr>
            <th>Código</th>
            <th>Nombre</th>
            <th>Teléfono</th>
            <th>Nº docentes</th>
            {isAdmin && <th>Acciones</th>}
          </tr>
        </thead>
        <tbody>
          {departamentos.map(d => (
            <tr key={d.id}>
              <td>{d.codigo}</td>
              <td>{d.nombre}</td>
              <td>{d.telefono ?? '-'}</td>
              <td>{conteos[d.codigo] ?? '—'}</td>
              {isAdmin && (
                <td style={{ display: 'flex', gap: '0.5rem' }}>
                  <Button
                    size="sm"
                    variant="outline-green"
                    icon={<PencilIcon size={14} />}
                    tooltip="Editar departamento"
                    onClick={() => abrirEditar(d)}
                  />
                  <Button
                    size="sm"
                    variant="outline-red"
                    icon={<TrashIcon size={14} />}
                    tooltip="Eliminar departamento"
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
