// admin/IssuedBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function IssuedBooks() {
  const [list, setList] = useState([]);
  const [popup, setPopup] = useState({ show: false, title: "", message: "", onOk: null });

  useEffect(() => {
    loadIssuedBooks();
  }, []);

  const loadIssuedBooks = () => {
    api.get("/admin/issues/issued")
      .then(res => setList(res.data))
      .catch(() => showError("Failed to load issued books"));
  };

  const showError = (msg) =>
    setPopup({ show: true, title: "Error", message: msg, onOk: closePopup });

  const closePopup = () =>
    setPopup({ show: false, title: "", message: "", onOk: null });

  const confirmAction = (title, message, action) =>
    setPopup({ show: true, title, message, onOk: action });

  const renewBook = (id) =>
    api.post(`/admin/issues/${id}/renew`)
      .then(loadIssuedBooks)
      .finally(closePopup);

  const returnBook = (id) =>
    api.post(`/admin/issues/${id}/return`)
      .then(loadIssuedBooks)
      .finally(closePopup);

  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">Issued Books</h3>

      <div className="glass p-3">
        <table className="table glass-table table-fixed text-center mb-0">
  <thead>
    <tr>
      <th>Issue ID</th><th>ISBN</th><th>Book</th><th>Reg ID</th>
      <th>Name</th><th>Date</th><th>Status</th><th colSpan="2">Actions</th>
    </tr>
  </thead>

  <tbody className="table-scroll-body">
    {list.length ? list.map(b => (
      <tr key={b.issueId}>
        <td>{b.issueId}</td>
        <td>{b.isbn}</td>
        <td>{b.bookName}</td>
        <td>{b.regId}</td>
        <td>{b.name}</td>
        <td>{b.reserveDate}</td>
        <td>
          <span className="status-badge info">{b.status}</span>
        </td>
        <td>
          <button
            className="btn-glass btn-sm"
            onClick={() =>
              confirmAction(
                "Confirm Renewal",
                "Renew this book?",
                () => renewBook(b.issueId)
              )
            }
          >
            🔄 Renew
          </button>
        </td>
        <td>
          <button
            className="btn-glass btn-sm"
            onClick={() =>
              confirmAction(
                "Confirm Return",
                "Return this book?",
                () => returnBook(b.issueId)
              )
            }
          >
            📥 Return
          </button>
        </td>
      </tr>
    )) : (
      <tr>
        <td colSpan="9" className="text-muted">
          No issued books found
        </td>
      </tr>
    )}
  </tbody>
</table>
      </div>

      <ConfirmPopup {...popup} onCancel={closePopup} />
    </>
  );
}

export default IssuedBooks;
