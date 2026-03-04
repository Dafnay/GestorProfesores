import { Routes, Route, Link } from 'react-router-dom'
import Docentes from './pages/Docentes'
import Departamentos from './pages/Departamentos'
import AsuntosPropios from './pages/AsuntosPropios'

export default function App() {
  return (
    <>
      <nav>
        <Link to="/">Docentes</Link>
        <Link to="/departamentos">Departamentos</Link>
        <Link to="/asuntos-propios">Asuntos Propios</Link>
      </nav>
      <main>
        <Routes>
          <Route path="/" element={<Docentes />} />
          <Route path="/departamentos" element={<Departamentos />} />
          <Route path="/asuntos-propios" element={<AsuntosPropios />} />
        </Routes>
      </main>
    </>
  )
}
