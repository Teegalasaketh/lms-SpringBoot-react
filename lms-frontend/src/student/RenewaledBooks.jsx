// student/RenewaledBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function RenewaledBooks() {
  const [list, setList] = useState([]);
  const [popup, setPopup] = useState({ show: false, title: "", message: "", onOk: null });

  useEffect(() => {
    api.get("/student/renewaled")
      .then(res => setList(res.data))
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Failed to load renewaled books",
          onOk: closePopup
        })
      );
  }, []);

  const closePopup = () =>
    setPopup({ show: false, title: "", message: "", onOk: null });

  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">Renewaled Books</h3>

      <div className="glass p-3">
        <table className="table table-hover text-center mb-0 glass-table table-fixed">
          <thead>
            <tr>
              <th>Issue No</th>
              <th>Book</th>
              <th>Status</th>
              <th>Due</th>
            </tr>
          </thead>

          <tbody className="table-scroll-body">
            {list.length ? list.map(b => (
              <tr key={b.issueId}>
                <td>{b.studentIssueNo}</td>
                <td>{b.bookName}</td>
                <td><span className="status-badge success">{b.status}</span></td>
                <td>{b.dueDate || "N/A"}</td>
              </tr>
            )) : (
              <tr>
                <td colSpan="4" className="text-muted">No renewals</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <ConfirmPopup {...popup} onCancel={closePopup} />
    </>
  );
}

export default RenewaledBooks;
