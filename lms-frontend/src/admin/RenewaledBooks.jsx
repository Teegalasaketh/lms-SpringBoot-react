// admin/RenewaledBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function RenewaledBooks() {
  const [list, setList] = useState([]);
  const [popup, setPopup] = useState({ show: false, title: "", message: "", onOk: null });

  useEffect(() => {
    load();
  }, []);

  const load = () =>
    api.get("/admin/renewaled")
      .then(res => setList(res.data))
      .catch(() => showError("Failed to load renewed books"));

  const showError = (msg) =>
    setPopup({ show: true, title: "Error", message: msg, onOk: close });

  const close = () =>
    setPopup({ show: false, title: "", message: "", onOk: null });

  const returnBook = (id) =>
    api.post(`/admin/issues/${id}/return`)
      .then(load)
      .finally(close);

  return (
    <>
      <h3 className="heading-no-underline text-center mb-4">Renewaled Books</h3>

      <div className="glass p-3">
        <table className="table glass-table table-fixed text-center mb-0">
  <thead>
    <tr>
      <th>Issue ID</th><th>ISBN</th><th>Book</th><th>Reg ID</th>
      <th>Student</th><th>Issue</th><th>Renewed</th>
      <th>Due</th><th>Status</th><th>Action</th>
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
        <td>{b.issueDate}</td>
        <td>{b.renewalDate}</td>
        <td>{b.dueDate}</td>
        <td>
          <span className="status-badge success">{b.status}</span>
        </td>
        <td>
          <button
            className="btn-glass btn-sm"
            onClick={() =>
              setPopup({
                show: true,
                title: "Confirm Return",
                message: "Return this book?",
                onOk: () => returnBook(b.issueId)
              })
            }
          >
            📥 Return
          </button>
        </td>
      </tr>
    )) : (
      <tr>
        <td colSpan="10" className="text-muted">
          No renewaled books
        </td>
      </tr>
    )}
  </tbody>
</table>
      </div>

      <ConfirmPopup {...popup} onCancel={close} />
    </>
  );
}

export default RenewaledBooks;
