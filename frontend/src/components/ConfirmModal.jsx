import Button from './Button'
import { TrashIcon } from './Icons'

export default function ConfirmModal({ mensaje, onConfirm, onCancel }) {
  return (
    <div className="modal-overlay">
      <div className="modal modal--confirm">
        <div className="modal-confirm__icon">
          <TrashIcon size={28} />
        </div>
        <h2>¿Estás seguro?</h2>
        <p className="modal-confirm__mensaje">{mensaje}</p>
        <div className="modal-actions">
          <Button variant="danger" onClick={onConfirm}>Eliminar</Button>
          <Button variant="secondary" onClick={onCancel}>Cancelar</Button>
        </div>
      </div>
    </div>
  )
}
