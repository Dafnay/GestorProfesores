import axios from 'axios'

const client = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
})

export const docentesApi = {
  getById: (id) => client.get(`/docentes/${id}`),
  getOrdenados: () => client.get('/docentes/ordenados'),
  getByDepartamento: (nombre) => client.get(`/docentes/departamento/${nombre}`)
}

export const departamentosApi = {
  contarPorCodigo: (codigo) => client.get(`/departamentos/contar/${codigo}`)
}

export const asuntosPropiosApi = {
  solicitar: (docenteId, fecha, descripcion) =>
    client.post('/asuntospropios/solicitar', null, { params: { docenteId, fecha, descripcion } }),
  validar: (id, aceptado) =>
    client.put(`/asuntospropios/validar/${id}`, null, { params: { aceptado } }),
  consultar: (docenteId) => client.get(`/asuntospropios/consultar/${docenteId}`),
  pendientes: (docenteId) => client.get(`/asuntospropios/pendientes/${docenteId}`),
  docenteMasDias: () => client.get('/asuntospropios/docente-mas-dias')
}
