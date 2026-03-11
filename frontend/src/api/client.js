import axios from 'axios'

const client = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
})

client.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

export const authApi = {
  login: (email, password) => client.post('/auth/login', { email, password }),
  setupPassword: (token, password) => client.post('/auth/setup-password', { token, password }),
  forgotPassword: (email) => client.post('/auth/forgot-password', { email }),
  changePassword: (passwordActual, passwordNuevo) => client.post('/auth/change-password', { passwordActual, passwordNuevo })
}

export const docentesApi = {
  getById: (id) => client.get(`/docentes/${id}`),
  getOrdenados: () => client.get('/docentes/ordenados'),
  getByDepartamento: (nombre) => client.get(`/docentes/departamento/${nombre}`),
  crear: (data) => client.post('/docentes', data),
  actualizar: (id, data) => client.put(`/docentes/${id}`, data),
  eliminar: (id) => client.delete(`/docentes/${id}`)
}

export const departamentosApi = {
  getAll: () => client.get('/departamentos'),
  contarPorCodigo: (codigo) => client.get(`/departamentos/contar/${codigo}`),
  crear: (data) => client.post('/departamentos', data),
  actualizar: (id, data) => client.put(`/departamentos/${id}`, data),
  eliminar: (id) => client.delete(`/departamentos/${id}`)
}

export const rolesApi = {
  getAll: () => client.get('/roles')
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
