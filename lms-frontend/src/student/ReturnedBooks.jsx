// student/ReturnedBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";

function ReturnedBooks() {
  const [list, setList] = useState([]);

  useEffect(() => {
    api.get("/student/returned")
      .then(res => setList(res.data))
      .catch(() => alert("Failed"));
  }, []);

  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
        Returned Books
      </h3>

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
                <td>
                  <span className="status-badge neutral">{b.status}</span>
                </td>
              </tr>
            )) : (
              <tr>
                <td colSpan="4" className="text-muted">
                  No returned books
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </>
  );
}

export default ReturnedBooks;
