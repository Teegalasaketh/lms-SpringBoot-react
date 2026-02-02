// student/ReservedBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function ReservedBooks() {
  const [list, setList] = useState([]);
  const [popup, setPopup] = useState({ show: false, title: "", message: "", onOk: null });

  useEffect(() => {
    api.get("/student/reserved")
      .then(res => setList(res.data))
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Failed to load reserved books",
          onOk: closePopup
        })
      );
  }, []);

  const closePopup = () =>
    setPopup({ show: false, title: "", message: "", onOk: null });

  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">Reserved Books</h3>

      <div className="glass p-3">
        <table className="table table-hover text-center mb-0 glass-table table-fixed">
          <thead>
            <tr>
              <th>Issue No</th>
              <th>Book</th>
              <th>Date</th>
              <th>Status</th>
            </tr>
          </thead>

          <tbody className="table-scroll-body">
            {list.length ? list.map(b => (
              <tr key={b.issueId}>
                <td>{b.studentIssueNo}</td>
                <td>{b.bookName}</td>
                <td>{b.reserveDate}</td>
                <td><span className="status-badge warning">{b.status}</span></td>
              </tr>
            )) : (
              <tr>
                <td colSpan="4" className="text-muted">No reserved books</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <ConfirmPopup {...popup} onCancel={closePopup} />
    </>
  );
}

export default ReservedBooks;
