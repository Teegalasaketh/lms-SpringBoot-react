// components/ConfirmPopup.jsx
import { useEffect } from "react";

function ConfirmPopup({ show, title, message, onOk, onCancel }) {

  useEffect(() => {
    if (!show) return;

    const handleKey = (e) => {
      if (e.key === "Escape" && onCancel) onCancel();
      if (e.key === "Enter" && onOk) onOk();
    };

    window.addEventListener("keydown", handleKey);
    return () => window.removeEventListener("keydown", handleKey);
  }, [show, onOk, onCancel]);

  
  if (!show) return null;

  return (
    <div className="popup-backdrop" onClick={onCancel}>
      <div
        className="popup-box"
        onClick={(e) => e.stopPropagation()}
      >
        <h5 className="heading-no-underline text-center mb-4">{title}</h5>
        <p className="popup-message">{message}</p>

        <div className="popup-actions">
          {onCancel && (
            <button
              className="btn-glass btn-sm"
              onClick={onCancel}
            >
              Cancel
            </button>
          )}

          <button
            className="btn-glass btn-accent btn-sm"
            onClick={onOk}
          >
            OK
          </button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmPopup;
