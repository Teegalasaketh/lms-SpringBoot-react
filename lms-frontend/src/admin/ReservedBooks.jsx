// admin/ReservedBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function ReservedBooks() {
  const [list, setList] = useState([]);

  const [popup, setPopup] = useState({
    show: false,
    title: "",
    message: "",
    onOk: null
  });

  useEffect(() => {
    loadReservedBooks();
  }, []);

  const loadReservedBooks = () => {
    api.get("/admin/issues/reserved")
      .then(res => setList(res.data))
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Failed to load reserved books",
          onOk: closePopup
        })
      );
  };

  const closePopup = () => {
    setPopup({ show: false, title: "", message: "", onOk: null });
  };

  const issueBook = (issueId) => {
    api.post(`/admin/issues/${issueId}/issue`)
      .then(() => {
        closePopup();
        loadReservedBooks();
      })
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Issue failed",
          onOk: closePopup
        })
      );
  };

  const confirmIssue = (issueId) => {
    setPopup({
      show: true,
      title: "Confirm Issue",
      message: "Issue this reserved book?",
      onOk: () => issueBook(issueId)
    });
  };

  return (
    <>
      {/* PAGE TITLE */}
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
        Reserved Books
      </h3>

      {/* GLASS CONTAINER */}
      <div className="glass p-3">

        <table className="table glass-table table-fixed text-center align-middle mb-0">
          <thead>
            <tr>
              <th>Issue ID</th>
              <th>ISBN</th>
              <th>Book Name</th>
              <th>Reg ID</th>
              <th>Name</th>
              <th>Reserve Date</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          {/* 🔥 SCROLL ONLY HERE */}
          <tbody className="table-scroll-body">
            {list.length > 0 ? (
              list.map(b => (
                <tr key={b.issueId}>
                  <td>{b.issueId}</td>
                  <td>{b.isbn}</td>
                  <td>{b.bookName}</td>
                  <td>{b.regId}</td>
                  <td>{b.name}</td>
                  <td>{b.reserveDate}</td>
                  <td>
                    <span className="status-badge warning">
                      {b.status}
                    </span>
                  </td>
                  <td>
                    <button
                      className="btn-glass btn-sm"
                      onClick={() => confirmIssue(b.issueId)}
                    >
                      📤 Issue
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8" className="text-muted">
                  No reserved books found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* CONFIRM POPUP */}
      <ConfirmPopup
        show={popup.show}
        title={popup.title}
        message={popup.message}
        onOk={popup.onOk}
        onCancel={closePopup}
      />
    </>
  );
}

export default ReservedBooks;
