// student/IssuedBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function IssuedBooks() {
  const [list, setList] = useState([]);
  const [popup, setPopup] = useState({ show: false, title: "", message: "", onOk: null });

  useEffect(() => {
    api.get("/student/issued")
      .then(res => setList(res.data))
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Failed to load issued books",
          onOk: closePopup
        })
      );
  }, []);

  const closePopup = () =>
    setPopup({ show: false, title: "", message: "", onOk: null });

  const confirmReturn = (id) =>
    setPopup({
      show: true,
      title: "Confirm Return",
      message: "Return this book?",
      onOk: () => returnBook(id)
    });

  const returnBook = (id) =>
    api.post(`/student/return/${id}`)
      .then(() => {
        closePopup();
        setList(list.filter(b => b.issueId !== id));
      })
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Return failed",
          onOk: closePopup
        })
      );

  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">Issued Books</h3>

      <div className="glass p-3">
        <table className="table table-hover text-center mb-0 glass-table table-fixed">
          <thead>
            <tr>
              <th>Issue No</th>
              <th>ISBN</th>
              <th>Book</th>
              <th>Date</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody className="table-scroll-body">
            {list.length ? list.map(b => (
              <tr key={b.issueId}>
                <td>{b.studentIssueNo}</td>
                <td>{b.isbn}</td>
                <td>{b.bookName}</td>
                <td>{b.reserveDate}</td>
                <td><span className="status-badge info">{b.status}</span></td>
                <td>
                  <div className="action-stack">
                  <button
                    className="btn-glass btn-sm"
                    onClick={() => confirmReturn(b.issueId)}
                  >
                    Return
                  </button>
                  </div>
                </td>
              </tr>
            )) : (
              <tr>
                <td colSpan="6" className="text-muted">No issued books</td>
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
