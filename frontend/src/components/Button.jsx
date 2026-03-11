export default function Button({
  children,
  variant = 'primary',
  icon,
  iconPosition = 'left',
  size = 'md',
  loading = false,
  type = 'button',
  disabled,
  className = '',
  tooltip,
  onClick,
  ...rest
}) {
  const classes = ['btn', `btn--${variant}`, `btn--${size}`, className]
    .filter(Boolean)
    .join(' ')

  const isDisabled = disabled || loading

  return (
    <button
      type={type}
      className={classes}
      disabled={isDisabled}
      onClick={onClick}
      data-tooltip={tooltip || undefined}
      {...rest}
    >
      {loading && <span className="btn__spinner" aria-hidden="true" />}
      {!loading && icon && iconPosition === 'left' && (
        <span className="btn__icon">{icon}</span>
      )}
      {children && <span className="btn__label">{children}</span>}
      {!loading && icon && iconPosition === 'right' && (
        <span className="btn__icon">{icon}</span>
      )}
    </button>
  )
}
