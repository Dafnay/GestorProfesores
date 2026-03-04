import { useEffect, useState } from 'react'
import { docentesApi } from '../api/client'

export default function Docentes() {
  const [docentes, setDocentes] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    docentesApi.getOrdenados()
      .then(res => setDocentes(res.data))
      .catch(() => setError('No se pudieron cargar los docentes. ¿Está el backend activo?'))
  }, [])

  return (
    <>
      <h1>Docentes</h1>
      {error && <p className="error">{error}</p>}
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
